# Versioning, Aliases, and Resource Policies<a name="versioning-aliases-permissions"></a>

With versioning and aliases you can access a Lambda function using various ARNs\. For example, consider the following scenario\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/alias_scenario_2_20.png)

You can invoke for example the `helloworld` function version 1 using any of the following two ARNs:
+ Using the qualified function ARN as shown following\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:1
  ```
+ Using the BETA alias ARN as shown following\.

  ```
  arn:aws:lambda:aws-region:acct-id:function:helloworld:BETA
  ```

In a *push* model, event sources \(such as Amazon S3 and custom applications\) can invoke any of the Lambda function versions as long you grant the necessary permissions to these event sources by using an access policy associated with the Lambda function\. For more information about the push model, see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\. 

Assuming that you grant permission, the next question is, "can an event source invoke a function version using any of the associated ARNs?" The answer is, it depends on how you identified function in your add permissions request \(see [AddPermission](API_AddPermission.md)\)\. The key to understanding this is that the permission you grant apply only to the ARN used in the add permission request:
+ If you use a qualified function name \(such as `helloworld:1`\), the permission is valid for invoking the `helloworld` function version 1 *only* using its qualified ARN \(using any other ARNs results in a permission error\)\. 
+ If you use an alias name \(such as `helloworld:BETA`\), the permission is valid only for invoking the `helloworld` function using the BETA alias ARN \(using any other ARNs results in a permission error, including the function version ARN to which the alias points\)\.
+ If you use an unqualified function name \(such as `helloworld`\), the permission is valid only for invoking the `helloworld` function using the unqualified function ARN \(using any other ARNs will result in a permission error\)\. 
**Note**  
Note that even though the access policy is only on the unqualified ARN, the code and configuration of the invoked Lambda function is still from function version `$LATEST`\. The unqualified function ARN maps to the `$LATEST` version but the permissions you add are ARN\-specific\.
+ If you use a qualified function name using the `$LATEST` version \(`helloworld:$LATEST`\), the permission is valid for invoking the `helloworld` function version `$LATEST` *only* using its qualified ARN \(using unqualified ARN results in a permission error\)\. 