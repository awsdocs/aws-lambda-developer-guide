# AWS Lambda Layers<a name="configuration-layers"></a>

You can configure your Lambda function to pull in additional code and content in the form of layers\. A layer is a ZIP archive that contains libraries, a [custom runtime](runtimes-custom.md), or other dependencies\. With layers, you can use libraries in your function without needing to include them in your deployment package\.

Layers let you keep your deployment package small, which makes development easier\. You can avoid errors that can occur when you install and package dependencies with your function code\. For Node\.js, Python, and Ruby functions, you can [develop your function code in the Lambda console](code-editor.md) as long as you keep your deployment package under 3 MB\.

**Note**  
A function can use up to 5 layers at a time\. The total unzipped size of the function and all layers can't exceed the unzipped deployment package size limit of 250 MB\. For more information, see [AWS Lambda Limits](limits.md)\.

You can create layers, or use layers published by AWS and other AWS customers\. Layers support [resource\-based policies](#configuration-layers-permissions) for granting layer usage permissions to specific AWS accounts, [AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/), or all accounts\.

Layers are extracted to the `/opt` directory in the function execution environment\. Each runtime looks for libraries in a different location under `/opt`, depending on the language\. [Structure your layer](#configuration-layers-path) so that function code can access libraries without additional configuration\.

You can also use AWS Serverless Application Model \(AWS SAM\) to manage layers and your function's layer configuration\. For instructions, see [Declaring Serverless Resources](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template.html) in the *AWS Serverless Application Model Developer Guide*\.

**Topics**
+ [Configuring a Function to Use Layers](#configuration-layers-using)
+ [Managing Layers](#configuration-layers-manage)
+ [Including Library Dependencies in a Layer](#configuration-layers-path)
+ [Layer Permissions](#configuration-layers-permissions)

## Configuring a Function to Use Layers<a name="configuration-layers-using"></a>

You can specify up to 5 layers in your function's configuration, during or after function creation\. You choose a specific version of a layer to use\. If you want to use a different version later, update your function's configuration\.

To add layers to your function, use the `update-function-configuration` command\. The following example adds two layers: one from the same account as the function, and one from a different account\.

```
$ aws lambda update-function-configuration --function-name my-function \
--layers arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3 \
arn:aws:lambda:us-east-2:210987654321:layer:their-layer:2
{
    "FunctionName": "test-layers",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs8.10",
    "Role": "arn:aws:iam::123456789012:role/service-role/lambda-role",
    "Handler": "index.handler",
    "CodeSize": 402,
    "Description": "",
    "Timeout": 5,
    "MemorySize": 128,
    "LastModified": "2018-11-14T22:47:04.542+0000",
    "CodeSha256": "kDHAEY62Ni3OovMwVO8tNvgbRoRa6IOOKqShm7bSWF4=",
    "Version": "$LATEST",
    "TracingConfig": {
        "Mode": "Active"
    },
    "RevisionId": "81cc64f5-5772-449a-b63e-12330476bcc4",
    "Layers": [
        {
            "Arn": "arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3",
            "CodeSize": 169
        },
        {
            "Arn": "arn:aws:lambda:us-east-2:210987654321:layer:their-layer:2",
            "CodeSize": 169
        }
    ]
}
```

You must specify the version of each layer to use by providing the full ARN of the layer version\. When you add layers to a function that already has layers, the previous list is overwritten by the new one\. Include all layers every time you update the layer configuration\. To remove all layers, specify an empty list\.

```
$ aws lambda update-function-configuration --function-name my-function --layers []
```

Your function can access the content of the layer during execution in the `/opt` directory\. Layers are applied in the order that's specified, merging any folders with the same name\. If the same file appears in multiple layers, the version in the last applied layer is used\.

The creator of a layer can delete the version of the layer that you're using\. When this happens, your function continues to run as though the layer version still existed\. However, when you update the layer configuration, you must remove the reference to the deleted version\.

## Managing Layers<a name="configuration-layers-manage"></a>

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
        "python3.7"
    ]
}
```

Each time you call `publish-layer-version`, you create a new version\. Functions that use the layer refer directly to a layer version\. You can [configure permissions](#configuration-layers-permissions) on an existing layer version, but to make any other changes, you must create a new version\.

 To find layers that are compatible with your function's runtime, use the `list-layers` command\.

```
$ aws lambda list-layers --compatible-runtime python3.7
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
                    "python3.7"
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
        "python3.7"
    ]
}
```

The link in the response lets you download the layer archive and is valid for 10 minutes\. To delete a layer version, use the `delete-layer-version` command\.

```
$ aws lambda delete-layer-version --layer-name my-layer --version-number 1
```

When you delete a layer version, you can no longer configure functions to use it\. However, any function that already uses the version continues to have access to it\. Version numbers are never re\-used for a layer name\.

## Including Library Dependencies in a Layer<a name="configuration-layers-path"></a>

You can move runtime dependencies out of your function code by placing them in a layer\. Lambda runtimes include paths in the `/opt` directory to ensure that your function code has access to libraries that are included in layers\.

To include libraries in a layer, place them in one of the folders supported by your runtime\.
+ **Node\.js** – `nodejs/node_modules`, `nodejs/node8/node_modules` \(`NODE_PATH`\)  
**Example AWS X\-Ray SDK for Node\.js**  

  ```
  xray-sdk.zip
  └ nodejs/node_modules/aws-xray-sdk
  ```
+ **Python** – `python`, `python/lib/python3.7/site-packages` \(site directories\)  
**Example Pillow**  

  ```
  pillow.zip
  │ python/PIL
  └ python/Pillow-5.3.0.dist-info
  ```
+ **Java** – `java/lib` \(classpath\)  
**Example Jackson**  

  ```
  jackson.zip
  └ java/lib/jackson-core-2.2.3.jar
  ```
+ **Ruby** – `ruby/gems/2.5.0` \(`GEM_PATH`\), `ruby/lib` \(`RUBY_LIB`\)  
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
+ **All** – `bin` \(`PATH`\), `lib` \(`LD_LIBRARY_PATH`\)  
**Example JQ**  

  ```
  jq.zip
  └ bin/jq
  ```

For more information about path settings in the Lambda execution environment, see [Environment Variables Available to Lambda Functions](lambda-environment-variables.md)\.

## Layer Permissions<a name="configuration-layers-permissions"></a>

Layer usage permissions are managed on the resource\. To configure a function with a layer, you need permission to call `GetLayerVersion` on the layer version\. For functions in your account, you can get this permission from your [user policy](access-control-identity-based.md) or from the function's [resource\-based policy](access-control-resource-based.md)\. To use a layer in another account, you need permission on your user policy, and the owner of the other account must grant your account permission with a resource\-based policy\.

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy with the `add-layer-version-permission` command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
$ aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 210987654321 --version-number 1 --output text
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::210987654321:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions only apply to a single version of a layer\. Repeat the procedure each time you create a new layer version\.

For more examples, see [Granting Layer Access to Other Accounts](access-control-resource-based.md#permissions-resource-xaccountlayer)\.