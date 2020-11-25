# AWS Lambda Logs API<a name="runtimes-logs-api"></a>

Lambda automatically captures runtime logs and streams them to Amazon CloudWatch\. This log stream contains the logs that your function code and extensions generate, and also the logs that Lambda generates as part of the function invocation\.

[Lambda extensions](runtimes-extensions-api.md) can use the Lambda Runtime Logs API to subscribe to log streams directly from within the Lambda [execution environment](runtimes-context.md)\. Lambda streams the logs to the extension, and the extension can then process, filter, and send the logs to any preferred destination\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/logs-api-concept-diagram.png)

The Logs API allows extensions to subscribe to three different logs streams:
+ Function logs that the Lambda function generates and writes to `stdout` or `stderr`\.
+ Lambda platform logs, such as the START, END, and REPORT logs\.
+ Extension logs that extension code generates\.

**Note**  
Lambda sends all logs to CloudWatch, even when an extension subscribes to one or more of the log streams\.

**Topics**
+ [Subscribing to receive logs](#runtimes-logs-api-subscribing)
+ [Destination protocols](#runtimes-logs-api-dest)
+ [Buffering configuration](#runtimes-logs-api-buffering)
+ [Example subscription](#runtimes-logs-api-subs-example)
+ [Example log messages](#runtimes-logs-api-examples)
+ [Sample code for Logs API](#runtimes-logs-api-samples)
+ [Logs API reference](#runtimes-logs-api-ref)

## Subscribing to receive logs<a name="runtimes-logs-api-subscribing"></a>

A Lambda extension can subscribe to receive logs by sending a subscription request to the Logs API\.

To subscribe to receive logs, you need the extension identifier \(`Lambda-Extension-Identifier`\)\. First [register the extension](runtimes-extensions-api.md#extensions-registration-api-a) to receive the extension identifier\. Then subscribe to the Logs API during [initialization](runtimes-context.md#runtimes-lifecycle-ib)\. After the initialization phase is complete, Lambda does not process subscription requests\.

**Note**  
Logs API subscription is idempotent\. Duplicate subscribe requests do not result in duplicate subscriptions\.

Memory usage increases linearly as the number of subscribers increases\. Subscriptions consume memory resources because each subscription opens a new memory buffer to store the logs\. To help optimize memory usage, you can adjust the [buffering configuration](#runtimes-logs-api-buffering)\. Buffer memory usage counts towards overall memory consumption in the execution environment\.

## Destination protocols<a name="runtimes-logs-api-dest"></a>

You can choose one of the following protocols to receive the logs:

1. **HTTP** \(recommended\) – Logs are delivered to a local HTTP endpoint \(`http://sandbox:${PORT}/${PATH}`\) as an array of records in JSON format\. The $PATH parameter is optional\. Note that only HTTP is supported, not HTTPS\. You can choose to receive logs through PUT or POST\.

1. **TCP** – Logs are delivered to a TCP port in [Newline delimited JSON \(NDJSON\) format](https://github.com/ndjson/ndjson-spec)\.

We recommend using HTTP rather than TCP\. With TCP, the Lambda platform cannot acknowledge that logs are delivered to the application layer\. Therefore, you may lose logs if your extension crashes\. HTTP does not share this limitation\. Also, in a TCP connection, you can unintentionally partition the NDJSON record incorrectly and corrupt the format\. If you choose to use TCP, wait until a newline arrives before flushing a record\.

We also recommend setting up the local HTTP listener or the TCP port before subscribing to receive logs\. During setup, note the following:
+ Lambda sends logs only to destinations that are inside the execution environment\.
+ Lambda retries the attempt to send the logs \(with backoff\) if there is no listener, or if the POST or PUT request results in error\. If the log subscriber crashes, it will continue to receive logs after Lambda restarts the execution environment\.
+ Lambda reserves port 9001\. There are no other port number restrictions or recommendations\.

## Buffering configuration<a name="runtimes-logs-api-buffering"></a>

Lambda can buffer logs and deliver them to the subscriber\. You can configure this behavior in the subscription request by specifying the following optional fields\. Lambda uses the default value for any field that you do not specify:
+ **timeoutMs** – The maximum time \(in milliseconds\) to buffer a batch\. Default: 1000\. Minimum: 100\. Maximum: 30000\.
+ **maxBytes** – The maximum size \(in bytes\) of the logs to buffer in memory\. Default: 262144\. Minimum: 262144\. Maximum: 1048576\.
+ **maxItems** – The maximum number of events to buffer in memory\. Default: 10000\. Minimum: 1000\. Maximum: 10000\.

During buffering configuration, note the following points:
+ Lambda flushes the logs if any of the input streams are closed, for example, if the runtime crashes\.
+ Each subscriber can specify a different buffering configuration during the subscription request\.
+ Consider the buffer size that you need for reading the data\. Expect to receive payloads as large as `2*maxBytes+metadata`, where `maxBytes` is configured in the subscribe request\. For example, Lambda adds the following metadata bytes to each record:

  ```
  {
  "time": "2020-08-20T12:31:32.123Z",
  "type": "function",
  "record": "Hello World"
  }
  ```
+ If the subscriber cannot process incoming logs quickly enough, Lambda might drop logs to keep memory utilization bounded\. To indicate the number of dropped records, Lambda injects a `platform.logsDropped` record\.

## Example subscription<a name="runtimes-logs-api-subs-example"></a>

The following example shows a request to subscribe to the platform and function logs\.

```
PUT http://${AWS_LAMBDA_RUNTIME_API}/2020-08-15/logs/ HTTP/1.1
{"types": [
    "platform",
    "function"
  ],
  "buffering": {
    "maxItems": 1000,
    "maxBytes": 10240,
    "timeoutMs": 100
  }
  "destination": {
    "protocol": "HTTP",
    "URI": "http://sandbox:8080/lambda_logs"  
  }
}
```

If the request succeeds, the subscriber receives an HTTP 200 success response\.

```
HTTP/1.1 200 OK
"OK"
```

## Example log messages<a name="runtimes-logs-api-examples"></a>

The following example shows platform start, end, and report logs\.

```
 {
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.start",
    "record": {
        "requestId": "6f7f0961f83442118a7af6fe80b88d56"
     }
}
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.end",
    "record": {
        "requestId": "6f7f0961f83442118a7af6fe80b88d56"
     }
}
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.report",
    "record": {
        "requestId": "6f7f0961f83442118a7af6fe80b88d56",
        "metrics": {
            "durationMs": 101.51,
            "billedDurationMs": 300,
            "memorySizeMB": 512,
            "maxMemoryUsedMB": 33,
            "initDurationMs": 116.67
        }
    }
}
```

The platform log captures runtime or execution environment errors\. The following example shows a platform fault log message\.

```
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.fault",
    "record": "RequestId: d783b35e-a91d-4251-af17-035953428a2c Process exited before completing request"
}
```

The following example shows an Extensions API registration log message\.

```
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.extension",
    "record": {
        "name": "Foo.bar",
        "state": "Ready",
        "events": ["INVOKE", "SHUTDOWN"]
     }
}
```

The following example shows a Logs API subscription log message\.

```
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.logsSubscription",
    "record": {
        "name": "Foo.bar",
        "state": "Subscribed",
        "types": ["function", "platform"],
    }
}
```

The following example shows a `platform.logsDropped` log message\.

```
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "platform.logsDropped",
    "record": {
        "reason": "Consumer seems to have fallen behind as it has not acknowledged receipt of logs.",
        "droppedRecords": 123,
        "droppedBytes" 12345
    }
}
```

The following example shows a function log message\.

```
{
    "time": "2020-08-20T12:31:32.123Z",
    "type": "function",
    "record": "ERROR something happened. Stack trace:\n\tfoo (line 10)\n"
}
```

## Sample code for Logs API<a name="runtimes-logs-api-samples"></a>

The [ compute blog post entry](https://aws.amazon.com/blogs/compute/using-aws-lambda-extensions-to-send-logs-to-custom-destinations) for Logs API includes sample code showing how to send logs to a custom destination\.

Lambda provides [Python and Go code examples](https://github.com/aws-samples/aws-lambda-extensions) showing how to develop a basic extension and subscribe to the Logs API\. For more information about building a Lambda extension, see [AWS Lambda Extensions API](runtimes-extensions-api.md)\.

## Logs API reference<a name="runtimes-logs-api-ref"></a>

You can retrieve the Logs API endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable\. To send an API request, use the prefix `2020-08-15/` before the API path\. For example:

```
http://${AWS_LAMBDA_RUNTIME_API}/2020-08-15/logs/
```

### Subscribe<a name="runtimes-logs-api-ref-a"></a>

To subscribe to one or more of the log streams that are available in the Lambda execution environment, extensions send a Subscribe API request\.

The OpenAPI specification for the Logs API subscription request, version **2020\-08\-15**, is available here: [logs\-api\-request\.zip](samples/logs-api-request.zip)

**Path** – `/logs`

**Method** – **PUT**

**Body parameters**

`destination` – See [Destination protocols](#runtimes-logs-api-dest)\. Required: yes\. Type: strings\.

`buffering` – See [Buffering configuration](#runtimes-logs-api-buffering)\. Required: no\. Type: strings\.

`types` – An array of the types of logs to receive\. Required: yes\. Type: array of strings\. Valid values: "platform", "function", "extension"\.

**Response parameters**

The OpenAPI specifications for the subscription responses, version **2020\-08\-15**, are available for HTTP and TCP:
+ HTTP: [logs\-api\-http\-response\.zip](samples/logs-api-http-response.zip)
+ TCP: [logs\-api\-tcp\-response\.zip](samples/logs-api-tcp-response.zip)

**Response codes**
+ 200 – Request completed successfully
+ 202 – Request accepted\. Response to a subscription request during local testing\.
+ 4XX – Bad Request
+ 500 – Service error

**Example subscription request**  

```
 PUT http://${AWS_LAMBDA_RUNTIME_API}/2020-08-15/logs/ HTTP/1.1
 {
  "types": [
    "platform",
    "function"
  ],
  "buffering": {
    "maxItems": 1000,
    "maxBytes": 10240,
    "timeoutMs": 100
  }
  "destination": {
    "protocol": "HTTP",
    "URI": "http://sandbox:8080/lambda_logs"  
  }
}
```

If the request succeeds, the subscriber receives an HTTP 200 success response\.

```
HTTP/1.1 200 OK
"OK"
```

If the request fails, the subscriber receives an error response\.

```
HTTP/1.1 400 OK
{
    "errorType": "Logs.ValidationError",
    "errorMessage": URI port is not provided; types should not be empty"
}
```