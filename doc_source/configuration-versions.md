# AWS Lambda function versions<a name="configuration-versions"></a>

You can use versions to manage the deployment of your AWS Lambda functions\. For example, you can publish a new version of a function for beta testing without affecting users of the stable production version\. 

The system creates a new version of your Lambda function each time that you publish the function\. The new version is a copy of the unpublished version of the function\. The function version includes the following information:
+ The function code and all associated dependencies\.
+ The Lambda runtime that executes the function\.
+ All of the function settings, including the environment variables\.
+ A unique Amazon Resource Name \(ARN\) to identify this version of the function\.

You can change the function code and settings only on the unpublished version of a function\. When you publish a version, the code and most of the settings are locked to ensure a consistent experience for users of that version\. For more information about configuring function settings, see [Configuring functions in the AWS Lambda console](configuration-console.md)\.

**To create a new version of a function**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose the function that you want to publish\.

1. In **Actions**, choose **Publish new version**\.

After you publish the first version of a function, the Lambda console displays a drop\-down menu of the available versions\. The **Designer** panel displays a version qualifier at the end of the function name\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/version-1-created.png)

To view the current versions of the function, choose a function, and then choose **Qualifiers**\. In the expanded **Qualifiers** menu, choose the **Versions** tab\. The **Versions** panel displays the list of versions for the selected function\. If you haven't published a version of the selected function, the **Versions** panel lists only the `$LATEST` version\.

## Managing versions with the Lambda API<a name="versioning-versions-api"></a>

To publish a version of a function, use the [PublishVersion](API_PublishVersion.md) API action\.

The following example publishes a new version of a function\. The response returns configuration information about the new version, including the version number and the function ARN with the version suffix\.

```
$ aws lambda publish-version --function-name my-function
{
  "FunctionName": "my-function",
  "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function:1",
  "Version": "1",
  "Role": "arn:aws:iam::123456789012:role/lambda-role",
  "Handler": "function.handler",
  "Runtime": "nodejs12.x",
  ...
}
```

## Using versions<a name="versioning-versions-using"></a>

You reference your Lambda function using its ARN\. There are two ARNs associated with this initial version:
+ **Qualified ARN** – The function ARN with the version suffix\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:$LATEST
  ```
+ **Unqualified ARN** – The function ARN without the version suffix\. 

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld
  ```

  You can use this unqualified ARN in all relevant operations\. However, you can't use it to create an alias\. 

If you decide not to publish function versions, you can use either the qualified or unqualified ARN in your event source mapping to invoke the function\.

Lambda only publishes a new function version if the code has never been published or if the code has changed compared to the most recently published version\. If there is no change, the function version remains at the most recently published version\.

Each Lambda function version has a unique ARN\. After you publish a version, you can't change the ARN or the function code\.

## Resource policies<a name="versioning-permissions"></a>

When you use a [resource\-based policy](access-control-resource-based.md) to give a service, resource, or account access to your function, the scope of that permission depends on whether you applied it to a function or to one version of a function:
+ If you use a qualified function name \(such as `helloworld:1`\), the permission is valid for invoking the `helloworld` function version 1 *only* using its qualified ARN\. Using any other ARNs results in a permission error\.
+ If you use an unqualified function name \(such as `helloworld`\), the permission is valid only for invoking the `helloworld` function using the unqualified function ARN\. Using any other ARNs, including `$LATEST`, results in a permission error\.
+ If you use the `$LATEST` qualified function name \(such as `helloworld:$LATEST`\), the permission is valid for invoking the `helloworld` function *only* using its qualified ARN\. Using an unqualified ARN results in a permission error\.

You can simplify the management of event sources and resource policies by using function aliases\. For more information, see [AWS Lambda function aliases](configuration-aliases.md)\.