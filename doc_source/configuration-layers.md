# Creating and sharing Lambda layers<a name="configuration-layers"></a>

Lambda [layers](gettingstarted-concepts.md#gettingstarted-concepts-layer) provide a convenient way to package libraries and other dependencies that you can use with your Lambda functions\. Using layers reduces the size of uploaded deployment archives and makes it faster to deploy your code\.

A layer is a \.zip file archive that can contain additional code or data\. A layer can contain libraries, a [custom runtime](runtimes-custom.md), data, or configuration files\. Layers promote code sharing and separation of responsibilities so that you can iterate faster on writing business logic\.

You can use layers only with Lambda functions [deployed as a \.zip file archive](gettingstarted-package.md#gettingstarted-package-zip)\. For functions [defined as a container image](images-create.md), you package your preferred runtime and all code dependencies when you create the container image\. For more information, see [Working with Lambda layers and extensions in container images](http://aws.amazon.com/blogs/compute/working-with-lambda-layers-and-extensions-in-container-images/) on the AWS Compute Blog\.

You can create layers using the Lambda console, the Lambda API, AWS CloudFormation, or the AWS Serverless Application Model \(AWS SAM\)\. For more information about creating layers with AWS SAM, see [Working with layers](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-layers.html) in the *AWS Serverless Application Model Developer Guide*\.

**Note**  
For Node\.js runtimes, Lambda doesn't currently support ES module dependencies in layers\.

**Topics**
+ [Creating layer content](#configuration-layers-upload)
+ [Compiling the \.zip file archive for your layer](#configuration-layers-compile)
+ [Including library dependencies in a layer](#configuration-layers-path)
+ [Language\-specific instructions](#configuration-layers-zip-cli)
+ [Creating a layer](#configuration-layers-create)
+ [Deleting a layer version](#configuration-layers-delete)
+ [Configuring layer permissions](#configuration-layers-permissions)
+ [Using AWS CloudFormation with layers](#invocation-layers-cloudformation)

## Creating layer content<a name="configuration-layers-upload"></a>

When you create a layer, you must bundle all its content into a \.zip file archive\. You upload the \.zip file archive to your layer from Amazon Simple Storage Service \(Amazon S3\) or your local machine\. Lambda extracts the layer contents into the `/opt` directory when setting up the execution environment for the function\.

## Compiling the \.zip file archive for your layer<a name="configuration-layers-compile"></a>

You build your layer code into a \.zip file archive using the same procedure that you would use for a function deployment package\. If your layer includes any native code libraries, you must compile and build these libraries using a Linux development machine so that the binaries are compatible with [Amazon Linux](lambda-runtimes.md)\. 

When you create a layer, you can specify whether the layer is compatible with one or both of the instruction set architectures\. You may need to set specific compile flags to build a layer that is compatible with the `arm64` architecture\.

One way to ensure that you package libraries correctly for Lambda is to use [AWS Cloud9](http://aws.amazon.com/cloud9/)\. For more information, see [Using Lambda layers to simplify your development process](http://aws.amazon.com/blogs/compute/using-lambda-layers-to-simplify-your-development-process/) on the AWS Compute Blog\.

## Including library dependencies in a layer<a name="configuration-layers-path"></a>

For each [Lambda runtime](lambda-runtimes.md), the PATH variable includes specific folders in the `/opt` directory\. If you define the same folder structure in your layer \.zip file archive, your function code can access the layer content without the need to specify the path\.

The following table lists the folder paths that each runtime supports\.


**Layer paths for each Lambda runtime**  
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)

The following examples show how you can structure the folders in your layer \.zip archive\.

------
#### [ Node\.js ]

**Example file structure for the AWS X\-Ray SDK for Node\.js**  

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

**Example file structure for the JSON gem**  

```
json.zip
└ ruby/gems/2.7.0/
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

**Example file structure for the Jackson JAR file**  

```
jackson.zip
└ java/lib/jackson-core-2.2.3.jar
```

------
#### [ All ]

**Example file structure for the jq library**  

```
jq.zip
└ bin/jq
```

------

For more information about path settings in the Lambda execution environment, see [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.

## Language\-specific instructions<a name="configuration-layers-zip-cli"></a>

 For language\-specific instructions on how to create a \.zip file archive, see the following topics\.

------
#### [ Node\.js ]

[Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md) 

------
#### [ Python ]

 [Deploy Python Lambda functions with \.zip file archives](python-package.md) 

------
#### [ Ruby ]

 [Deploy Ruby Lambda functions with \.zip file archives](ruby-package.md) 

------
#### [ Java ]

 [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md) 

------
#### [ Go ]

 [Deploy Go Lambda functions with \.zip file archives](golang-package.md) 

------
#### [ C\# ]

 [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md) 

------
#### [ PowerShell ]

 [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md) 

------

## Creating a layer<a name="configuration-layers-create"></a>

You can create new layers using the Lambda console or the Lambda API\.

Layers can have one or more version\. When you create a layer, Lambda sets the layer version to version 1\. You can configure permissions on an existing layer version, but to update the code or make other configuration changes, you must create a new version of the layer\.

**To create a layer \(console\)**

1. Open the [Layers page](https://console.aws.amazon.com/lambda/home#/layers) of the Lambda console\.

1. Choose **Create layer**\.

1. Under **Layer configuration**, for **Name**, enter a name for your layer\.

1. \(Optional\) For **Description**, enter a description for your layer\.

1. To upload your layer code, do one of the following:
   + To upload a \.zip file from your computer, choose **Upload a \.zip file**\. Then, choose **Upload** to select your local \.zip file\.
   + To upload a file from Amazon S3, choose **Upload a file from Amazon S3**\. Then, for **Amazon S3 link URL**, enter a link to the file\.

1. \(Optional\) For **Compatible instruction set architectures**, choose one value or both values\.

1. \(Optional\) For **Compatible runtimes**, choose up to 15 runtimes\.

1. \(Optional\) For **License**, enter any necessary license information\.

1. Choose **Create**\.

**To create a layer \(API\)**

To create a layer, use the publish\-layer\-version command with a name, description, \.zip file archive, a list of [runtimes](lambda-runtimes.md) and a list of architectures that are compatible with the layer\. The runtimes and architecture parameters are optional\.

```
aws lambda publish-layer-version --layer-name my-layer --description "My layer"  \ 
--license-info "MIT" --content S3Bucket=lambda-layers-us-east-2-123456789012,S3Key=layer.zip \
 --compatible-runtimes python3.6 python3.7 python3.8
  --compatible-architectures "arm64" "x86_64"
```

You should see output similar to the following:

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
    "CompatibleArchitectures": [
        "arm64",
        "x86_64"
     ],
    "LicenseInfo": "MIT",
    "CompatibleRuntimes": [
        "python3.6",
        "python3.7",
        "python3.8"
    ]
}
```

**Note**  
Each time that you call `publish-layer-version`, you create a new version of the layer\.

## Deleting a layer version<a name="configuration-layers-delete"></a>

To delete a layer version, use the delete\-layer\-version command\.

```
aws lambda delete-layer-version --layer-name my-layer --version-number 1
```

When you delete a layer version, you can no longer configure a Lambda function to use it\. However, any function that already uses the version continues to have access to it\. Version numbers are never reused for a layer name\.

## Configuring layer permissions<a name="configuration-layers-permissions"></a>

By default, a layer that you create is private to your AWS account\. However, you can optionally share the layer with other accounts or make it public\.

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy using the add\-layer\-version\-permission command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 111122223333 --version-number 1 --output text
```

You should see output similar to the following:

```
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::111122223333:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions apply only to a single layer version\. Repeat the process each time that you create a new layer version\.

For more examples, see [Granting layer access to other accounts](access-control-resource-based.md#permissions-resource-xaccountlayer)\.

## Using AWS CloudFormation with layers<a name="invocation-layers-cloudformation"></a>

You can use AWS CloudFormation to create a layer and associate the layer with your Lambda function\. The following example template creates a layer named **blank\-nodejs\-lib** and attaches the layer to the Lambda function using the **Layers** property\.

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: A Lambda application that calls the Lambda API.
Resources:
  function:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      CodeUri: function/.
      Description: Call the Lambda API
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
    Type: AWS::Lambda::LayerVersion
    Properties:
      LayerName: blank-nodejs-lib
      Description: Dependencies for the blank sample app.
      Content:
        S3Bucket: my-bucket-region-123456789012
        S3Key: layer.zip
      CompatibleRuntimes:
        - nodejs12.x
```