# Converting Lambda Telemetry API `Event` objects to OpenTelemetry Spans<a name="telemetry-otel-spans"></a>

The AWS Lambda Telemetry API schema is semantically compatible with OpenTelemetry \(OTel\)\. This means that you can convert your AWS Lambda Telemetry API `Event` objects to OpenTelemetry \(OTel\) Spans\. When converting, you shouldn't map a single `Event` object to a single OTel Span\. Instead, you should present all three events related to a lifecycle phase in a single OTel Span\. For example, the `start`, `runtimeDone`, and `runtimeReport` events represent a single function invocation\. Present all three of these events as a single OTel Span\.

You can convert your events using Span Events or Child \(nested\) Spans\. The tables on this page describe the mappings between Telemetry API schema properties and OTel Span properties for both approaches\. For more information about OTel Spans, see [Span](https://opentelemetry.io/docs/reference/specification/trace/api/#span) on the **Tracing API** page of the OpenTelemetry Docs website\.

**Topics**
+ [Map to OTel Spans with Span Events](#telemetry-otel-span-events)
+ [Map to OTel Spans with Child Spans](#telemetry-otel-child-spans)

## Map to OTel Spans with Span Events<a name="telemetry-otel-span-events"></a>

In the following tables, `e` represents the event coming from the telemetry source\.


**Mapping the `*Start` events**  

| OpenTelemetry | Lambda Telemetry API schema | 
| --- | --- | 
|  `Span.Name`  |  Your extension generates this value based on the `type` field\.  | 
|  `Span.StartTime`  |  Use `e.time`\.  | 
|  `Span.EndTime`  |  N/A, because the event hasn't completed yet\.  | 
|  `Span.Kind`  |  Set to `Server`\.  | 
|  `Span.Status`  |  Set to `Unset`\.  | 
|  `Span.TraceId`  |  Parse the AWS X\-Ray header found in `e.tracing.value`, then use the `TraceId` value\.  | 
|  `Span.ParentId`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Parent` value\.  | 
|  `Span.SpanId`  |  Use `e.tracing.spanId` if available\. Otherwise, generate a new `SpanId`\.  | 
|  `Span.SpanContext.TraceState`  |  N/A for an X\-Ray trace context\.  | 
|  `Span.SpanContext.TraceFlags`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Sampled` value\.  | 
|  `Span.Attributes`  |  Your extension can add any custom values here\.  | 


**Mapping the `*RuntimeDone` events**  

| OpenTelemetry | Lambda Telemetry API schema | 
| --- | --- | 
|  `Span.Name`  |  Your extension generates the value based on the `type` field\.  | 
|  `Span.StartTime`  |  Use `e.time` from the matching `*Start` event\. Alternatively, use `e.time - e.metrics.durationMs`\.  | 
|  `Span.EndTime`  |  N/A, because the event hasn't completed yet\.  | 
|  `Span.Kind`  |  Set to `Server`\.  | 
|  `Span.Status`  |  If `e.status` doesn't equal `success`, then set to `Error`\. Otherwise, set to `Ok`\.  | 
|  `Span.Events[]`  |  Use `e.spans[]`\.  | 
|  `Span.Events[i].Name`  |  Use `e.spans[i].name`\.  | 
|  `Span.Events[i].Time`  |  Use `e.spans[i].start`\.  | 
|  `Span.TraceId`  |  Parse the AWS X\-Ray header found in `e.tracing.value`, then use the `TraceId` value\.  | 
|  `Span.ParentId`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Parent` value\.  | 
|  `Span.SpanId`  |  Use the same `SpanId` from the `*Start` event\. If unavailable, then use `e.tracing.spanId`, or generate a new `SpanId`\.  | 
|  `Span.SpanContext.TraceState`  |  N/A for an X\-Ray trace context\.  | 
|  `Span.SpanContext.TraceFlags`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Sampled` value\.  | 
|  `Span.Attributes`  |  Your extension can add any custom values here\.  | 


**Mapping the `*Report` events**  

| OpenTelemetry | Lambda Telemetry API schema | 
| --- | --- | 
|  `Span.Name`  |  Your extension generates the value based on the `type` field\.  | 
|  `Span.StartTime`  |  Use `e.time` from the matching `*Start` event\. Alternatively, use `e.time - e.metrics.durationMs`\.  | 
|  `Span.EndTime`  |  Use `e.time`\.  | 
|  `Span.Kind`  |  Set to `Server`\.  | 
|  `Span.Status`  |  Use the same value as the `*RuntimeDone` event\.  | 
|  `Span.TraceId`  |  Parse the AWS X\-Ray header found in `e.tracing.value`, then use the `TraceId` value\.  | 
|  `Span.ParentId`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Parent` value\.  | 
|  `Span.SpanId`  |  Use the same `SpanId` from the `*Start` event\. If unavailable, then use `e.tracing.spanId`, or generate a new `SpanId`\.  | 
|  `Span.SpanContext.TraceState`  |  N/A for an X\-Ray trace context\.  | 
|  `Span.SpanContext.TraceFlags`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Sampled` value\.  | 
|  `Span.Attributes`  |  Your extension can add any custom values here\.  | 

## Map to OTel Spans with Child Spans<a name="telemetry-otel-child-spans"></a>

The following table describes how to convert Lambda Telemetry API events into OTel Spans with Child \(nested\) Spans for `*RuntimeDone` Spans\. For `*Start` and `*Report` mappings, refer to the tables in [Map to OTel Spans with Span Events](#telemetry-otel-span-events), as they're the same for Child Spans\. In this table, `e` represents the event coming from the telemetry source\.


**Mapping the `*RuntimeDone` events**  

| OpenTelemetry | Lambda Telemetry API schema | 
| --- | --- | 
|  `Span.Name`  |  Your extension generates the value based on the `type` field\.  | 
|  `Span.StartTime`  |  Use `e.time` from the matching `*Start` event\. Alternatively, use `e.time - e.metrics.durationMs`\.  | 
|  `Span.EndTime`  |  N/A, because the event hasn't completed yet\.  | 
|  `Span.Kind`  |  Set to `Server`\.  | 
|  `Span.Status`  |  If `e.status` doesn't equal `success`, then set to `Error`\. Otherwise, set to `Ok`\.  | 
|  `Span.TraceId`  |  Parse the AWS X\-Ray header found in `e.tracing.value`, then use the `TraceId` value\.  | 
|  `Span.ParentId`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Parent` value\.  | 
|  `Span.SpanId`  |  Use the same `SpanId` from the `*Start` event\. If unavailable, then use `e.tracing.spanId`, or generate a new `SpanId`\.  | 
|  `Span.SpanContext.TraceState`  |  N/A for an X\-Ray trace context\.  | 
|  `Span.SpanContext.TraceFlags`  |  Parse the X\-Ray header found in `e.tracing.value`, then use the `Sampled` value\.  | 
|  `Span.Attributes`  |  Your extension can add any custom values here\.  | 
|  `ChildSpan[i].Name`  |  Use `e.spans[i].name`\.  | 
|  `ChildSpan[i].StartTime`  |  Use `e.spans[i].start`\.  | 
|  `ChildSpan[i].EndTime`  |  Use `e.spans[i].start + e.spans[i].durations`\.  | 
|  `ChildSpan[i].Kind`  |  Same as parent `Span.Kind`\.  | 
|  `ChildSpan[i].Status`  |  Same as parent `Span.Status`\.  | 
|  `ChildSpan[i].TraceId`  |  Same as parent `Span.TraceId`\.  | 
|  `ChildSpan[i].ParentId`  |  Use parent `Span.SpanId`\.  | 
|  `ChildSpan[i].SpanId`  |  Generate a new `SpanId`\.  | 
|  `ChildSpan[i].SpanContext.TraceState`  |  N/A for an X\-Ray trace context\.  | 
|  `ChildSpan[i].SpanContext.TraceFlags`  |  Same as parent `Span.SpanContext.TraceFlags`\.  | 