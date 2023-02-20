# Lambda permissions<a name="lambda-permissions"></a>

You can use AWS Identity and Access Management \(IAM\) to manage access to the Lambda API and resources such as functions and layers\. For users and applications in your account that use Lambda, you can create IAM policies that apply to IAM users, groups, or roles\.

Every Lambda function has an IAM role called an [execution role](lambda-intro-execution-role.md)\. In this role, you can attach a policy that defines the permissions that your function needs to access other AWS services and resources\. At a minimum, your function needs access to Amazon CloudWatch Logs for log streaming\. If your function calls other service APIs with the AWS SDK, you must include the necessary permissions in the execution role's policy\. Lambda also uses the execution role to get permission to read from event sources when you use an [event source mapping](invocation-eventsourcemapping.md) to invoke your function\.

To give other accounts and AWS services permission to use your Lambda resources, use a [resource\-based policy](access-control-resource-based.md)\. Lambda resources include functions, versions, aliases, and layer versions\. When a user tries to access a Lambda resource, Lambda considers both the user's [identity\-based policies](access-control-identity-based.md) and the resource's resource\-based policy\. When an AWS service such as Amazon Simple Storage Service \(Amazon S3\) calls your Lambda function, Lambda considers only the resource\-based policy\.

To manage permissions for users and applications in your account, we recommend using an [AWS managed policy](access-control-identity-based.md)\. You can use these managed policies as\-is, or as a starting point for writing your own more restrictive policies\. Policies can restrict user permissions by the resource that an action affects, and by additional optional conditions\. For more information, see [Resources and conditions for Lambda actions](lambda-api-permissions-ref.md)\.

If your Lambda functions contain calls to other AWS resources, you might also want to restrict which functions can access those resources\. To do this, include the `lambda:SourceFunctionArn` condition key in a resource\-based policy for the target resource\. For more information, see [Working with Lambda execution environment credentials](lambda-intro-execution-role.md#permissions-executionrole-source-function-arn)\.

For more information about IAM, see the *[IAM User Guide](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html)*\.

For more information about applying security principles to Lambda applications, see [Security](https://docs.aws.amazon.com/lambda/latest/operatorguide/security-ops.html) in the *AWS Lambda Operator Guide*\.

**Topics**
+ [Lambda execution role](lambda-intro-execution-role.md)
+ [Identity\-based IAM policies for Lambda](access-control-identity-based.md)
+ [Attribute\-based access control for Lambda](attribute-based-access-control.md)
+ [Using resource\-based policies for Lambda](access-control-resource-based.md)
+ [Resources and conditions for Lambda actions](lambda-api-permissions-ref.md)
+ [Using permissions boundaries for AWS Lambda applications](permissions-boundary.md)