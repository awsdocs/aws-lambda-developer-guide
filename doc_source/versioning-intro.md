# Introduction to AWS Lambda Versioning<a name="versioning-intro"></a>

Following, you can find how to create a Lambda function and publish a version from it\. You can also find how to update function code and configuration information when you have one or more published versions\. In addition, you can find information on how to delete function versions, either specific versions or an entire Lambda function with all of its versions and associated aliases\.

## Creating a Lambda Function \(the $LATEST Version\)<a name="versioning-intro-create-function"></a>

When you create a Lambda function, there is only one version—the `$LATEST` version\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-versioning-v2-latest1.png)

You can refer to this function using its Amazon Resource Name \(ARN\)\. There are two ARNs associated with this initial version:
+ **Qualified ARN** – The function ARN with the version suffix\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:$LATEST
  ```
+ **Unqualified ARN** – The function ARN without the version suffix\. 

  You can use this unqualified ARN in all relevant operations\. However, you cannot use it to create an alias\. For more information, see [Introduction to AWS Lambda Aliases](aliases-intro.md)\. 

  The unqualified ARN has its own resource policies\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld
  ```

**Note**  
Unless you choose to publish versions, the $LATEST function version is the only Lambda function version that you have\. You can use either the qualified or unqualified ARN in your event source mapping to invoke the `$LATEST` version\.

The following is an example response for a `CreateFunction` API call\.

```
{
    "CodeSize": 287,
    "Description": "test function."
    "FunctionArn": "arn:aws:lambda:aws-region:acct-id:function:helloworld",
    "FunctionName": "helloworld",
    "Handler": "helloworld.handler",
    "LastModified": "2015-07-16T00:34:31.322+0000",
    "MemorySize": 128,
    "Role": "arn:aws:iam::acct-id:role/lambda_basic_execution",
    "Runtime": "nodejs6.10",
    "Timeout": 3,
    "CodeSHA256": "OjRFuuHKizEE8tHFIMsI+iHR6BPAfJ5S0rW31Mh6jKg=",
    "Version": "$LATEST" 
}
```

For more information, see [CreateFunction](API_CreateFunction.md)\.

In this response, AWS Lambda returns the unqualified ARN of the newly created function and also its version, `$LATEST`\. The response also shows that the `Version` is `$LATEST`\. The `CodeSha256` is the checksum of the deployment package that you uploaded\.

## Publishing an AWS Lambda Function Version<a name="versioning-intro-publish-version"></a>

When you publish a version, AWS Lambda makes a snapshot copy of the Lambda function code \(and configuration\) in the `$LATEST` version\. A published version is immutable\. That is, you can't change the code or configuration information\. The new version has a unique ARN that includes a version number suffix as shown following\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-versioning-v2-latest+v1_1.png)

You can publish a version by using any of the following methods:
+ **Publish a version explicitly** – You can use the `PublishVersion` API operation to explicitly publish a version\. For more information, see [PublishVersion](API_PublishVersion.md)\. This operation creates a new version using the code and configuration in the `$LATEST` version\.
+ **Publish a version at the time you create or update a Lambda function** – You can also use the `CreateFunction` or `UpdateFunctionCode` requests to publish a version by adding the optional `publish` parameter in the request:
  + Specify the `publish` parameter in your `CreateFunction` request to create a new Lambda function \(the `$LATEST` version\)\. You can then immediately publish the new function by creating a snapshot and assigning it to be version 1\. For more information about `CreateFunction`, see [CreateFunction](API_CreateFunction.md)\.
  + Specify the `publish` parameter in your `UpdateFunctionCode` request to update the code in the `$LATEST` version\. You can then publish a version from the `$LATEST`\. For more information about `UpdateFunctionCode`, see [UpdateFunctionCode](API_UpdateFunctionCode.md)\.

  If you specify the `publish` parameter at the time you create a Lambda function, the function configuration information that AWS Lambda returns in response shows the version number of the newly published version\. In the following example, the version is 1\.

  ```
  {
      "CodeSize": 287,
      "Description": "test function."
      "FunctionArn": "arn:aws:lambda:aws-region:acct-id:function:helloworld",
      "FunctionName": "helloworld",
      "Handler": "helloworld.handler",
      "LastModified": "2015-07-16T00:34:31.322+0000",
      "MemorySize": 128,
      "Role": "arn:aws:iam::acct-id:role/lambda_basic_execution",
      "Runtime": "nodejs6.10",
      "Timeout": 3,
      "CodeSHA256": "OjRFuuHKizEE8tHFIMsI+iHR6BPAfJ5S0rW31Mh6jKg=",
      "Version": "1" 
  }
  ```

**Note**  
Lambda only publishes a new version if the code hasn't yet been published or if the code has changed when compared against the $LATEST version\. If there is no change, the $LATEST published version is returned\.

We recommend that you publish a version at the same time that you create your Lambda function or update your Lambda function code\. This recommendation especially applies when multiple developers contribute to the same Lambda function development\. You can use the `publish` parameter in your request to do this\. 

When you have multiple developers working on a project, you can have a scenario where developer A creates a Lambda function \(`$LATEST` version\)\. Before developer A publishes this version, developer B might update the code \(the deployment package\) associated with the `$LATEST` version\. In this case, you lose the original code that developer A uploaded\. When both developers add the `publish` parameter, it prevents the race condition described\.

Each version of a Lambda function is a unique resource with a Amazon Resource Name \(ARN\)\. The following example shows the ARN of version number 1 of the `helloworld` Lambda function\.

```
arn:aws:lambda:aws-region:acct-id:function:helloworld:1
```

You can publish multiple versions of a Lambda function\. Each time you publish a version, AWS Lambda copies `$LATEST` version \(code and configuration information\) to create a new version\. When you publish additional versions, AWS Lambda assigns a monotonically increasing sequence number for versioning, even if the function was deleted and recreated\. Version numbers are never reused, even for a function that has been deleted and recreated\. This approach means that the consumer of a function version can depend on the executable of that version to never change \(except if it's deleted\)\. 

If you want to reuse a qualifier, use aliases with your versions\. Aliases can be deleted and re\-created with the same name\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-versioning-v2-latest+v1+v2_1.png)

## Updating Lambda Function Code and Configuration<a name="versioning-intro-updating-function-code"></a>

AWS Lambda maintains your latest function code in the `$LATEST` version\. When you update your function code, AWS Lambda replaces the code in the `$LATEST` version of the Lambda function\. For more information, see [UpdateFunctionCode](API_UpdateFunctionCode.md)\.

You have the following options of publishing a new version as you update your Lambda function code:
+ **Publish a version in the same update code request** – Use the `UpdateFunctionCode` API operation \(recommended\)\.
+ **First update the code, and then explicitly publish a version** – Use the `PublishVersion` API operation\.

You can update code and configuration information \(such as description, memory size, and execution timeout\) for the `$LATEST` version of the Lambda function\.

## Deleting a Lambda Function and a Specific Version<a name="versioning-intro-deleting-function-versions"></a>

With versioning, you have the following choices:
+ **Delete a specific version** – You can delete a Lambda function version by specifying the version you want to delete in your `DeleteFunction` request\. If there are aliases that depend on this version, the request fails\. AWS Lambda deletes the version only if there are no aliases dependent on this version\. For more information about aliases, see [Introduction to AWS Lambda Aliases](aliases-intro.md)\.
+ **Delete the entire Lambda function \(all of its versions and aliases\)** – To delete the Lambda function and all of its versions, don't specify any version in your `DeleteFunction` request\. Doing this deletes the entire function including all of its versions and aliases\.