# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-dynamodb-create-execution-role"></a>

In this section, you create an IAM role using the following predefined role type and access policy:

+ AWS service role of the type **Lambda** – This role grants AWS Lambda permissions to call other AWS services\. 

+ **AWSLambdaDynamoDBExecutionRole** – This contains the DynamoDB permissions policy that you attach to augment Lambda's basic execution policy and allows the two services to interoperate under your Lambda function's account\.

 For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide* as well as the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. 

**To create an IAM role \(execution role\) for this exercise, do the following:**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Choose **Roles**

1. Choose **Create role**

1. In **Select type of trusted entity**, choose **AWS service**, and then choose **Lambda**\. This will allow Lambda functions to call AWS services under your account\.

1. Choose **Next: Permissions**

1. In **Filter: Policy type** enter **AWSLambdaDynamoDBExecutionRole** and choose **Next: Review**\. 

1. In **Role name\***, enter a role name that is unique within your AWS account \(for example, **lambda\-dynamodb\-execution\-role**\) and then choose **Create role**\. 

1. Under the **Summary** of your role, record the **Role ARN** \(Amazon Resource Name\)\. You will supply that value in the next step when you create your Lambda function\.

## Next Step<a name="with-ddb-next-step-3"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-dynamodb-create-function.md)