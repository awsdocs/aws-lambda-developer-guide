# Introduction to AWS Lambda Aliases<a name="aliases-intro"></a>

You can create one or more aliases for your Lambda function\. An AWS Lambda alias is like a pointer to a specific Lambda function version\. For more information about versioning, see [Introduction to AWS Lambda Versioning](versioning-intro.md)\. 

By using aliases, you can access the Lambda function an alias is pointing to \(for example, to invoke the function\) without the caller having to know the specific version the alias is pointing to\.

AWS Lambda aliases enable the following use cases:
+ **Easier support for promotion of new versions of Lambda functions and rollback when needed** – After initially creating a Lambda function \(the `$LATEST` version\), you can publish a version 1 of it\. By creating an alias named PROD that points to version 1, you can now use the PROD alias to invoke version 1 of the Lambda function\. 

  Now, you can update the code \(the `$LATEST` version\) with all of your improvements, and then publish another stable and improved version \(version 2\)\. You can promote version 2 to production by remapping the PROD alias so that it points to version 2\. If you find something wrong, you can easily roll back the production version to version 1 by remapping the PROD alias so that it points to version 1\.
**Note**  
In this context, the terms *promotion* and *roll back* refer to the remapping of aliases to different function versions\.
+ **Simplify management of event source mappings** – Instead of using Amazon Resource Names \(ARNs\) for Lambda function in event source mappings, you can use an alias ARN\. This approach means that you don't need to update your event source mappings when you promote a new version or roll back to a previous version\. 

An AWS Lambda alias is a resource similar to a Lambda function\. However, you can't create an alias independently\. You create an alias for an existing Lambda function\. If a Lambda function is a resource, you can think of an AWS Lambda alias as a subresource that is associated with a Lambda function\. 

Both the Lambda function and alias are AWS Lambda resources, and like all other AWS resources they both have unique ARNs\. The following example shows a Lambda function \(the `$LATEST` version\), with one published version\. Each version has an alias pointing to it\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_intro_2_10.png)

You can access the function using either the function ARN or the alias ARN\.
+ Because the function version for an unqualified function always maps to `$LATEST`, you can access it using the qualified or unqualified function ARN\. The following shows a qualified function ARN with the `$LATEST` version suffix\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:$LATEST
  ```
+ When using any of the alias ARNs, you are using a qualified ARN\. Each alias ARN has an alias name suffix\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:PROD
  arn:aws:lambda:aws-region:acct-id:function:helloworld:BETA
  arn:aws:lambda:aws-region:acct-id:function:helloworld:DEV
  ```

AWS Lambda provides the following API operations for you to create and manage aliases:
+ [CreateAlias](API_CreateAlias.md)
+ [UpdateAlias](API_UpdateAlias.md)
+ [GetAlias](API_GetAlias.md)
+ [ListAliases](API_ListAliases.md)
+ [DeleteAlias](API_DeleteAlias.md)

## Example: Using Aliases to Manage Lambda Function Versions<a name="aliases-intro-example"></a>

The following is an example scenario of how to use versioning and aliases to promote new versions of Lambda functions into production\.

**Initially, you create a Lambda function\.**  
The function you create is the `$LATEST` version\. You also create an alias \(DEV, for development\) that points to the newly created function\. Developers can use this alias to test the function with the event sources in a development environment\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_scenario_2_10.png)

**You then test the function version using event sources in a beta environment in a stable way, while continuing to develop newer versions\.**  
You publish a version from the `$LATEST` and have another alias \(BETA\) point to it\. This approach allows you to associate your beta event sources to this specific alias\. In the event source mappings, use the BETA alias to associate your Lambda function with the event source\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_scenario_2_20.png)

**You next promote the Lambda function version in production to work with event sources in production environment\.**  
After testing the BETA version of the function, you can define the production version by creating an alias that maps to version 1\. In this approach, you point your production event sources to this specific version\. You do this by creating a PROD alias and using the PROD alias ARN in all of your production event source mappings\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_scenario_2_30.png)

**You continue development, publish more versions, and test\.**  
As you develop your code, you can update the `$LATEST` version by uploading updated code and then publish to beta testing by having the BETA alias point to it\. This simple remapping of the beta alias lets you put version 2 of your Lambda function into beta without changing any of your event sources\. This approach is how aliases enable you to control which versions of your function are used with specific event sources in your development environment\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_scenario_2_40.png)

If you want to try creating this setup using the AWS Command Line Interface, see [Tutorial: Using AWS Lambda Aliases](versioning-aliases-walkthrough1.md)\.

## Related Topics<a name="aliases-intro-related-topics"></a>

[Introduction to AWS Lambda Versioning](versioning-intro.md)

[Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)

[Tutorial: Using AWS Lambda Aliases](versioning-aliases-walkthrough1.md)

[Managing Versioning Using the AWS Management Console, the AWS CLI, or Lambda API Operations](how-to-manage-versioning.md)