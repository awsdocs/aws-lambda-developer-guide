# Using layers with your Lambda function<a name="invocation-layers"></a>

A Lambda layer is a \.zip file archive that can contain additional code or other content\. A layer can contain libraries, a custom runtime, data, or configuration files\. Use layers to reduce deployment package size and to promote code sharing and separation of responsibilities so that you can iterate faster on writing business logic\.

You can use layers only with Lambda functions [deployed as a \.zip file archive](gettingstarted-package.md#gettingstarted-package-zip)\. For a function [defined as a container image](images-create.md), you can package your preferred runtime and all code dependencies when you create the container image\. For more information, see [ Working with Lambda layers and extensions in container images](http://aws.amazon.com/blogs/compute/working-with-lambda-layers-and-extensions-in-container-images/) on the AWS Compute Blog\.

**Topics**
+ [Configuring functions to use layers](#invocation-layers-using)
+ [Accessing layer content from your function](#invocation-layers-accessing)
+ [Finding layer information](#configuration-layers-finding)
+ [Adding layer permissions](#invocation-layers-permissions)
+ [Using AWS SAM to add a layer to a function](#invocation-layers-cloudformation)
+ [Sample applications](#invocation-layers-samples)

## Configuring functions to use layers<a name="invocation-layers-using"></a>

You can add up to five layers to a Lambda function\. The total unzipped size of the function and all layers cannot exceed the unzipped deployment package size quota of 250 MB\. For more information, see [Lambda quotas](gettingstarted-limits.md)\.

If your functions consume a layer that a different AWS account publishes, your functions can continue to use the layer version after it has been deleted, or after your permission to access the layer is revoked\. However, you cannot create a new function that uses a deleted layer version\.

**Note**  
Make sure that the layers that you add to a function are compatible with the runtime and instruction set architecture of the function\. 

### Configuring layers with the console<a name="invocation-layers-console"></a>

**Adding a layer to a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to configure\.

1. Under **Layers**, choose **Add a layer**

1. Under **Choose a layer**, choose a layer source\. 

1. For the **AWS layers** or **Custom layers** layer source:

   1. Choose a layer from the pull\-down menu\. 

   1. Under **Version**, choose a layer version from the pull\-down menu\. Each layer version entry lists its compatible runtimes and architectures\.

   1. Choose **Add**\.

1. For the **Specify an ARN** layer source:

   1. Enter an ARN in the text box and choose **Verify**\.

   1. Choose **Add**\.

The order in which you add the layers is the order in which Lambda later merges the layer content into the execution environment\. You can change the layer merge order using the console\. 

**Update layer order for your function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to configure\.

1. Under **Layers**, choose **Edit**

1. Choose one of the layers\.

1. Choose **Merge earlier** or **Merge later** to adjust the order of the layers\.

1. Choose **Save**\.

Layers are versioned, and the content of each layer version is immutable\. The layer owner can release a new layer version to provide updated content\. You can use the console to update your functions' layer versions\.

**Update layer versions for your function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Under **Additional resources**, choose **Layers**\.

1. Choose the layer to modify\.

1. Under **Functions using this version**, select the functions you want to modify, then choose **Edit**\.

1. From **Layer version**, select the layer version to change to\.

1. Choose **Update functions**\.

You cannot update functions' layer versions across AWS accounts\.

### Configuring layers with the API<a name="invocation-layers-api"></a>

To add layers to your function, use the update\-function\-configuration command\. The following example adds two layers: one from the same AWS account as the function, and one from a different account\.

```
aws lambda update-function-configuration --function-name my-function \
--layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3 \
arn:aws:lambda:us-east-2:111122223333:layer:their-layer:2
```

You should see output similar to the following:

```
{
    "FunctionName": "test-layers",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs16.x",
    "Role": "arn:aws:iam::123456789012:role/service-role/lambda-role",
    "Layers": [
        {
            "Arn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3",
            "CodeSize": 169
        },
        {
            "Arn": "arn:aws:lambda:us-east-2:111122223333:layer:their-layer:2",
            "CodeSize": 169
        }
    ],
    "RevisionId": "81cc64f5-5772-449a-b63e-12330476bcc4",
    ...
}
```

To specify the layer versions to use, you must provide the full Amazon Resource Name \(ARN\) of each layer version\. When you add layers to a function that already has layers, you overwrite the previous list of layers\. Be sure to include all layers every time that you update the layer configuration\. The order in which you add the layers is the order in which Lambda later extracts the layer content into the execution environment\.



To remove all layers, specify an empty list\.

```
aws lambda update-function-configuration --function-name my-function --layers []
```

The creator of a layer can delete a version of the layer\. If you're using that layer version in a function, your function continues to run as though the layer version still exists\. However, when you update the layer configuration, you must remove the reference to the deleted version\.

Layers are versioned, and the content of each layer version is immutable\. The layer owner can release a new layer version to provide updated content\. You can use the API to update the layer versions that your function uses\. 

**Update layer versions for your function**

To update one or more layer versions for your function, use the update\-function\-configuration command\. Use the `--layers` option with this command to include all of the layer versions for the function, even if you are updating one of the layer versions\. If the function already has layers, the new list overwrites the previous list\.

The following procedure steps assume that you have packaged the updated layer code into a local file named `layer.zip`\.

1. \(Optional\) If the new layer version is not published yet, publish the new version\.

   ```
    aws lambda publish-layer-version --layer-name my-layer --description "My layer" --license-info "MIT" \
   --zip-file  "fileb://layer.zip"  --compatible-runtimes python3.6 python3.7
   ```

1. \(Optional\) If the function has more than one layer, get the current layer versions associated with the function\.

   ```
   aws lambda get-function-configuration --function-name my-function  --query 'Layers[*].Arn' --output yaml
   ```

1. Add the new layer version to the function\. In the following example command, the function also has a layer version named `other-layer:5`:

   ```
   aws lambda update-function-configuration --function-name my-function \
   --layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:2 \        
               arn:aws:lambda:us-east-2:123456789012:layer:other-layer:5
   ```

## Accessing layer content from your function<a name="invocation-layers-accessing"></a>

If your Lambda function includes layers, Lambda extracts the layer contents into the `/opt` directory in the function execution environment\. Lambda extracts the layers in the order \(low to high\) listed by the function\. Lambda merges folders with the same name, so if the same file appears in multiple layers, the function uses the version in the last extracted layer\.

Each [Lambda runtime](lambda-runtimes.md) adds specific `/opt` directory folders to the PATH variable\. Your function code can access the layer content without the need to specify the path\. For more information about path settings in the Lambda execution environment, see [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.

## Finding layer information<a name="configuration-layers-finding"></a>

To find layers in your AWS account that are compatible with your Lambda function's runtime, use the list\-layers command\.

```
aws lambda list-layers --compatible-runtime python3.8
```

You should see output similar to the following:

```
{
    "Layers": [
        {
            "LayerName": "my-layer",
            "LayerArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer",
            "LatestMatchingVersion": {
                "LayerVersionArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:2",
                "Version": 2,
                "Description": "My layer",
                "CreatedDate": "2018-11-15T00:37:46.592+0000",
                "CompatibleRuntimes": [
                    "python3.6",
                    "python3.7",
                    "python3.8",
                ]
            }
        }
    ]
}
```

To list all layers in your account, you can omit the `--compatible-runtime` option\. The details in the response reflect the latest version of the layer\.

You can also get the latest version of a layer using the list\-layer\-versions command\.

```
aws lambda list-layer-versions --layer-name my-layer --query 'LayerVersions[0].LayerVersionArn'
```

## Adding layer permissions<a name="invocation-layers-permissions"></a>

To use a Lambda function with a layer, you need permission to call the [GetLayerVersion](API_GetLayerVersion.md) API operation on the layer version\. For functions in your AWS account, you can add this permission from your [user policy](access-control-identity-based.md)\.

To use a layer in another account, the owner of that account must grant your account permission in a [resource\-based policy](access-control-resource-based.md)\.

For examples, see [Granting layer access to other accounts](access-control-resource-based.md#permissions-resource-xaccountlayer)\.

## Using AWS SAM to add a layer to a function<a name="invocation-layers-cloudformation"></a>

To automate the creation and mapping of layers in your application, use the AWS Serverless Application Model \(AWS SAM\)\. The `AWS::Serverless::LayerVersion` resource type creates a layer version that you can reference from your Lambda function configuration\.

**Example [blank\-nodejs/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-nodejs/template.yml) – Serverless resources**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: An AWS Lambda application that calls the Lambda API.
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      CodeUri: function/.
      Description: Call the AWS Lambda API
      Timeout: 10
      # Function's execution role
      Policies:
        - AWSLambdaBasicExecutionRole
        - AWSLambda_ReadOnlyAccess
        - AWSXrayWriteOnlyAccess
      Tracing: Active
      Layers:
        - !Ref libs
  libs:
    Type: [AWS::Serverless::LayerVersion](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-layerversion.html)
    Properties:
      LayerName: blank-nodejs-lib
      Description: Dependencies for the blank sample app.
      ContentUri: lib/.
      CompatibleRuntimes:
        - nodejs12.x
```

When you update your dependencies and deploy, AWS SAM creates a new version of the layer and updates the mapping\.

## Sample applications<a name="invocation-layers-samples"></a>

The GitHub repository for this guide provides blank sample applications that demonstrate the use of layers for dependency management\.
+ **Node\.js** – [blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs)
+ **Python** – [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-python)
+ **Ruby** – [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-ruby)
+ **Java** – [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java)

For more information about the blank sample app, see [Blank function sample application for AWS Lambda](samples-blank.md)\. For other samples, see [Lambda sample applications](lambda-samples.md)\.
