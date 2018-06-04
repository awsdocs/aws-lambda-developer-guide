# Programming Model\(Node\.js\)<a name="programming-model"></a>

AWS Lambda currently supports the following Node\.js runtimes:
+ Node\.js runtime v8\.10 \(runtime = nodejs8\.10\) 
+ Node\.js runtime v6\.10 \(runtime = nodejs6\.10\) 
+ Node\.js runtime v4\.3 \(runtime = nodejs4\.3\)\* 
+ Node\.js runtime v0\.10\.42 \(runtime = nodejs\)\* 
**Important**  
\*Node v0\.10\.42 and Node v4\.3 are currently marked as deprecated\. For more information, see [Runtime Support Policy](runtime-support-policy.md)\. You must migrate existing functions to the newer Node\.js runtime versions available on AWS Lambda \(nodejs\.8\.10 or nodejs6\.10\) as soon as possible\. 

When you create a Lambda function, you specify the runtime that you want to use\. For more information, see `runtime` parameter of [CreateFunction](API_CreateFunction.md)\. 

The following sections explain how [common programming patterns and core concepts](http://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in Node\.js\. The programming model described in the following sections applies to all supported runtime versions, except where indicated\. 

**Topics**
+ [Lambda Function Handler \(Node\.js\)](nodejs-prog-model-handler.md)
+ [The Context Object \(Node\.js\)](nodejs-prog-model-context.md)
+ [Logging \(Node\.js\)](nodejs-prog-model-logging.md)
+ [Function Errors \(Node\.js\)](nodejs-prog-mode-exceptions.md)