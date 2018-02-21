# Programming Model\(Node\.js\)<a name="programming-model"></a>

AWS Lambda currently supports the following Node\.js runtimes:

+ Node\.js runtime v6\.10 \(runtime = nodejs6\.10\) 

+ Node\.js runtime v4\.3 \(runtime = nodejs4\.3\) 

+ Node\.js runtime v0\.10\.42 \(runtime = nodejs\) 
**Important**  
Node v0\.10\.42 is currently marked as deprecated\. For more information, see [Runtime Support Policy](runtime-support-policy.md)\. You must migrate existing functions to the newer Node\.js runtime versions available on AWS Lambda \(nodejs4\.3 or nodejs6\.10\) as soon as possible\. Note that you will have to follow this procedure for each region that contains functions written in the Node v0\.10\.42 runtime\. For information about programming model differences in the v0\.10\.42 runtime, see [Using the Earlier Node\.js Runtime v0\.10\.42](nodejs-prog-model-using-old-runtime.md)\. 

When you create a Lambda function, you specify the runtime that you want to use\. For more information, see `runtime` parameter of [CreateFunction](API_CreateFunction.md)\. 

The following sections explain how [common programming patterns and core concepts](http://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in Node\.js\. The programming model described in the following sections apply to both versions, except where indicated\. 


+ [Lambda Function Handler \(Node\.js\)](nodejs-prog-model-handler.md)
+ [The Context Object \(Node\.js\)](nodejs-prog-model-context.md)
+ [Logging \(Node\.js\)](nodejs-prog-model-logging.md)
+ [Function Errors \(Node\.js\)](nodejs-prog-mode-exceptions.md)
+ [Using the Earlier Node\.js Runtime v0\.10\.42](nodejs-prog-model-using-old-runtime.md)