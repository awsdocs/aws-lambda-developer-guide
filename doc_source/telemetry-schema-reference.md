# Lambda Telemetry API `Event` schema reference<a name="telemetry-schema-reference"></a>

Use the Lambda Telemetry API endpoint to subscribe extensions to telemetry streams\. You can retrieve the Telemetry API endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable\. To send an API request, append the API version \(`2022-07-01/`\) and `telemetry/`\. For example:

```
http://${AWS_LAMBDA_RUNTIME_API}/2022-07-01/telemetry/
```

**Note**  
We will update this section with the OpenAPI Specification \(OAS\) definition of the latest version of the Telemetry API for HTTP and TCP protocols in the coming weeks\.

The following table is a summary of all the types of `Event` objects that the Telemetry API supports\.


**Telemetry API message types**  

| Category | Event type | Description | Event record schema | 
| --- | --- | --- | --- | 
|  Platform event  |  `platform.initStart`  |  Function initialization started\.  |  [`platform.initStart`](#platform-initStart) schema  | 
|  Platform event  |  `platform.initRuntimeDone`  |  Function initialization completed\.  |  [`platform.initRuntimeDone`](#platform-initRuntimeDone) schema  | 
|  Platform event  |  `platform.initReport`  |  A report of function initialization\.  |  [`platform.initReport`](#platform-initReport) schema  | 
|  Platform event  |  `platform.start`  |  Function invocation started\.  |  [`platform.start`](#platform-start) schema  | 
|  Platform event  |  `platform.runtimeDone`  |  The runtime finished processing an event with either success or failure\.  |  [`platform.runtimeDone`](#platform-runtimeDone) schema  | 
|  Platform event  |  `platform.report`  |  A report of function invocation\.  |  [`platform.report`](#platform-report) schema  | 
|  Platform event  |  `platform.telemetrySubscription`  |  The extension subscribed to the Telemetry API\.  |  [`platform.telemetrySubscription`](#platform-telemetrySubscription) schema  | 
|  Platform event  |  `platform.logsDropped`  |  Lambda dropped log entries\.  |  [`platform.logsDropped`](#platform-logsDropped) schema  | 
|  Function logs  |  `function`  |  A log line from function code\.  |  [`function`](#telemetry-api-function) schema  | 
|  Extension logs  |  `extension`  |  A log line from extension code\.  |  [`extension`](#telemetry-api-extension) schema  | 

**Contents**
+ [Telemetry API `Event` object types](#telemetry-api-events)
  + [`platform.initStart`](#platform-initStart)
  + [`platform.initRuntimeDone`](#platform-initRuntimeDone)
  + [`platform.initReport`](#platform-initReport)
  + [`platform.start`](#platform-start)
  + [`platform.runtimeDone`](#platform-runtimeDone)
  + [`platform.report`](#platform-report)
  + [`platform.extension`](#platform-extension)
  + [`platform.telemetrySubscription`](#platform-telemetrySubscription)
  + [`platform.logsDropped`](#platform-logsDropped)
  + [`function`](#telemetry-api-function)
  + [`extension`](#telemetry-api-extension)
+ [Shared object types](#telemetry-api-objects)
  + [`InitPhase`](#InitPhase)
  + [`InitReportMetrics`](#InitReportMetrics)
  + [`InitType`](#InitType)
  + [`ReportMetrics`](#ReportMetrics)
  + [`RuntimeDoneMetrics`](#RuntimeDoneMetrics)
  + [Span](#Span)
  + [`Status`](#Status)
  + [`TraceContext`](#TraceContext)
  + [`TracingType`](#TracingType)

## Telemetry API `Event` object types<a name="telemetry-api-events"></a>

This section details the types of `Event` objects that the Lambda Telemetry API supports\. In the event descriptions, a question mark \(`?`\) indicates that the attribute may not be present in the object\.

### `platform.initStart`<a name="platform-initStart"></a>

A `platform.initStart` event indicates that the function initialization phase has started\. A `platform.initStart` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.initStart
- record: PlatformInitStart
```

The `PlatformInitStart` object has the following attributes:
+ **initializationType** – ``InitType`` object
+ **phase** – ``InitPhase`` object
+ **runtimeArn?** – `String`
+ **runtimeVersion?** – `String`

The following is an example `Event` of type `platform.initStart`:

```
{
    "time": "2022-10-12T00:00:15.064Z",
    "type": "platform.initStart",
    "record": {
        "initializationType": "on-demand",
        "phase": "init",
        "runtimeVersion": "nodejs-14.v3",
        "runtimeVersionArn": "arn"
    }
}
```

### `platform.initRuntimeDone`<a name="platform-initRuntimeDone"></a>

A `platform.initRuntimeDone` event indicates that the function initialization phase has completed\. A `platform.initRuntimeDone` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.initRuntimeDone
- record: PlatformInitRuntimeDone
```

The `PlatformInitRuntimeDone` object has the following attributes:
+ **initializationType** – ``InitType`` object
+ **phase** – ``InitPhase`` object
+ **status** – ``Status`` object
+ **spans?** – List of [Span](#Span) objects

The following is an example `Event` of type `platform.initRuntimeDone`:

```
{
    "time": "2022-10-12T00:01:15.000Z",
    "type": "platform.initRuntimeDone",
    "record": {
        "initializationType": "on-demand"
        "status": "success",
        "spans": [
            {
                "name": "someTimeSpan",
                "start": "2022-06-02T12:02:33.913Z",
                "durationMs": 70.5
            }
        ]
    }
}
```

### `platform.initReport`<a name="platform-initReport"></a>

A `platform.initReport` event contains an overall report of the function initialization phase\. A `platform.initReport` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.initReport
- record: PlatformInitReport
```

The `PlatformInitReport` object has the following attributes:
+ **initializationType** – ``InitType`` object
+ **phase** – ``InitPhase`` object
+ **metrics** – ``InitReportMetrics`` object
+ **spans?** – List of [Span](#Span) objects

The following is an example `Event` of type `platform.initReport`:

```
{
    "time": "2022-10-12T00:01:15.000Z",
    "type": "platform.initReport",
    "record": {
        "initializationType": "on-demand",
        "phase": "init",
        "metrics": {
            "durationMs": 125.33
        },
        "spans": [
            {
                "name": "someTimeSpan",
                "start": "2022-06-02T12:02:33.913Z",
                "durationMs": 90.1
            }
        ]
    }
}
```

### `platform.start`<a name="platform-start"></a>

A `platform.start` event indicates that the function invocation phase has started\. A `platform.start` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.start
- record: PlatformStart
```

The `PlatformStart` object has the following attributes:
+ **requestId** – `String`
+ **version?** – `String`
+ **tracing?** – ``TraceContext``

The following is an example `Event` of type `platform.start`:

```
{
    "time": "2022-10-12T00:00:15.064Z",
    "type": "platform.initStart",
    "record": {
        "requestId": "6d68ca91-49c9-448d-89b8-7ca3e6dc66aa",
        "version": "$LATEST",
        "tracing": {
            "spanId": "54565fb41ac79632",
            "type": "X-Amzn-Trace-Id",
            "value": "Root=1-62e900b2-710d76f009d6e7785905449a;Parent=0efbd19962d95b05;Sampled=1"
        }
    }
}
```

### `platform.runtimeDone`<a name="platform-runtimeDone"></a>

A `platform.runtimeDone` event indicates that the function invocation phase has completed\. A `platform.runtimeDone` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.runtimeDone
- record: PlatformRuntimeDone
```

The `PlatformRuntimeDone` object has the following attributes:
+ **metrics?** – ``RuntimeDoneMetrics`` object
+ **requestId** – `String`
+ **status** – ``Status`` object
+ **spans?** – List of [Span](#Span) objects
+ **tracing?** – ``TraceContext`` object

The following is an example `Event` of type `platform.runtimeDone`:

```
{
    "time": "2022-10-12T00:01:15.000Z",
    "type": "platform.runtimeDone",
    "record": {
        "requestId": "6d68ca91-49c9-448d-89b8-7ca3e6dc66aa",
        "status": "success",
        "tracing": {
            "spanId": "54565fb41ac79632",
            "type": "X-Amzn-Trace-Id",
            "value": "Root=1-62e900b2-710d76f009d6e7785905449a;Parent=0efbd19962d95b05;Sampled=1"
        },
        "spans": [
            {
                "name": "someTimeSpan",
                "start": "2022-08-02T12:01:23:521Z",
                "durationMs": 80.0
            }
        ],
        "metrics": {
            "durationMs": 140.0,
            "producedBytes": 16
        }
    }
}
```

### `platform.report`<a name="platform-report"></a>

A `platform.report` event contains an overall report of the function initialization phase\. A `platform.report` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.report
- record: PlatformReport
```

The `PlatformReport` object has the following attributes:
+ **metrics** – ``ReportMetrics`` object
+ **requestId** – `String`
+ **spans?** – List of [Span](#Span) objects
+ **status** – ``Status`` object
+ **tracing?** – ``TraceContext`` object

The following is an example `Event` of type `platform.report`:

```
{
    "time": "2022-10-12T00:01:15.000Z",
    "type": "platform.report",
    "record": {
        "metrics": {
            "billedDurationMs": 694,
            "durationMs": 693.92,
            "initDurationMs": 397.68,
            "maxMemoryUsedMB": 84,
            "memorySizeMB": 128
        },
        "requestId": "6d68ca91-49c9-448d-89b8-7ca3e6dc66aa",
    }
}
```

### `platform.extension`<a name="platform-extension"></a>

An `extension` event contains logs from the extension code\. An `extension` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = extension
- record: {}
```

The `PlatformExtension` object has the following attributes:
+ **events** – List of `String`
+ **name** – `String`
+ **state** – `String`

The following is an example `Event` of type `platform.extension`:

```
{
    "time": "2022-10-12T00:02:15.000Z",
    "type": "platform.extension",
    "record": {
        "events": [ "INVOKE", "SHUTDOWN" ],
        "name": "my-telemetry-extension",
        "state": "Ready"
    }
}
```

### `platform.telemetrySubscription`<a name="platform-telemetrySubscription"></a>

A `platform.telemetrySubscription` event contains information about an extension subscription\. A `platform.telemetrySubscription` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.telemetrySubscription
- record: PlatformTelemetrySubscription
```

The `PlatformTelemetrySubscription` object has the following attributes:
+ **name** – `String`
+ **state** – `String`
+ **types** – List of `String`

The following is an example `Event` of type `platform.telemetrySubscription`:

```
{
    "time": "2022-10-12T00:02:35.000Z",
    "type": "platform.telemetrySubscription",
    "record": {
        "name": "my-telemetry-extension",
        "state": "Subscribed",
        "types": [ "platform", "function" ]
    }
}
```

### `platform.logsDropped`<a name="platform-logsDropped"></a>

A `platform.logsDropped` event contains information about dropped events\. Lambda emits the `platform.logsDropped` event when an extension can't process one or more events\. A `platform.logsDropped` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = platform.logsDropped
- record: PlatformLogsDropped
```

The `PlatformLogsDropped` object has the following attributes:
+ **droppedBytes** – `Integer`
+ **droppedRecords** – `Integer`
+ **reason** – `String`

The following is an example `Event` of type `platform.logsDropped`:

```
{
    "time": "2022-10-12T00:02:35.000Z",
    "type": "platform.logsDropped",
    "record": {
        "droppedBytes": 12345,
        "droppedRecords": 123,
        "reason": "Consumer seems to have fallen behind as it has not acknowledged receipt of logs."
    }
}
```

### `function`<a name="telemetry-api-function"></a>

A `function` event contains logs from the function code\. A `function` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = function
- record: {}
```

The following is an example `Event` of type `function`:

```
{
    "time": "2022-10-12T00:03:50.000Z",
    "type": "function",
    "record": "[INFO] Hello world, I am a function!"
}
```

### `extension`<a name="telemetry-api-extension"></a>

A `extension` event contains logs from the extension code\. A `extension` `Event` object has the following shape:

```
Event: Object
- time: String
- type: String = extension
- record: {}
```

The following is an example `Event` of type `extension`:

```
{
    "time": "2022-10-12T00:03:50.000Z",
    "type": "extension",
    "record": "[INFO] Hello world, I am an extension!"
}
```

## Shared object types<a name="telemetry-api-objects"></a>

This section details the types of shared objects that the Lambda Telemetry API supports\.

### `InitPhase`<a name="InitPhase"></a>

A string enum that describes the phase when the initialization step occurs\. In most cases, Lambda runs the function initialization code during the `init` phase\. However, in some error cases, Lambda may re\-run the function initialization code during the `invoke` phase\. \(This is called a *suppressed init*\.\)
+ **Type** – `String`
+ **Valid values** – `init`\|`invoke`

### `InitReportMetrics`<a name="InitReportMetrics"></a>

An object that contains metrics about an initialization phase\.
+ **Type** – `Object`

An `InitReportMetrics` object has the following shape:

```
InitReportMetrics: Object
- durationMs: Double
```

The following is an example `InitReportMetrics` object:

```
{
    "durationMs": 247.88
}
```

### `InitType`<a name="InitType"></a>

A string enum that describes how Lambda initialized the environment\.
+ **Type** – `String`
+ **Valid values** – `on-demand`\|`provisioned-concurrency`

### `ReportMetrics`<a name="ReportMetrics"></a>

An object that contains metrics about a completed phase\.
+ **Type** – `Object`

A `ReportMetrics` object has the following shape:

```
ReportMetrics: Object
- billedDurationMs: Integer
- durationMs: Double
- initDurationMs?: Double
- maxMemoryUsedMB: Integer
- memorySizeMB: Integer
- restoreDurationMs?: Double
```

The following is an example `ReportMetrics` object:

```
{
    "billedDurationMs": 694,
    "durationMs": 693.92,
    "initDurationMs": 397.68,
    "maxMemoryUsedMB": 84,
    "memorySizeMB": 128
}
```

### `RuntimeDoneMetrics`<a name="RuntimeDoneMetrics"></a>

An object that contains metrics about an invocation phase\.
+ **Type** – `Object`

A `RuntimeDoneMetrics` object has the following shape:

```
RuntimeDoneMetrics: Object
- durationMs: Double
- producedBytes?: Integer
```

The following is an example `RuntimeDoneMetrics` object:

```
{
    "durationMs": 200.0,
    "producedBytes": 15
}
```

### Span<a name="Span"></a>

An object that contains details about a span\. A span represents a unit of work or operation in a trace\. For more information about spans, see [Span](https://opentelemetry.io/docs/reference/specification/trace/api/#span) on the **Tracing API** page of the OpenTelemetry Docs website\.

Lambda supports the following two spans for the `platform.RuntimeDone` event:
+ The `responseLatency` span describes how long it took your Lambda function to start sending the response\.
+ The `responseDuration` span describes how long it took your Lambda function to finish sending the entire response\.

The following is an example `responseLatency` span object:

```
{
        "name": "responseLatency", 
        "start": "2022-08-02T12:01:23.521Z",
        "durationMs": 23.02
      }
```

### `Status`<a name="Status"></a>

An object that describes the status of an initialization or invocation phase\. If the status is either `failure` or `error`, then the `Status` object also contains an `errorType` field describing the error\.
+ **Type** – `Object`
+ **Valid status values** – `success`\|`failure`\|`error`

### `TraceContext`<a name="TraceContext"></a>

An object that describes the properties of a trace\.
+ **Type** – `Object`

A `TraceContext` object has the following shape:

```
TraceContext: Object
- spanId?: String
- type: TracingType enum
- value: String
```

The following is an example `TraceContext` object:

```
{
    "spanId": "073a49012f3c312e",
    "type": "X-Amzn-Trace-Id",
    "value": "Root=1-62e900b2-710d76f009d6e7785905449a;Parent=0efbd19962d95b05;Sampled=1"
}
```

### `TracingType`<a name="TracingType"></a>

A string enum that describes the type of tracing in a ``TraceContext`` object\.
+ **Type** – `String`
+ **Valid values** – `X-Amzn-Trace-Id`