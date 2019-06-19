# AWS Lambda Function Versioning and Aliases<a name="versioning-aliases"></a>

By using versioning, you can manage your in\-production function code in AWS Lambda better\. When you use versioning in AWS Lambda, you can publish one or more versions of your Lambda function\. As a result, you can work with different variations of your Lambda function in your development workflow, such as development, beta, and production\. 

Each Lambda function version has a unique Amazon Resource Name \(ARN\)\. After you publish a version, it can't be changed\.

AWS Lambda also supports creating aliases for each of your Lambda function versions\. Conceptually, an AWS Lambda alias is a pointer to a specific Lambda function version\. It's also a resource similar to a Lambda function, and each alias has a unique ARN\. Each alias maintains an ARN for the function version to which it points\. An alias can only point to a function version, not to another alias\. Unlike versions, aliases can be modified\. You can update aliases to point to different versions of functions\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_intro_2_10.png)

Aliases enable you to abstract the process of promoting new Lambda function versions into production from the mapping of the Lambda function version and its event source\.

For example, suppose Amazon S3 is the event source that invokes your Lambda function when new objects are created in a bucket\. When Amazon S3 is your event source, you store the event source mapping information in the bucket notification configuration\. In that configuration, you can identify the Lambda function ARN that Amazon S3 can invoke\. However, in this case each time you publish a new version of your Lambda function you need to update the notification configuration so that Amazon S3 invokes the correct version\. 

In contrast, instead of specifying the function ARN, suppose that you specify an alias ARN in the notification configuration \(for example, PROD alias ARN\)\. As you promote new versions of your Lambda function into production, you only need to update the PROD alias to point to the latest stable version\. You don't need to update the notification configuration in Amazon S3\. 

The same applies when you need to roll back to a previous version of your Lambda function\. In this scenario, you just update the PROD alias to point to a different function version\. There is no need to update event source mappings\.

We recommend that you use versioning and aliases to deploy your Lambda functions when building applications with multiple dependencies and developers involved\. 

**Topics**
+ [Introduction to AWS Lambda Versioning](versioning-intro.md)
+ [Introduction to AWS Lambda Aliases](aliases-intro.md)
+ [Versioning, Aliases, and Resource Policies](versioning-aliases-permissions.md)
+ [Managing Versioning Using the AWS Management Console, the AWS CLI, or Lambda API Operations](how-to-manage-versioning.md)
+ [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)