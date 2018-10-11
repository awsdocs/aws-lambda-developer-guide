# Using AWS Lambda with Amazon API Gateway \(On\-Demand Over HTTPS\)<a name="with-on-demand-https"></a>

You can invoke AWS Lambda functions over HTTPS\. You can do this by defining a custom REST API and endpoint using [Amazon API Gateway](https://aws.amazon.com/api-gateway/), and then mapping individual methods, such as `GET` and `PUT`, to specific Lambda functions\. Alternatively, you could add a special method named ANY to map all supported methods \(`GET`, `POST`, `PATCH`, `DELETE`\) to your Lambda function\. When you send an HTTPS request to the API endpoint, the Amazon API Gateway service invokes the corresponding Lambda function\. For more information about the `ANY` method, see [Create a Simple Microservice using Lambda and API Gateway](with-on-demand-https-example-configure-event-source_1.md)\.

 Amazon API Gateway also adds a layer between your application users and your app logic that enables the following: 
+ Ability to throttle individual users or requests\. 
+ Protect against Distributed Denial of Service attacks\.
+ Provide a caching layer to cache response from your Lambda function\. 

Note the following about how the Amazon API Gateway and AWS Lambda integration works:
+ **Push\-event model** – This is a model \(see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\), where Amazon API Gateway invokes the Lambda function by passing data in the request body as parameter to the Lambda function\. 
+ **Synchronous invocation** – The Amazon API Gateway can invoke the Lambda function and get a response back in real time by specifying `RequestResponse` as the invocation type\. For information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is the body from the HTTPS request that Amazon API Gateway receives and your Lambda function is the custom code written to process the specific event type\. 

Note that there are two types of permissions policies that you work with when you set up the end\-to\-end experience:
+ **Permissions for your Lambda function** – Regardless of what invokes a Lambda function, AWS Lambda executes the function by assuming the IAM role \(execution role\) that you specify at the time you create the Lambda function\. Using the permissions policy associated with this role, you grant your Lambda function the permissions that it needs\. For example, if your Lambda function needs to read an object, you grant permissions for the relevant Amazon S3 actions in the permissions policy\. For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.
+ **Permission for Amazon API Gateway to invoke your Lambda function** – Amazon API Gateway cannot invoke your Lambda function without your permission\. You grant this permission via the permission policy associated with the Lambda function\.

For a tutorial that walks you through an example setup, see [Using AWS Lambda with Amazon API Gateway \(On\-Demand Over HTTPS\)](with-on-demand-https-example.md)\.