# Building Lambda Functions with Node\.js<a name="programming-model"></a>

AWS Lambda supports the following Node\.js runtimes\.


**Node\.js Runtimes**  

| Name  | Identifier  | 
| --- | --- | 
|  Node\.js 8\.10  |  `nodejs8.10`  | 
|  Node\.js 6\.10  |  `nodejs6.10`  | 

When you create a Lambda function, you specify the runtime that you want to use\. For more information, see `runtime` parameter of [CreateFunction](API_CreateFunction.md)\. 

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in Node\.js\. The programming model described in the following sections applies to all supported runtime versions, except where indicated\. 

**Topics**
+ [AWS Lambda Deployment Package in Node\.js](nodejs-create-deployment-pkg.md)
+ [AWS Lambda Function Handler in Node\.js](nodejs-prog-model-handler.md)
+ [AWS Lambda Context Object in Node\.js](nodejs-prog-model-context.md)
+ [AWS Lambda Function Logging in Node\.js](nodejs-prog-model-logging.md)
+ [AWS Lambda Function Errors in Node\.js](nodejs-prog-mode-exceptions.md)
+ [Instrumenting Node\.js Code in AWS Lambda](nodejs-tracing.md)