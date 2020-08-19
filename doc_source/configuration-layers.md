# AWS Lambda layers<a name="configuration-layers"></a>

You can configure your Lambda function to pull in additional code and content in the form of layers\. A layer is a ZIP archive that contains libraries, a [custom runtime](runtimes-custom.md), or other dependencies\. With layers, you can use libraries in your function without needing to include them in your deployment package\.

Layers let you keep your deployment package small, which makes development easier\. You can avoid errors that can occur when you install and package dependencies with your function code\. For Node\.js, Python, and Ruby functions, you can [develop your function code in the Lambda console](code-editor.md) as long as you keep your deployment package under 3 MB\.

**Note**  
A function can use up to 5 layers at a time\. The total unzipped size of the function and all layers can't exceed the unzipped deployment package size limit of 250 MB\. For more information, see [AWS Lambda quotas](gettingstarted-limits.md)\.

You can create layers, or use layers published by AWS and other AWS customers\. Layers support [resource\-based policies](#configuration-layers-permissions) for granting layer usage permissions to specific AWS accounts, [AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/), or all accounts\.

Layers are extracted to the `/opt` directory in the function execution environment\. Each runtime looks for libraries in a different location under `/opt`, depending on the language\. [Structure your layer](#configuration-layers-path) so that function code can access libraries without additional configuration\.

You can also use AWS Serverless Application Model \(AWS SAM\) to manage layers and your function's layer configuration\. For instructions, see [Declaring serverless resources](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template.html) in the *AWS Serverless Application Model Developer Guide*\.

**Topics**
+ [Configuring a function to use layers](#configuration-layers-using)
+ [Managing layers](#configuration-layers-manage)
+ [Including library dependencies in a layer](#configuration-layers-path)
+ [Layer permissions](#configuration-layers-permissions)
+ [AWS CloudFormation and AWS SAM](#configuration-layers-cloudformation)
+ [Sample applications](#configuration-layers-samples)

## Configuring a function to use layers<a name="configuration-layers-using"></a>

You can specify up to 5 layers in your function's configuration, during or after function creation\. You choose a specific version of a layer to use\. If you want to use a different version later, update your function's configuration\.

To add layers to your function, use the `update-function-configuration` command\. The following example adds two layers: one from the same account as the function, and one from a different account\.

```
$ aws lambda update-function-configuration --function-name my-function \
--layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3 \
arn:aws:lambda:us-east-2:210987654321:layer:their-layer:2
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

You must specify the version of each layer to use by providing the full ARN of the layer version\. When you add layers to a function that already has layers, the previous list is overwritten by the new one\. Include all layers every time you update the layer configuration\. To remove all layers, specify an empty list\.

```
$ aws lambda update-function-configuration --function-name my-function --layers []
```

Your function can access the content of the layer during execution in the `/opt` directory\. Layers are applied in the order that's specified, merging any folders with the same name\. If the same file appears in multiple layers, the version in the last applied layer is used\.

The creator of a layer can delete the version of the layer that you're using\. When this happens, your function continues to run as though the layer version still existed\. However, when you update the layer configuration, you must remove the reference to the deleted version\.

## Managing layers<a name="configuration-layers-manage"></a>

To create a layer, use the `publish-layer-version` command with a name, description, ZIP archive, and a list of [runtimes](lambda-runtimes.md) that are compatible with the layer\. The list of runtimes is optional, but it makes the layer easier to discover\.

```
$ aws lambda publish-layer-version --layer-name my-layer --description "My layer" --license-info "MIT" \
--content S3Bucket=lambda-layers-us-east-2-123456789012,S3Key=layer.zip --compatible-runtimes python3.6 python3.7
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

Each time you call `publish-layer-version`, you create a new version\. Functions that use the layer refer directly to a layer version\. You can [configure permissions](#configuration-layers-permissions) on an existing layer version, but to make any other changes, you must create a new version\.

 To find layers that are compatible with your function's runtime, use the `list-layers` command\.

```
$ aws lambda list-layers --compatible-runtime python3.8
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

You can omit the runtime option to list all layers\. The details in the response reflect the latest version of the layer\. See all the versions of a layer with `list-layer-versions`\. To see more information about a version, use `get-layer-version`\.

```
$ aws lambda get-layer-version --layer-name my-layer --version-number 2
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

The link in the response lets you download the layer archive and is valid for 10 minutes\. To delete a layer version, use the `delete-layer-version` command\.

```
$ aws lambda delete-layer-version --layer-name my-layer --version-number 1
```

When you delete a layer version, you can no longer configure functions to use it\. However, any function that already uses the version continues to have access to it\. Version numbers are never re\-used for a layer name\.

## Including library dependencies in a layer<a name="configuration-layers-path"></a>

You can move runtime dependencies out of your function code by placing them in a layer\. Lambda runtimes include paths in the `/opt` directory to ensure that your function code has access to libraries that are included in layers\.

To include libraries in a layer, place them in one of the folders supported by your runtime, or modify that path variable for your language\.
+ **Node\.js** – `nodejs/node_modules`, `nodejs/node8/node_modules` \(`NODE_PATH`\)  
**Example AWS X\-Ray SDK for Node\.js**  

  ```
  xray-sdk.zip
  └ nodejs/node_modules/aws-xray-sdk
  ```
+ **Python** – `python`, `python/lib/python3.8/site-packages` \(site directories\)  
**Example Pillow**  

  ```
  pillow.zip
  │ python/PIL
  └ python/Pillow-5.3.0.dist-info
  ```
+ **Ruby** – `ruby/gems/2.5.0` \(`GEM_PATH`\), `ruby/lib` \(`RUBYLIB`\)  
**Example JSON**  

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
+ **Java** – `java/lib` \(classpath\)  
**Example Jackson**  

  ```
  jackson.zip
  └ java/lib/jackson-core-2.2.3.jar
  ```
+ **All** – `bin` \(`PATH`\), `lib` \(`LD_LIBRARY_PATH`\)  
**Example JQ**  

  ```
  jq.zip
  └ bin/jq
  ```

For more information about path settings in the Lambda execution environment, see [Runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.

## Layer permissions<a name="configuration-layers-permissions"></a>

Layer usage permissions are managed on the resource\. To configure a function with a layer, you need permission to call `GetLayerVersion` on the layer version\. For functions in your account, you can get this permission from your [user policy](access-control-identity-based.md) or from the function's [resource\-based policy](access-control-resource-based.md)\. To use a layer in another account, you need permission on your user policy, and the owner of the other account must grant your account permission with a resource\-based policy\.

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy with the `add-layer-version-permission` command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
$ aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 210987654321 --version-number 1 --output text
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::210987654321:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions only apply to a single version of a layer\. Repeat the procedure each time you create a new layer version\.

For more examples, see [Granting layer access to other accounts](access-control-resource-based.md#permissions-resource-xaccountlayer)\.

## AWS CloudFormation and AWS SAM<a name="configuration-layers-cloudformation"></a>

Use the AWS Serverless Application Model \(AWS SAM\) in your AWS CloudFormation templates to automate the creation and mapping of layers in your application\. The `AWS::Serverless::LayerVersion` resource type creates a layer version that you can reference from your function configuration\.

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
        - AWSLambdaReadOnlyAccess
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

When you update your dependencies and deploy, AWS SAM creates a new version of the layer and updates the mapping\. If you deploy changes to your code without modifying your dependencies, AWS SAM skips the layer update, saving upload time\.

## Sample applications<a name="configuration-layers-samples"></a>

The GitHub repository for this guide provides [sample applications](lambda-samples.md) that demonstrate the use of layers for dependency management\.
+ **Node\.js** – [blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-nodejs)
+ **Python** – [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-python)
+ **Ruby** – [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-ruby)
+ **Java** – [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java)

For more information about the blank sample app, see [Blank function sample application for AWS Lambda](samples-blank.md)\.