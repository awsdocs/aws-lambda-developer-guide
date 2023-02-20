# Lambda Telemetry API reference<a name="telemetry-api-reference"></a>

Use the Lambda Telemetry API endpoint to subscribe extensions to telemetry streams\. You can retrieve the Telemetry API endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable\. To send an API request, append the API version \(`2022-07-01/`\) and `telemetry/`\. For example:

```
http://${AWS_LAMBDA_RUNTIME_API}/2022-07-01/telemetry/
```

**Note**  
We will update this section with the OpenAPI Specification \(OAS\) definition of the latest version of the Telemetry API for HTTP and TCP protocols in the coming weeks\.

**Topics**
+ [Subscribe](#telemetry-subscribe-api)

## Subscribe<a name="telemetry-subscribe-api"></a>

To subscribe to a telemetry stream, a Lambda extension can send a Subscribe API request\.
+ **Path** – `/telemetry`
+ **Method** – `PUT`
+ **Headers**
  + `Content-Type`: `application/json`
+ **Request body parameters**
  + **schemaVersion**
    + Required: Yes
    + Type: String
    + Valid values: `"2022-07-01"`
  + **destination** – The configuration settings that define the telemetry event destination and the protocol for event delivery\.
    + Required: Yes
    + Type: Object

      ```
      {
          "protocol": "HTTP",
          "URI": "http://sandbox.localdomain:8080"
      }
      ```
    + **protocol** – The protocol that Lambda uses to send telemetry data\.
      + Required: Yes
      + Type: String
      + Valid values: `"HTTP"`\|`"TCP"`
    + **URI** – The URI to send telemetry data to\.
      + Required: Yes
      + Type: String
    + For more information, see [Specifying a destination protocol](telemetry-api.md#telemetry-api-destination)\.
  + **types** – The types of telemetry that you want the extension to subscribe to\.
    + Required: Yes
    + Type: Array of strings
    + Valid values: `"platform"`\|`"function"`\|`"extension"`
  + **buffering** – The configuration settings for event buffering\.
    + Required: No
    + Type: Object

      ```
      {
         "buffering": {
              "maxItems": 1000,
              "maxBytes": 256*1024,
              "timeoutMs": 100
         }
      }
      ```
    + **maxItems** – The maximum number of events to buffer in memory\.
      + Required: No
      + Type: Integer
      + Default: 1,000
      + Minimum: 25
      + Maximum: 30,000
    + **maxBytes** – The maximum volume of telemetry \(in bytes\) to buffer in memory\.
      + Required: No
      + Type: Integer
      + Default: 262,144
      + Minimum: 262,144
      + Maximum: 1,048,576
    + **timeoutMs** – The maximum time \(in milliseconds\) to buffer a batch\.
      + Required: No
      + Type: Integer
      + Default: 10,000
      + Minimum: 1,000
      + Maximum: 10,000
    + For more information, see [Configuring memory usage and buffering](telemetry-api.md#telemetry-api-buffering)\.

### Example Subscribe API request<a name="telemetry-subscribe-api-example"></a>

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

If the Subscribe request succeeds, the extension receives an HTTP 200 success response:

```
HTTP/1.1 200 OK
"OK"
```

If the Subscribe request fails, the extension receives an error response\. For example:

```
HTTP/1.1 400 OK
{
    "errorType": "ValidationError",
    "errorMessage": "URI port is not provided; types should not be empty"
}
```

Here are some additional response codes that the extension can receive:
+ 200 – Request completed successfully
+ 202 – Request accepted\. Subscription request response in local testing environment
+ 400 – Bad request
+ 500 – Service error