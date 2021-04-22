# Lambda layers<a name="configuration-layers"></a>

You use layers with functions that use \.zip file archives for deployment\. For a function defined as a container image, you package your preferred runtime and all code dependencies when you [create the container image](runtimes-custom.md)\. For more information, see [ Working with Lambda layers and extensions in container images]( https://aws.amazon.com/blogs/compute/working-with-lambda-layers-and-extensions-in-container-images/) on the AWS Compute Blog\.

You can use Lambda layers to share code in your functions\. A *layer* contains code libraries, a [custom runtime](runtimes-custom.md), or other dependencies\. Layers help to reduce your \.zip archive deployment package size, because layers are not included in the deployment package\. 

A function can use up to five layers at a time\. The total unzipped size of the function and all layers can't exceed the unzipped deployment package size limit of 250 MB\. For more information, see [Lambda quotas](gettingstarted-limits.md)\.

You can create your own layers, or use layers that AWS or an AWS customer has published\. Layers support [resource\-based policies](#configuration-layers-permissions) for granting layer usage permissions to specific AWS accounts, [AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_introduction.html), or all accounts\.

You can also use the AWS Serverless Application Model \(AWS SAM\) to manage layers and your function's layer configuration\. For instructions, see [Working with layers](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-layers.html) in the *AWS Serverless Application Model Developer Guide*\.

**Topics**
+ [Create layers](#configuration-layers-create)
+ [Manage layers](#configuration-layers-manage)
+ [Include library dependencies in a layer](#configuration-layers-path)
+ [Layer permissions](#configuration-layers-permissions)
+ [Configure a function to use layers](#configuration-layers-using)
+ [Add an updated layer version to your function](#configuration-layers-script)
+ [AWS CloudFormation and AWS SAM](#configuration-layers-cloudformation)
+ [Sample applications](#configuration-layers-samples)

## Create layers<a name="configuration-layers-create"></a>

When you create a new layer, Lambda creates the layer as version 1\. You can [configure permissions](#configuration-layers-permissions) on an existing layer version, but to make any other changes, you must create a new version\.

**To create a layer \(console\)**

1. Open the [Layers page](https://console.aws.amazon.com/lambda/home#/layers) of the Lambda console\.

1. Choose **Create layer**\.

1. For **Name**, enter a name for your layer\.

1. \(Optional\) For **Description**, enter a description for your layer\.

1. To upload your layer code, do one of the following:
   + To upload a \.zip file from your computer, choose **Upload a \.zip file**, choose your \.zip file, and then choose **Open**\.
   + To upload a file from Amazon Simple Storage Service \(Amazon S3\), choose **Upload a file from Amazon S3**\. Then, for **Amazon S3 link URL**, enter a link to the file\.

1. \(Optional\) For **Compatible runtimes**, choose up to 15 runtimes\.

1. \(Optional\) For **License**, enter any necessary license information\.

1. Choose **Create**\.

**To create a layer \(API\)**

To create a layer, use the `publish-layer-version` command with a name, description, \.zip file archive, and a list of [runtimes](lambda-runtimes.md) that are compatible with the layer\. The list of runtimes is optional, but it makes the layer easier to discover\.

```
aws lambda publish-layer-version --layer-name my-layer --description "My layer"  \ 
--license-info "MIT" --content S3Bucket=lambda-layers-us-east-2-123456789012,S3Key=layer.zip \
 --compatible-runtimes python3.6 python3.7 python3.8
```

You should see the following output:

```
{
    "Content": {
        "Location": "https://awslambda-us-east-2-layers.s3.us-east-2.amazonaws.com/snapshots/123456789012/my-layer-4aaa2fbb-ff77-4b0a-ad92-5b78a716a96a?versionId=27iWyA73cCAYqyH...",
        "CodeSha256": "tv9jJO+rPbXUUXuRKi7CwHzKtLDkDRJLB3cC3Z/ouXo=",
        "CodeSize": 169
    },
    "LayerArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer",
    "LayerVersionArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:1",
    "Description": "My layer",
    "CreatedDate": "2018-11-14T23:03:52.894+0000",
    "Version": 1,
    "LicenseInfo": "MIT",
    "CompatibleRuntimes": [
        "python3.6",
        "python3.7",
        "python3.8"
    ]
}
```

Each time that you call `publish-layer-version`, you create a new version of the layer\.

## Manage layers<a name="configuration-layers-manage"></a>

 To find layers that are compatible with your Lambda function's runtime, use the `list-layers` command\.

```
aws lambda list-layers --compatible-runtime python3.8
```

You should see the following output:

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

To list all layers, you can omit the runtime option\. The details in the response reflect the latest version of the layer\. See all the versions of a layer with `list-layer-versions`\. To see more information about a version, use `get-layer-version`\.

```
aws lambda get-layer-version --layer-name my-layer --version-number 2
```

You should see the following output:

```
{
    "Content": {
        "Location": "https://awslambda-us-east-2-layers.s3.us-east-2.amazonaws.com/snapshots/123456789012/my-layer-91e9ea6e-492d-4100-97d5-a4388d442f3f?versionId=GmvPV.309OEpkfN...",
        "CodeSha256": "tv9jJO+rPbXUUXuRKi7CwHzKtLDkDRJLB3cC3Z/ouXo=",
        "CodeSize": 169
    },
    "LayerArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer",
    "LayerVersionArn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:2",
    "Description": "My layer",
    "CreatedDate": "2018-11-15T00:37:46.592+0000",
    "Version": 2,
    "CompatibleRuntimes": [
        "python3.6",
        "python3.7",
        "python3.8"
    ]
}
```

The link in the response lets you download the layer archive\. The link is valid for 10 minutes\.

To delete a layer version, use the `delete-layer-version` command\.

```
aws lambda delete-layer-version --layer-name my-layer --version-number 1
```

When you delete a layer version, you can no longer configure functions to use it\. However, any function that already uses the version continues to have access to it\. Version numbers are never reused for a layer name\.

## Include library dependencies in a layer<a name="configuration-layers-path"></a>

Each Lambda runtime adds specific `/opt` directory folders to the PATH variable\. If the layer uses the same folder structure, your function code can access the layer content without the need to specify the path\. 

The following table lists the folder paths that each runtime supports\. 


**Layer paths for each Lambda runtime**  
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)

The following examples show how you can structure the folders in your layer \.zip archive\.

------
#### [ Node\.js ]

**Example file structure for AWS X\-Ray SDK**  

```
xray-sdk.zip
└ nodejs/node_modules/aws-xray-sdk
```

------
#### [ Python ]

**Example file structure for the Pillow library**  

```
pillow.zip
│ python/PIL
└ python/Pillow-5.3.0.dist-info
```

------
#### [ Ruby ]

**Example file structure for JSON gem**  

```
json.zip
└ ruby/gems/2.5.0/
               | build_info
               | cache
               | doc
               | extensions
               | gems
               | └ json-2.1.0
               └ specifications
                 └ json-2.1.0.gemspec
```

------
#### [ Java ]

**Example file structure for Jackson jar file**  

```
jackson.zip
└ java/lib/jackson-core-2.2.3.jar
```

------
#### [ All ]

**Example file structure for JQ library**  

```
jq.zip
└ bin/jq
```

------

For more information about path settings in the Lambda execution environment, see [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.

## Layer permissions<a name="configuration-layers-permissions"></a>

To configure a Lambda function with a layer, you need permission to call `GetLayerVersion` on the layer version\. For functions in your account, you can get this permission from your [user policy](access-control-identity-based.md)\. 

To use a layer in another account, the owner of the other account must grant your account permission in a [resource\-based policy](access-control-resource-based.md)\. 

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy with the `add-layer-version-permission` command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 210987654321 --version-number 1 --output text
```

You should see the following output:

```
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::210987654321:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions apply only to a single version of a layer\. Repeat the process each time that you create a new layer version\.

For more examples, see [Granting layer access to other accounts](access-control-resource-based.md#permissions-resource-xaccountlayer)\.

## Configure a function to use layers<a name="configuration-layers-using"></a>

You can specify up to five layers in your Lambda function's configuration, during or after function creation\. You choose a specific version of a layer to use\. If you want to use a different version later, you can update your function's configuration\.

To add layers to your function, use the `update-function-configuration` command\. The following example adds two layers: one from the same account as the function, and one from a different account\.

```
aws lambda update-function-configuration --function-name my-function \
--layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3 \
arn:aws:lambda:us-east-2:210987654321:layer:their-layer:2
```

You should see the following output:

```
{
    "FunctionName": "test-layers",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs12.x",
    "Role": "arn:aws:iam::123456789012:role/service-role/lambda-role",
    "Layers": [
        {
            "Arn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3",
            "CodeSize": 169
        },
        {
            "Arn": "arn:aws:lambda:us-east-2:210987654321:layer:their-layer:2",
            "CodeSize": 169
        }
    ],
    "RevisionId": "81cc64f5-5772-449a-b63e-12330476bcc4",
    ...
}
```

You must specify the version of each layer to use by providing the full Amazon Resource Name \(ARN\) of the layer version\. When you add layers to a function that already has layers, the new list overwrites the previous list\. Include all layers every time that you update the layer configuration\. To remove all layers, specify an empty list\.

```
aws lambda update-function-configuration --function-name my-function --layers []
```

While your function is running, it can access the content of the layer in the `/opt` directory\. Layers are applied in the order that's specified, merging any folders with the same name\. If the same file appears in multiple layers, the version in the last applied layer is used\.

The creator of a layer can delete the version of the layer that you're using\. When this happens, your function continues to run as though the layer version still exists\. However, when you update the layer configuration, you must remove the reference to the deleted version\.

## Add an updated layer version to your function<a name="configuration-layers-script"></a>

 Use the `update-function-configuration` command to add an updated layer version to your function\. 

The `layers` parameter for this command is a list of all of the layer versions to add to the function\. If the function already has layers, the new list overwrites the previous list\. If you want to update only one of the layer versions, the `layers` parameter needs to include the ARNs of the existing layer versions\. 

The following procedure assumes that you have packaged the updated layer code into a local file named `layer.zip`\. 

**Add an updated layer version to your function**

1. \(Optional\) If the new layer version is not published yet, publish the new version\.

   ```
    aws lambda publish-layer-version --layer-name my-layer --description "My layer" --license-info "MIT" \
   --zip-file  "fileb://layer.zip"  --compatible-runtimes python3.6 python3.7
   ```

1. \(Optional\) If the function has more than one layer, get the current layer versions associated with the function\.

   ```
   aws lambda get-function-config --function-name my-function --query 'Layers[*].Arn' --output yaml
   ```

   \(Optional\) You can also get the latest version of a layer based on the layer name\.

   ```
   aws lambda list-layer-versions --layer-name my-layer --query 'LayerVersions[0].LayerVersionArn'
   ```

1. Add the new layer version to the function\. In the following example command, the function also has a layer version named `other-layer:5`\.

   ```
   aws lambda update-function-configuration --function-name my-function \
   --layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:2 \        
               arn:aws:lambda:us-east-2:123456789012:layer:other-layer:5
   ```

## AWS CloudFormation and AWS SAM<a name="configuration-layers-cloudformation"></a>

To automate the creation and mapping of layers in your application, use AWS SAM in your AWS CloudFormation templates\. The `AWS::Serverless::LayerVersion` resource type creates a layer version that you can reference from your Lambda function configuration\.

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

## Sample applications<a name="configuration-layers-samples"></a>

The GitHub repository for this guide provides [sample applications](lambda-samples.md) that demonstrate the use of layers for dependency management\.
+ **Node\.js** – [blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs)
+ **Python** – [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-python)
+ **Ruby** – [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-ruby)
+ **Java** – [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java)

For more information about the blank sample app, see [Blank function sample application for AWS Lambda](samples-blank.md)\.