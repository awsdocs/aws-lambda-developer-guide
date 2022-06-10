# Lambda programming model<a name="foundation-progmodel"></a>

Lambda provides a programming model that is common to all of the runtimes\. The programming model defines the interface between your code and the Lambda system\. You tell Lambda the entry point to your function by defining a *handler* in the function configuration\. The runtime passes in objects to the handler that contain the invocation *event* and the *context*, such as the function name and request ID\.

When the handler finishes processing the first event, the runtime sends it another\. The function's class stays in memory, so clients and variables that are declared outside of the handler method in *initialization code* can be reused\. To save processing time on subsequent events, create reusable resources like AWS SDK clients during initialization\. Once initialized, each instance of your function can process thousands of requests\.

When [AWS X\-Ray tracing](services-xray.md) is enabled, the runtime records separate subsegments for initialization and execution\.

Your function also has access to local storage in the `/tmp` directory\. Instances of your function that are serving requests remain active for a few hours before being recycled\.

The runtime captures logging output from your function and sends it to Amazon CloudWatch Logs\. In addition to logging your function's output, the runtime also logs entries when function invocation starts and ends\. This includes a report log with the request ID, billed duration, initialization duration, and other details\. If your function throws an error, the runtime returns that error to the invoker\.

**Note**  
Logging is subject to [CloudWatch Logs quotas](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/cloudwatch_limits_cwl.html)\. Log data can be lost due to throttling or, in some cases, when an instance of your function is stopped\.

For a hands\-on introduction to the programming model in your preferred programming language, see the following chapters\.
+ [Building Lambda functions with Node\.js](lambda-nodejs.md)
+ [Building Lambda functions with Python](lambda-python.md)
+ [Building Lambda functions with Ruby](lambda-ruby.md)
+ [Building Lambda functions with Java](lambda-java.md)
+ [Building Lambda functions with Go](lambda-golang.md)
+ [Building Lambda functions with C\#](lambda-csharp.md)
+ [Building Lambda functions with PowerShell](lambda-powershell.md)

Lambda scales your function by running additional instances of it as demand increases, and by stopping instances as demand decreases\. Unless noted otherwise, incoming requests might be processed out of order or concurrently\. Store your application's state in other services, and don't rely on instances of your function being long lived\. Use local storage and class\-level objects to increase performance, but keep to a minimum the size of your deployment package and the amount of data that you transfer onto the execution environment\.