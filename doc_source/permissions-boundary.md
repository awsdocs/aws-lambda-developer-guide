# Using permissions boundaries for AWS Lambda applications<a name="permissions-boundary"></a>

When you [create an application](applications-tutorial.md) in the AWS Lambda console, Lambda applies a *permissions boundary* to the application's IAM roles\. The permissions boundary limits the scope of the [execution role](lambda-intro-execution-role.md) that the application's template creates for each of its functions, and any roles that you add to the template\. The permissions boundary prevents users with write access to the application's Git repository from escalating the application's permissions beyond the scope of its own resources\.

The application templates in the Lambda console include a global property that applies a permissions boundary to all functions that they create\. 

```
Globals:
  Function:
    PermissionsBoundary: !Sub 'arn:${AWS::Partition}:iam::${AWS::AccountId}:policy/${AppId}-${AWS::Region}-PermissionsBoundary'
```

The boundary limits the permissions of the functions' roles\. You can add permissions to a function's execution role in the template, but that permission is only effective if it's also allowed by the permissions boundary\. The role that AWS CloudFormation assumes to deploy the application enforces the use of the permissions boundary\. That role only has permission to create and pass roles that have the application's permissions boundary attached\.

By default, an application's permissions boundary enables functions to perform actions on the resources in the application\. For example, if the application includes an Amazon DynamoDB table, the boundary allows access to any API action that can be restricted to operate on specific tables with resource\-level permissions\. You can only use actions that don't support resource\-level permissions if they're specifically permitted in the boundary\. These include Amazon CloudWatch Logs and AWS X\-Ray API actions for logging and tracing\.

**Example permissions boundary**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "*"
            ],
            "Resource": [
                "arn:aws:lambda:us-east-2:123456789012:function:my-app-getAllItemsFunction-*",
                "arn:aws:lambda:us-east-2:123456789012:function:my-app-getByIdFunction-*",
                "arn:aws:lambda:us-east-2:123456789012:function:my-app-putItemFunction-*",
                "arn:aws:dynamodb:us-east-1:123456789012:table/my-app-SampleTable-*"
            ],
            "Effect": "Allow",
            "Sid": "StackResources"
        },
        {
            "Action": [
                "logs:CreateLogGroup",
                "logs:CreateLogStream",
                "logs:DescribeLogGroups",
                "logs:PutLogEvents",
                "xray:Put*"
            ],
            "Resource": "*",
            "Effect": "Allow",
            "Sid": "StaticPermissions"
        },
        ...
    ]
}
```

To access other resources or API actions, you or an administrator must expand the permissions boundary to include those resources\. You might also need to update the execution role or deployment role of an application to allow the use of additional actions\.
+ **Permissions boundary** – Extend the application's permissions boundary when you add resources to your application, or the execution role needs access to more actions\. In IAM, add resources to the boundary to allow the use of API actions that support resource\-level permissions on that resource's type\. For actions that don't support resource\-level permissions, add them in a statement that isn't scoped to any resource\.
+ **Execution role** – Extend a function's execution role when it needs to use additional actions\. In the application template, add policies to the execution role\. The intersection of permissions in the boundary and execution role is granted to the function\.
+ **Deployment role** – Extend the application's deployment role when it needs additional permissions to create or configure resources\. In IAM, add policies to the application's deployment role\. The deployment role needs the same user permissions that you need to deploy or update an application in AWS CloudFormation\.

For a tutorial that walks through adding resources to an application and extending its permissions, see [Creating an application with continuous delivery in the Lambda console](applications-tutorial.md)\.

For more information, see [Permissions boundaries for IAM entities](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_boundaries.html) in the IAM User Guide\.