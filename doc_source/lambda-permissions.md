# AWS Lambda permissions<a name="lambda-permissions"></a>

You can use AWS Identity and Access Management \(IAM\) to manage access to the Lambda API and resources like functions and layers\. For users and applications in your account that use Lambda, you manage permissions in a permissions policy that you can apply to IAM users, groups, or roles\. To grant permissions to other accounts or AWS services that use your Lambda resources, you use a policy that applies to the resource itself\.

A Lambda function also has a policy, called an [execution role](lambda-intro-execution-role.md), that grants it permission to access AWS services and resources\. At a minimum, your function needs access to Amazon CloudWatch Logs for log streaming\. If you [use AWS X\-Ray to trace your function](services-xray.md), or your function accesses services with the AWS SDK, you grant it permission to call them in the execution role\. Lambda also uses the execution role to get permission to read from event sources when you use an [event source mapping](invocation-eventsourcemapping.md) to trigger your function\.

**Note**  
If your function needs network access to a resource like a relational database that isn't accessible through AWS APIs or the internet, [configure it to connect to your VPC](configuration-vpc.md)\.

Use [resource\-based policies](access-control-resource-based.md) to give other accounts and AWS services permission to use your Lambda resources\. Lambda resources include functions, versions, aliases, and layer versions\. Each of these resources has a permissions policy that applies when the resource is accessed, in addition to any policies that apply to the user\. When an AWS service like Amazon S3 calls your Lambda function, the resource\-based policy gives it access\.

To manage permissions for users and applications in your accounts, [use the managed policies that Lambda provides](access-control-identity-based.md), or write your own\. The Lambda console uses multiple services to get information about your function's configuration and triggers\. You can use the managed policies as\-is, or as a starting point for more restrictive policies\.

You can restrict user permissions by the resource an action affects and, in some cases, by additional conditions\. For example, you can specify a pattern for the Amazon Resource Name \(ARN\) of a function that requires a user to include their user name in the name of functions that they create\. Additionally, you can add a condition that requires that the user configure functions to use a specific layer to, for example, pull in logging software\. For the resources and conditions that are supported by each action, see [Resources and Conditions](lambda-api-permissions-ref.md)\.

For more information about IAM, see [What is IAM?](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html) in the *IAM User Guide*\.

**Topics**
+ [AWS Lambda execution role](lambda-intro-execution-role.md)
+ [Using resource\-based policies for AWS Lambda](access-control-resource-based.md)
+ [Identity\-based IAM policies for AWS Lambda](access-control-identity-based.md)
+ [Resources and conditions for Lambda actions](lambda-api-permissions-ref.md)
+ [Using permissions boundaries for AWS Lambda applications](permissions-boundary.md)