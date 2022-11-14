# Lambda function versions<a name="configuration-versions"></a>

You can use versions to manage the deployment of your functions\. For example, you can publish a new version of a function for beta testing without affecting users of the stable production version\. Lambda creates a new version of your function each time that you publish the function\. The new version is a copy of the unpublished version of the function\. 

**Note**  
Lambda doesn't create a new version if the code in the unpublished version is the same as the previous published version\. You need to deploy code changes in $LATEST before you can create a new version\.

A function version includes the following information:
+ The function code and all associated dependencies\.
+ The Lambda runtime that invokes the function\.
+ All the function settings, including the environment variables\.
+ A unique Amazon Resource Name \(ARN\) to identify the specific version of the function\.

**Topics**
+ [Creating function versions](#configuration-versions-config)
+ [Managing versions with the Lambda API](#versioning-versions-api)
+ [Using versions](#versioning-versions-using)
+ [Granting permissions](#versioning-permissions)

## Creating function versions<a name="configuration-versions-config"></a>

You can change the function code and settings only on the unpublished version of a function\. When you publish a version, Lambda locks the code and most of the settings to maintain a consistent experience for users of that version\. For more information about configuring function settings, see [Configuring Lambda function options](configuration-function-common.md)\.

You can create a function version using the Lambda console\.

**To create a new function version**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function and then choose **Versions**\.

1. On the versions configuration page, choose **Publish new version**\.

1. \(Optional\) Enter a version description\.

1. Choose **Publish**\.

## Managing versions with the Lambda API<a name="versioning-versions-api"></a>

To publish a version of a function, use the [PublishVersion](API_PublishVersion.md) API operation\.

The following example publishes a new version of a function\. The response returns configuration information about the new version, including the version number and the function ARN with the version suffix\.

```
aws lambda publish-version --function-name my-function
```

You should see the following output:

```
{
  "FunctionName": "my-function",
  "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function:1",
  "Version": "1",
  "Role": "arn:aws:iam::123456789012:role/lambda-role",
  "Handler": "function.handler",
  "Runtime": "nodejs16.x",
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