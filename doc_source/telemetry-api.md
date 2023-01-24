# Lambda Telemetry API<a name="telemetry-api"></a>

Using the Lambda Telemetry API, your extensions can directly receive telemetry data from Lambda\. During function initialization and invocation, Lambda automatically captures telemetry, such as logs, platform metrics, and platform traces\. With Telemetry API, extensions can get this telemetry data directly from Lambda in near real time\.

You can subscribe your Lambda extensions to telemetry streams directly from within the Lambda execution environment\. After subscribing, Lambda automatically streams all telemetry data to your extensions\. You can then process, filter, and deliver that data to your preferred destination, such as an Amazon Simple Storage Service \(Amazon S3\) bucket or a third\-party observability tools provider\.

The following diagram shows how the Extensions API and Telemetry API connect extensions to Lambda from within the execution environment\. The Runtime API also connects your runtime and function to Lambda\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/telemetry-api-concept-diagram.png)

**Important**  
The Lambda Telemetry API supersedes the Lambda Logs API\. **While the Logs API remains fully functional, we recommend using only the Telemetry API going forward\.** You can subscribe your extension to a telemetry stream using either the Telemetry API or the Logs API\. After subscribing using one of these APIs, any attempt to subscribe using the other API returns an error\.

Extensions can use the Telemetry API to subscribe to three different telemetry streams:
+ **Platform telemetry** – Logs, metrics, and traces, which describe events and errors related to the execution environment runtime lifecycle, extension lifecycle, and function invocations\.
+ **Function logs** – Custom logs that the Lambda function code generates\.
+ **Extension logs** – Custom logs that the Lambda extension code generates\.

**Note**  
Lambda sends logs and metrics to CloudWatch, and traces to X\-Ray \(if you've activated tracing\), even if an extension subscribes to telemetry streams\.

**Topics**
+ [Creating extensions using the Telemetry API](#telemetry-api-creating-extensions)
+ [Registering your extension](#telemetry-api-registration)
+ [Creating a telemetry listener](#telemetry-api-listener)
+ [Specifying a destination protocol](#telemetry-api-destination)
+ [Configuring memory usage and buffering](#telemetry-api-buffering)
+ [Sending a subscription request to the Telemetry API](#telemetry-api-subscription)
+ [Inbound Telemetry API messages](#telemetry-api-messages)
+ [Lambda Telemetry API reference](telemetry-api-reference.md)
+ [Lambda Telemetry API `Event` schema reference](telemetry-schema-reference.md)
+ [Converting Lambda Telemetry API `Event` objects to OpenTelemetry Spans](telemetry-otel-spans.md)
+ [Lambda Logs API](runtimes-logs-api.md)

## Creating extensions using the Telemetry API<a name="telemetry-api-creating-extensions"></a>

Lambda extensions run as independent processes in the execution environment, and can continue to run after the function invocation completes\. Because extensions are separate processes, you can write them in a language different from the function code\. We recommend implementing extensions using a compiled language such as Golang or Rust\. This way, the extension is a self\-contained binary that can be compatible with any supported runtime\.

The following diagram illustrates a four\-step process to create an extension that receives and processes telemetry data using the Telemetry API\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/telemetry-api-creation-steps.png)

Here is each step in more detail:

1. Register your extension using the [Lambda Extensions API](runtimes-extensions-api.md)\. This provides you with a `Lambda-Extension-Identifier`, which you'll need in the following steps\. For more information about how to register your extension, see [Registering your extension](#telemetry-api-registration)\.

1. Create a telemetry listener\. This can be a basic HTTP or TCP server\. Lambda uses the URI of the telemetry listener to send telemetry data to your extension\. For more information, see [Creating a telemetry listener](#telemetry-api-listener)\.

1. Using the Subscribe API in the Telemetry API, subscribe your extension to your desired telemetry streams\. You'll need the URI of your telemetry listener for this step\. For more information, see [Sending a subscription request to the Telemetry API](#telemetry-api-subscription)\.

1. Get telemetry data from Lambda via the telemetry listener\. You can do any custom processing of this data, such as dispatching the data to Amazon S3 or to an external observability service\.

**Note**  
A Lambda function's execution environment can start and stop multiple times as part of its [lifecycle](runtimes-extensions-api.md#runtimes-extensions-api-lifecycle)\. In general, your extension code runs during function invocations, and also up to 2 seconds during the shutdown phase\. We recommend batching the telemetry as it arrives to your listener, and using the `Invoke` and `Shutdown` lifecycle events to dispatch each batch to their desired destinations\.

## Registering your extension<a name="telemetry-api-registration"></a>

Before you can subscribe to receive telemetry data, you must register your Lambda extension\. Registration occurs during the [extension initialization phase](runtimes-extensions-api.md#runtimes-extensions-api-reg)\. The following example shows an HTTP request to register an extension\.

```
POST http://${AWS_LAMBDA_RUNTIME_API}/2020-01-01/extension/register
 Lambda-Extension-Name: lambda_extension_name
{
    'events': [ 'INVOKE', 'SHUTDOWN']
}
```

If the request succeeds, the subscriber receives an HTTP 200 success response\. The response header contains the `Lambda-Extension-Identifier`\. The response body contains other properties of the function\.

```
HTTP/1.1 200 OK
Lambda-Extension-Identifier: a1b2c3d4-5678-90ab-cdef-EXAMPLE11111
{
    "functionName": "lambda_function",
    "functionVersion": "$LATEST",
    "handler": "lambda_handler",
    "accountId": "123456789012"
}
```

For more information, see the [Extensions API reference](runtimes-extensions-api.md#runtimes-extensions-registration-api)\.

## Creating a telemetry listener<a name="telemetry-api-listener"></a>

Your Lambda extension must have a listener that handles incoming requests from the Telemetry API\. The following code shows an example telemetry listener implementation in Golang:

```
// Starts the server in a goroutine where the log events will be sent
func (s *TelemetryApiListener) Start() (string, error) {
	address := listenOnAddress()
	l.Info("[listener:Start] Starting on address", address)
	s.httpServer = &http.Server{Addr: address}
	http.HandleFunc("/", s.http_handler)
	go func() {
		err := s.httpServer.ListenAndServe()
		if err != http.ErrServerClosed {
			l.Error("[listener:goroutine] Unexpected stop on Http Server:", err)
			s.Shutdown()
		} else {
			l.Info("[listener:goroutine] Http Server closed:", err)
		}
	}()
	return fmt.Sprintf("http://%s/", address), nil
}

// http_handler handles the requests coming from the Telemetry API.
// Everytime Telemetry API sends log events, this function will read them from the response body
// and put into a synchronous queue to be dispatched later.
// Logging or printing besides the error cases below is not recommended if you have subscribed to
// receive extension logs. Otherwise, logging here will cause Telemetry API to send new logs for
// the printed lines which may create an infinite loop.
func (s *TelemetryApiListener) http_handler(w http.ResponseWriter, r *http.Request) {
	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		l.Error("[listener:http_handler] Error reading body:", err)
		return
	}

	// Parse and put the log messages into the queue
	var slice []interface{}
	_ = json.Unmarshal(body, &slice)

	for _, el := range slice {
		s.LogEventsQueue.Put(el)
	}

	l.Info("[listener:http_handler] logEvents received:", len(slice), " LogEventsQueue length:", s.LogEventsQueue.Len())
	slice = nil
}
```

## Specifying a destination protocol<a name="telemetry-api-destination"></a>

When you subscribe to receive telemetry using the Telemetry API, you can specify a destination protocol in addition to the destination URI:

```
{
    "destination": {
        "protocol": "HTTP",
        "URI": "http://sandbox.localdomain:8080"
    }
}
```

Lambda accepts two protocols for receiving telemetry:
+ **HTTP \(recommended\)** – Lambda delivers telemetry to a local HTTP endpoint \(`http://sandbox.localdomain:${PORT}/${PATH}`\) as an array of records in JSON format\. The `$PATH` parameter is optional\. Lambda supports only HTTP, not HTTPS\. Lambda delivers telemetry through POST requests\.
+ **TCP** – Lambda delivers telemetry to a TCP port in [Newline delimited JSON \(NDJSON\) format](https://github.com/ndjson/ndjson-spec)\.

**Note**  
We strongly recommend using HTTP rather than TCP\. With TCP, the Lambda platform cannot acknowledge when it delivers telemetry to the application layer\. Therefore, if your extension crashes, you might lose telemetry\. HTTP does not have this limitation\.

Before subscribing to receive telemetry, set up the local HTTP listener or TCP port\. During setup, note the following:
+ Lambda sends telemetry only to destinations that are inside the execution environment\.
+ Lambda retries the attempt to send telemetry \(with backoff\) if there is no listener, or if the POST request results in an error\. If the telemetry listener crashes, then it continues to receive telemetry after Lambda restarts the execution environment\.
+ Lambda reserves port 9001\. There are no other port number restrictions or recommendations\.

## Configuring memory usage and buffering<a name="telemetry-api-buffering"></a>

An execution environment's memory usage increases linearly as the number of subscribers increases\. Subscriptions consume memory resources because each subscription opens a new memory buffer to store telemetry data\. Buffer memory usage counts towards overall memory consumption in the execution environment\.

When you subscribe to receive telemetry using the Telemetry API, you can buffer telemetry data and deliver it to subscribers in batches\. To help optimize memory usage, you can specify a buffering configuration:

```
{
    "buffering": {
        "maxBytes": 256*1024,
        "maxItems": 1000,
        "timeoutMs": 100
    }
}
```


**Buffering configuration settings**  

| Parameter | Description | Defaults and limits | 
| --- | --- | --- | 
|  `maxBytes`  |  The maximum volume of telemetry \(in bytes\) to buffer in memory\.  |  Default: 262,144 Minimum: 262,144 Maximum: 1,048,576  | 
|  `maxItems`  |  The maximum number of events to buffer in memory\.  |  Default: 10,000 Minimum: 1,000 Maximum: 10,000  | 
|  `timeoutMs`  |  The maximum time \(in milliseconds\) to buffer a batch\.  |  Default: 1,000 Minimum: 25 Maximum: 30,000  | 

When configuring buffering, note the following points:
+ If any of the input streams are closed, then Lambda flushes the logs\. This can happen if, for example, the runtime crashes\.
+ Each subscriber can specify a different buffering configuration in their subscription request\.
+ Consider the buffer size that you need for reading the data\. Expect to receive payloads as large as `2 * maxBytes + metadataBytes`, where `maxBytes` is part of your buffering configuration\. To get an idea of how much `metadataBytes` you should account for, review the following metadata\. Lambda adds metadata similar to this to each record:

  ```
  {
     "time": "2022-08-20T12:31:32.123Z",
     "type": "function",
     "record": "Hello World"
  }
  ```
+ If the subscriber cannot process incoming telemetry fast enough, Lambda might drop records to keep memory utilization bounded\. When this occurs, Lambda sends a `platform.logsDropped` event\.

## Sending a subscription request to the Telemetry API<a name="telemetry-api-subscription"></a>

Lambda extensions can subscribe to receive telemetry data by sending a subscription request to the Telemetry API\. The subscription request should contain information about the types of events that you want the extension to subscribe to\. In addition, the request can contain [delivery destination information](#telemetry-api-destination) and a [buffering configuration](#telemetry-api-buffering)\.

Before sending a subscription request, you must have an extension ID \(`Lambda-Extension-Identifier`\)\. When you [register your extension with the Extensions API](#telemetry-api-registration), you obtain an extension ID from the API response\.

Subscription occurs during the [extension initialization phase](runtimes-extensions-api.md#runtimes-extensions-api-reg)\. The following example shows an HTTP request to subscribe to all three telemetry streams: platform telemetry, function logs, and extension logs\.

```
PUT http://${AWS_LAMBDA_RUNTIME_API}/2022-07-01/telemetry HTTP/1.1
{
   "schemaVersion": "2022-07-01",
   "types": [
        "platform",
        "function",
        "extension"
   ],
   "buffering": {
        "maxItems": 1000,
        "maxBytes": 256*1024,
        "timeoutMs": 100
   },
   "destination": {
        "protocol": "HTTP",
        "URI": "http://sandbox.localdomain:8080"
   }
}
```

If the request succeeds, then the subscriber receives an HTTP 200 success response\.

```
HTTP/1.1 200 OK
"OK"
```

## Inbound Telemetry API messages<a name="telemetry-api-messages"></a>

After subscribing using the Telemetry API, an extension automatically starts to receive telemetry from Lambda via POST requests to the telemetry listener\. Each POST request body contains an array of `Event` objects\. `Event` is a JSON object with the following schema:

```
{
   time: String,
   type: String,
   record: Object
}
```
+ The `time` property defines when the Lambda platform generated the event\. This isn't the same as when the event actually occurred\. The string value of `time` is a timestamp in ISO 8601 format\.
+ The `type` property defines the event type\. The following table describes all possible values\.
+ The `record` property defines a JSON object that contains the telemetry data\. The schema of this JSON object depends on the `type`\.

The following table summarizes all types of `Event` objects, and links to the [Telemetry API `Event` schema reference](telemetry-schema-reference.md) for each event type\.


**Telemetry API message types**  

| Category | Event type | Description | Event record schema | 
| --- | --- | --- | --- | 
|  Platform event  |  `platform.initStart`  |  Function initialization started\.  |  [`platform.initStart`](telemetry-schema-reference.md#platform-initStart) schema  | 
|  Platform event  |  `platform.initRuntimeDone`  |  Function initialization completed\.  |  [`platform.initRuntimeDone`](telemetry-schema-reference.md#platform-initRuntimeDone) schema  | 
|  Platform event  |  `platform.initReport`  |  A report of function initialization\.  |  [`platform.initReport`](telemetry-schema-reference.md#platform-initReport) schema  | 
|  Platform event  |  `platform.start`  |  Function invocation started\.  |  [`platform.start`](telemetry-schema-reference.md#platform-start) schema  | 
|  Platform event  |  `platform.runtimeDone`  |  The runtime finished processing an event with either success or failure\.  |  [`platform.runtimeDone`](telemetry-schema-reference.md#platform-runtimeDone) schema  | 
|  Platform event  |  `platform.report`  |  A report of function invocation\.  |  [`platform.report`](telemetry-schema-reference.md#platform-report) schema  | 
|  Platform event  |  `platform.telemetrySubscription`  |  The extension subscribed to the Telemetry API\.  |  [`platform.telemetrySubscription`](telemetry-schema-reference.md#platform-telemetrySubscription) schema  | 
|  Platform event  |  `platform.logsDropped`  |  Lambda dropped log entries\.  |  [`platform.logsDropped`](telemetry-schema-reference.md#platform-logsDropped) schema  | 
|  Function logs  |  `function`  |  A log line from function code\.  |  [`function`](telemetry-schema-reference.md#telemetry-api-function) schema  | 
|  Extension logs  |  `extension`  |  A log line from extension code\.  |  [`extension`](telemetry-schema-reference.md#telemetry-api-extension) schema  | 