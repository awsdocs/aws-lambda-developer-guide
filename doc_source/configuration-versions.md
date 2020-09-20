# Lambda function versions<a name="configuration-versions"></a>

You can use versions to manage the deployment of your functions\. For example, you can publish a new version of a function for beta testing without affecting users of the stable production version\. Lambda creates a new version of your function each time that you publish the function\. The new version is a copy of the unpublished version of the function\. 

A function version includes the following information:
+ The function code and all associated dependencies\.
+ The Lambda runtime that invokes the function\.
+ All of the function settings, including the environment variables\.
+ A unique Amazon Resource Name \(ARN\) to identify the specific version of the function\.

You can change the function code and settings only on the unpublished version of a function\. When you publish a version, the code and most of the settings are locked to maintain a consistent experience for users of that version\. For more information about configuring function settings, see [Configuring functions in the AWS Lambda console](configuration-console.md)\.

**To create a new version of a function**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose the name of the function that you want to publish\.

1. On the function configuration page, choose **Actions**, **Publish new version**\.

1. \(Optional\) Enter a version description\.

1. Choose **Publish**\.

After you publish the first version of a function, the Lambda console displays a dropdown list of the available versions\. The **Designer** panel displays a version qualifier at the end of the function name\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/version-1-created.png)

To view the current versions of a function, on the function configuration page, choose **Qualifiers**, and then choose the **Versions** tab to see a list of versions for the function\. If you haven't published a new version of the function, the list only displays the `$LATEST` version\.

## Managing versions with the Lambda API<a name="versioning-versions-api"></a>

To publish a version of a function, use the [PublishVersion](API_PublishVersion.md) API operation\.

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

You can reference your Lambda function using either a qualified ARN or an unqualified ARN\.
+ **Qualified ARN** – The function ARN with a version suffix\. The following example refers to version 42 of the `helloworld` function\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:42
  ```
+ **Unqualified ARN** – The function ARN without a version suffix\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld
  ```

You can use a qualified or an unqualified ARN in all relevant API operations\. However, you can't use an unqualified ARN to create an alias\.

If you decide not to publish function versions, you can invoke the function using either the qualified or unqualified ARN in your [event source mapping](invocation-eventsourcemapping.md)\. When you invoke a function using an unqualified ARN, Lambda implicitly invokes $LATEST\.

Lambda publishes a new function version only if the code has never been published or if the code has changed from the last published version\. If there is no change, the function version remains at the last published version\.

The qualified ARN for each Lambda function version is unique\. After you publish a version, you can't change the ARN or the function code\.

## Granting permissions<a name="versioning-permissions"></a>

You can use a [resource\-based policy](access-control-resource-based.md) or an [identity\-based policy](access-control-identity-based.md) to grant access to your function\. The scope of the permission depends on whether you apply the policy to a function or to one version of a function\. For more information about function resource names in policies, see [Resources and conditions for Lambda actions](lambda-api-permissions-ref.md)\. 

You can simplify the management of event sources and AWS Identity and Access Management \(IAM\) policies by using function aliases\. For more information, see [Lambda function aliases](configuration-aliases.md)\.