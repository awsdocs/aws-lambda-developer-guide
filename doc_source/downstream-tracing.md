# Emitting Trace Segments from a Lambda Function<a name="downstream-tracing"></a>

For each traced invocation, Lambda will emit the Lambda service segment and all of its subsegments\. In addition, Lambda will emit the Lambda function segment and the init subsegment\. These segments will be emitted regardless of the function's runtime, and with no code changes or additional libraries required\. If you want your Lambda function's X\-Ray traces to include custom segments, annotations, or subsegments for downstream calls, you might need to include additional libraries and annotate your code\. 

Note that any instrumentation code must be implemented inside the Lambda function handler and not as part of the initialization code\. 

The following examples explain how to do this in the supported runtimes:

**Topics**
+ [Node\.js](nodejs-tracing.md)
+ [Java](java-tracing.md)
+ [Python](python-tracing.md)
+ [Go](go-tracing.md)