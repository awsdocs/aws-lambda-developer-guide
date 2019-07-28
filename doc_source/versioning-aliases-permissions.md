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

When you use a [resource\-based policy](access-control-resource-based.md) to give a service, resource, or account access to your function, the scope of that permission depends on if you applied it to a version, to an alias, or to a function\.
+ If you use a qualified function name \(such as `helloworld:1`\), the permission is valid for invoking the `helloworld` function version 1 *only* using its qualified ARN \(using any other ARNs results in a permission error\)\. 
+ If you use an alias name \(such as `helloworld:BETA`\), the permission is valid only for invoking the `helloworld` function using the BETA alias ARN \(using any other ARNs results in a permission error, including the function version ARN to which the alias points\)\.
+ If you use an unqualified function name \(such as `helloworld`\), the permission is valid only for invoking the `helloworld` function using the unqualified function ARN \(using any other ARNs will result in a permission error\)\. 
**Note**  
Note that even though the access policy is only on the unqualified ARN, the code and configuration of the invoked Lambda function is still from function version `$LATEST`\. The unqualified function ARN maps to the `$LATEST` version but the permissions you add are ARN\-specific\.
+ If you use a qualified function name using the `$LATEST` version \(`helloworld:$LATEST`\), the permission is valid for invoking the `helloworld` function version `$LATEST` *only* using its qualified ARN \(using unqualified ARN results in a permission error\)\. 

For example, the following AWS CLI command grants Amazon S3 permissions to invoke the PROD alias of the `helloworld` Lambda function \(note that the `--qualifier` parameter specifies the alias name\)\. 

```
aws lambda add-permission --function-name helloworld \
--qualifier PROD --statement-id 1 --principal s3.amazonaws.com --action lambda:InvokeFunction \
--source-arn arn:aws:s3:::examplebucket --source-account 111111111111
```

In this case, Amazon S3 is now able to invoke the PROD alias and AWS Lambda can then execute the helloworld Lambda function version that the PROD alias points to\. For this to work, you must use the PROD alias ARN in the S3 bucket's notification configuration\. 

For information about how to handle Amazon S3 events, see [Using AWS Lambda with Amazon S3](with-s3.md)\.