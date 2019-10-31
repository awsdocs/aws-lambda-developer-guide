# Using AWS Lambda Application Permissions Boundaries<a name="permissions-boundary"></a>

When you [create an application](applications-tutorial.md) in the AWS Lambda console, Lambda applies a *permissions boundary* to the application's IAM roles\. The permissions boundary limits the scope of the [execution role](lambda-intro-execution-role.md) that the application's template creates for each of its functions, and any roles that you add to the template\. The permissions boundary prevents users with write access to the application's Git repository from escalating the application's privileges beyond the scope of its own resources\.

The application templates in the Lambda console include a global property that applies a permissions boundary to all functions that they create\. 

```
Globals:
  Function:
    PermissionsBoundary: !Sub 'arn:${AWS::Partition}:iam::${AWS::AccountId}:policy/${AppId}-${AWS::Region}-PermissionsBoundary'
```

The boundary limits the permissions of the functions' roles\. You can add permissions to a function's execution role in the template, but that permission is only effective if it is also allowed by the permissions boundary\. Use of the permissions boundary is enforced by the role that AWS CloudFormation assumes to deploy the application\. That role only has permission to create and pass roles that have the application's permissions boundary attached\.

By default, an application's permissions boundary enables functions to perform actions on the resources in the application\. For example, if the application includes a Amazon DynamoDB table, the boundary allows access to any API action that can be restricted to operate on specific tables with resource\-level permissions\. Actions that don't support resource\-level permissions can only be used if they are specifically permitted in the boundary\. These include Amazon CloudWatch Logs and AWS X\-Ray API actions for logging and tracing\.

**Example Permissions Boundary**  

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

To access other resources or API actions, you or an administrator needs to expand the permissions boundary to include those resources\. You may also need to update functions' execution roles or deployment role to allow use of additional API actions\.
+ **Permissions boundary** – In IAM, add resources to the boundary to allow use of API actions that support resource\-level permissions on that resource's type\. For API actions that don't support resource\-level permissions, add them in a statement that is not scoped to any resource\.
+ **Execution role** – In the application template, add policies to the execution role\. The intersection of permissions in the boundary and execution role is granted to the function\.
+ **Deployment role** – In IAM, add policies to the application's deployment role\. This is only required if the deployment role doesn't have permission to create a resource that you want to add to your application\.

For a tutorial that walks through adding resources to and extending the permissions of an application, see [Creating an Application with Continuous Delivery in the Lambda Console](applications-tutorial.md)\.

For more information, see [Permissions Boundaries for IAM Entities](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_boundaries.html) in the IAM User Guide\.