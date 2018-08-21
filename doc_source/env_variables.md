# Environment Variables<a name="env_variables"></a>

Environment variables for Lambda functions enable you to dynamically pass settings to your function code and libraries, without making changes to your code\. Environment variables are key\-value pairs that you create and modify as part of your function configuration, using either the AWS Lambda Console, the AWS Lambda CLI or the AWS Lambda SDK\. AWS Lambda then makes these key value pairs available to your Lambda function code using standard APIs supported by the language, like `process.env` for Node\.js functions\. 

You can use environment variables to help libraries know what directory to install files in, where to store outputs, store connection and logging settings, and more\. By separating these settings from the application logic, you don't need to update your function code when you need to change the function behavior based on different settings\. 

**Note**  
This feature is not yet available in AWS regions based in China \(Beijing or Ningxia\)\. Deploying a Lambda function that contains Environment Variables will result in an `InvalidParameterException`\.

## Setting Up<a name="env_setting_up"></a>

Suppose you want a Lambda function to behave differently as it moves through lifecycle stages from development to deployment\. For example, the dev, test, and production stages can contain databases that the function needs to connect to that require different connection information and use different table names\. You can create environment variables to reference the database names, connection information or table names and set the value for the function based on the stage in which it’s executing \(for example, development, test, production\) while your function code remains unchanged\. 

The following screenshots show how to modify your function's configuration using the AWS console\. The first screenshot configures the settings for the function corresponding to a test stage\. The second one configures settings for a production stage\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/env_variables1.png)

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/env_vars2.png)

Note the **Encryption configuration** section\. You will learn more about using this in the [Create a Lambda Function Using Environment Variables To Store Sensitive Information](tutorial-env_console.md) tutorial\.

You can also use the AWS CLI to create Lambda functions that contain environment variables\. For more details, see the [CreateFunction](API_CreateFunction.md) and [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) APIs\. Environment variables are also supported when creating and updating functions using AWS CloudFormation\. Environment variables can also be used to configure settings specific to the language runtime or a library included in your function\. For example, you can modify `PATH` to specify a directory where executables are stored\. You can also set runtime\-specific environment variables, such as `PYTHONPATH` for Python or `NODE_PATH` for Node\.js\. 

The following example creates a new Lambda function that sets the `LD_LIBRARY_PATH` environment variable, which is used to specify a directory where shared libraries are dynamically loaded at runtime\. In this example, the Lambda function code uses the shared library in the `/usr/bin/test/lib64` directory\. Note that the `Runtime` parameter uses `nodejs6.10` but you can also specify `nodejs8.10`\. 

```
aws lambda create-function \
    --region us-east-1
    --function-name myTestFunction
    --zip-file fileb://path/package.zip
    --role role-arn
    --environment Variables="{LD_LIBRARY_PATH=/usr/bin/test/lib64}"
    --handler index.handler
    --runtime nodejs6.10
    --profile default
```

## Rules for Naming Environment Variables<a name="env_limits"></a>

There is no limit to the number of environment variables you can create as long as the total size of the set does not exceed 4 KB\.

Other requirements include:
+ Must start with letters *\[a\-zA\-Z\]*\. 
+ Can only contain alphanumeric characters and underscores *\(\[a\-zA\-Z0\-9\_\]*\. 

In addition, there are a specific set of keys that AWS Lambda reserves\. If you try to set values for any of these reserved keys, you will receive an error message indicating that the action is not allowed\. For more information on these keys, see [Environment Variables Available to Lambda Functions](current-supported-versions.md#lambda-environment-variables)\.

## Environment Variables and Function Versioning<a name="env_versioning"></a>

Function versioning provides a way to manage your Lambda function code by enabling you to publish one or more versions of your Lambda function as it proceeds from development to test to production\. For each version of a Lambda function that you publish, the environment variables \(as well as other function\-specific configurations such as `MemorySize` and `Timeout` limit\) are saved as a snapshot of that version and those settings are immutable \(cannot be changed\)\.

As application and configuration requirements evolve, you can create new versions of your Lambda function and update the environment variables to meet those requirements prior to the newest version being published\. The current version of your function is $LATEST\. 

In addition, you can create aliases, which are pointers to a particular version of your function\. The advantage of aliases is that if you need to roll back to a previous function version, you point the alias to that version, which contains the environment variables required for that version\. For more information, see [AWS Lambda Function Versioning and Aliases](versioning-aliases.md)\.

## Environment Variable Encryption<a name="env_encrypt"></a>

When you create or update Lambda functions that use environment variables, AWS Lambda encrypts them using the [AWS Key Management Service](http://docs.aws.amazon.com/kms/latest/developerguide/)\. When your Lambda function is invoked, those values are decrypted and made available to the Lambda code\. 

The first time you create or update Lambda functions that use environment variables in a region, a default service key is created for you automatically within AWS KMS\. This key is used to encrypt environment variables\. However, should you wish to use encryption helpers and use KMS to encrypt environment variables after your Lambda function is created, then you must create your own AWS KMS key and choose it instead of the default key\. The default key will give errors when chosen\. Creating your own key gives you more flexibility, including the ability to create, rotate, disable, and define access controls, and to audit the encryption keys used to protect your data\. For more information, see the [AWS Key Management Service Developer Guide](http://docs.aws.amazon.com/kms/latest/developerguide/)\.

If you use your own key, you will be billed per [AWS Key Management Service Pricing](https://aws.amazon.com/kms/pricing/) guidelines\. You will not be billed if you use the default service key provided by AWS Lambda\.

If you’re using the default KMS service key for Lambda, then no additional IAM permissions are required in your function execution role – your role will just work automatically without changes\. If you’re supplying your own \(custom\) KMS key, then you’ll need to add `kms:Decrypt` to your execution role\. In addition, the user that will be creating and updating the Lambda function must have permissions to use the KMS key\. For more information on KMS keys, see the [Using Key Policies in AWS KMS](http://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html)\.

**Note**  
AWS Lambda authorizes your function to use the default KMS key through a user grant, which it adds when the role is first selected\. If you re\-create a function's execution role \(that is, delete and create a role of the same name\) and the role does not have `kms:Decrypt` permissions, you will need to refresh the role's grant\. You can do so by toggling the function's execution role after the role has been re\-created in the console\. 

### Storing Sensitive Information<a name="env-storing-sensitive-data"></a>

As mentioned in the previous section, when you deploy your Lambda function, all the environment variables you've specified are encrypted by default after, but not during, the deployment process\. They are then decrypted automatically by AWS Lambda when the function is invoked\. If you need to store sensitive information in an environment variable, we strongly suggest you encrypt that information before deploying your Lambda function\.

 Fortunately, the Lambda console makes that easier for you by providing encryption helpers that leverage [AWS Key Management Service](http://docs.aws.amazon.com/kms/latest/developerguide/) to store that sensitive information as `Ciphertext`\. The Lambda console also provides decryption helper code to decrypt that information for use in your in Lambda function code\. For more information, see [Create a Lambda Function Using Environment Variables To Store Sensitive Information](tutorial-env_console.md)\.

### Error scenarios<a name="env-errors"></a>

If your function configuration exceeds 4KB, or you use environment variable keys reserved by AWS Lambda, then your update or create operation will fail with a configuration error\. During execution time, it's possible that the encryption/decryption of environment variables can fail\. If AWS Lambda is unable to decrypt the environment variables due to an AWS KMS service exception, AWS KMS will return an exception message explaining what the error conditions are and what, if any, remedies you can apply to address the issue\. These will be logged to your function log stream in Amazon CloudWatch logs\. For example, if the KMS key you are using to access the environment variables is disabled, you will see the following error: 

```
Lambda was unable to configure access to your environment variables because the KMS key used is disabled. 
            Please check your KMS key settings.
```

### Next Step<a name="env-next-step"></a>

[Create a Lambda Function Using Environment Variables](tutorial-env_cli.md)