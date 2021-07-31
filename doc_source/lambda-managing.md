# Managing AWS Lambda functions<a name="lambda-managing"></a>

Concurrency is the number of instances of your function that are active\. Scaling is how quickly Lambda increases the the number of instances\.

Depending on who invokes your function and how it's invoked, scaling behavior and the types of errors that occur can vary\. When you invoke a function synchronously, you receive errors in the response and can retry\. When you invoke asynchronously, use an event source mapping, or configure another service to invoke your function, the retry requirements and the way that your function scales to handle large numbers of events can vary\. For details, see [AWS Lambda function scaling](invocation-scaling.md) and [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.



As you add libraries and other dependencies to your function, creating and uploading a deployment package can slow down development\. Use [layers](configuration-layers.md) to manage your function's dependencies independently and keep your deployment package small\. You can also use layers to share your own libraries with other customers and use publicly available layers with your functions\.

To use your Lambda function with AWS resources in an Amazon VPC, configure it with security groups and subnets to [create a VPC connection](configuration-vpc.md)\. Connecting your function to a VPC lets you access resources in a private subnet such as relational databases and caches\. You can also [create a database proxy](configuration-database.md) for MySQL and Aurora DB instances\. A database proxy enables a function to reach high concurrency levels without exhausting database connections\.

To use [code signing](configuration-codesigning.md) with your Lambda function, configure it with a code\-signing configuration\. When a user attempts to deploy a code package, Lambda checks that the code package has a valid signature from a trusted publisher\. The code\-signing configuration includes a set of signing profiles, which define the trusted publishers for this function\.