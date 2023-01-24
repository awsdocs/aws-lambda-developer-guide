# Configuring code signing for AWS Lambda<a name="configuration-codesigning"></a>

Code signing for AWS Lambda helps to ensure that only trusted code runs in your Lambda functions\. When you enable code signing for a function, Lambda checks every code deployment and verifies that the code package is signed by a trusted source\.

**Note**  
Functions defined as container images do not support code signing\.

To verify code integrity, use [AWS Signer](https://docs.aws.amazon.com/signer/latest/developerguide/Welcome.html) to create digitally signed code packages for functions and layers\. When a user attempts to deploy a code package, Lambda performs validation checks on the code package before accepting the deployment\. Because code signing validation checks run at deployment time, there is no performance impact on function execution\.

You also use AWS Signer to create *signing profiles*\. You use a signing profile to create the signed code package\. Use AWS Identity and Access Management \(IAM\) to control who can sign code packages and create signing profiles\. For more information, see [Authentication and Access Control](https://docs.aws.amazon.com/signer/latest/developerguide/accessctrl-toplevel.html) in the *AWS Signer Developer Guide*\.

To enable code signing for a function, you create a *code signing configuration* and attach it to the function\. A code signing configuration defines a list of allowed signing profiles and the policy action to take if any of the validation checks fail\.

Lambda layers follow the same signed code package format as function code packages\. When you add a layer to a function that has code signing enabled, Lambda checks that the layer is signed by an allowed signing profile\. When you enable code signing for a function, all layers that are added to the function must also be signed by one of the allowed signing profiles\.

Use IAM to control who can create code signing configurations\. Typically, you allow only specific administrative users to have this ability\. Additionally, you can set up IAM policies to enforce that developers only create functions that have code signing enabled\.

You can configure code signing to log changes to AWS CloudTrail\. Successful and blocked deployments to functions are logged to CloudTrail with information about the signature and validation checks\.

You can configure code signing for your functions using the Lambda console, the AWS Command Line Interface \(AWS CLI\), AWS CloudFormation, and the AWS Serverless Application Model \(AWS SAM\)\.

There is no additional charge for using AWS Signer or code signing for AWS Lambda\.

**Topics**
+ [Signature validation](#config-codesigning-valid)
+ [Configuration prerequisites](#config-codesigning-prereqs)
+ [Creating code signing configurations](#config-codesigning-config-console)
+ [Updating a code signing configuration](#config-codesigning-config-update)
+ [Deleting a code signing configuration](#config-codesigning-config-delete)
+ [Enabling code signing for a function](#config-codesigning-function-console)
+ [Configuring IAM policies](#config-codesigning-policies)
+ [Configuring code signing with the Lambda API](#config-codesigning-api)

## Signature validation<a name="config-codesigning-valid"></a>

Lambda performs the following validation checks when you deploy a signed code package to your function:

1. Integrity – Validates that the code package has not been modified since it was signed\. Lambda compares the hash of the package with the hash from the signature\.

1. Expiry – Validates that the signature of the code package has not expired\.

1. Mismatch – Validates that the code package is signed with one of the allowed signing profiles for the Lambda function\. A mismatch also occurs if a signature is not present\.

1. Revocation – Validates that the signature of the code package has not been revoked\.

The signature validation policy defined in the code signing configuration determines which of the following actions Lambda takes if any of the validation checks fail:
+ Warn – Lambda allows the deployment of the code package, but issues a warning\. Lambda issues a new Amazon CloudWatch metric and also stores the warning in the CloudTrail log\.
+ Enforce – Lambda issues a warning \(the same as for the Warn action\) and blocks the deployment of the code package\.

You can configure the policy for the expiry, mismatch, and revocation validation checks\. Note that you cannot configure a policy for the integrity check\. If the integrity check fails, Lambda blocks deployment\.

## Configuration prerequisites<a name="config-codesigning-prereqs"></a>

Before you can configure code signing for a Lambda function, use AWS Signer to do the following:
+ Create one or more signing profiles\.
+ Use a signing profile to create a signed code package for your function\.

For more information, see [Creating Signing Profiles \(Console\)](https://docs.aws.amazon.com/signer/latest/developerguide/ConsoleLambda.html) in the *AWS Signer Developer Guide*\.

## Creating code signing configurations<a name="config-codesigning-config-console"></a>

A code signing configuration defines a list of allowed signing profiles and the signature validation policy\.

**To create a code signing configuration \(console\)**

1. Open the [Code signing configurations page](https://console.aws.amazon.com/lambda/home#/code-signing-configurations) of the Lambda console\.

1. Choose **Create configuration**\.

1. For **Description**, enter a descriptive name for the configuration\.

1. Under **Signing profiles**, add up to 20 signing profiles to the configuration\.

   1. For **Signing profile version ARN**, choose a profile version's Amazon Resource Name \(ARN\), or enter the ARN\.

   1. To add an additional signing profile, choose **Add signing profiles**\.

1. Under **Signature validation policy**, choose **Warn** or **Enforce**\.

1. Choose **Create configuration**\.

## Updating a code signing configuration<a name="config-codesigning-config-update"></a>

When you update a code signing configuration, the changes impact the future deployments of functions that have the code signing configuration attached\.

**To update a code signing configuration \(console\)**

1. Open the [Code signing configurations page](https://console.aws.amazon.com/lambda/home#/code-signing-configurations) of the Lambda console\.

1. Select a code signing configuration to update, and then choose **Edit**\.

1. For **Description**, enter a descriptive name for the configuration\.

1. Under **Signing profiles**, add up to 20 signing profiles to the configuration\.

   1. For **Signing profile version ARN**, choose a profile version's Amazon Resource Name \(ARN\), or enter the ARN\.

   1. To add an additional signing profile, choose **Add signing profiles**\.

1. Under **Signature validation policy**, choose **Warn** or **Enforce**\.

1. Choose **Save changes**\.

## Deleting a code signing configuration<a name="config-codesigning-config-delete"></a>

You can delete a code signing configuration only if no functions are using it\.

**To delete a code signing configuration \(console\)**

1. Open the [Code signing configurations page](https://console.aws.amazon.com/lambda/home#/code-signing-configurations) of the Lambda console\.

1. Select a code signing configuration to delete, and then choose **Delete**\.

1. To confirm, choose **Delete** again\.

## Enabling code signing for a function<a name="config-codesigning-function-console"></a>

To enable code signing for a function, you associate a code signing configuration with the function\.

**To associate a code signing configuration with a function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function for which you want to enable code signing\.

1. Under **Code signing configuration**, choose **Edit**\.

1. In **Edit code signing**, choose a code signing configuration for this function\.

1. Choose **Save**\.

## Configuring IAM policies<a name="config-codesigning-policies"></a>

To grant permission for a user to access the [code signing API operations](#config-codesigning-api), attach one or more policy statements to the user policy\. For more information about user policies, see [Identity\-based IAM policies for Lambda](access-control-identity-based.md)\.

The following example policy statement grants permission to create, update, and retrieve code signing configurations\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
          "lambda:CreateCodeSigningConfig",
          "lambda:UpdateCodeSigningConfig",
          "lambda:GetCodeSigningConfig"
        ],
      "Resource": "*" 
    }
  ]
}
```

Administrators can use the `CodeSigningConfigArn` condition key to specify the code signing configurations that developers must use to create or update your functions\.

The following example policy statement grants permission to create a function\. The policy statement includes a `lambda:CodeSigningConfigArn` condition to specify the allowed code signing configuration\. Lambda blocks any `CreateFunction` API request if its `CodeSigningConfigArn` parameter is missing or does not match the value in the condition\. 

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowReferencingCodeSigningConfig",
      "Effect": "Allow",
      "Action": [
          "lambda:CreateFunction",
        ],
      "Resource": "*",
      "Condition": {
          "StringEquals": {
              "lambda:CodeSigningConfigArn":  
                  “arn:aws:lambda:us-west-2:123456789012:code-signing-config:csc-0d4518bd353a0a7c6”
          }
      }
    }
  ]
}
```

## Configuring code signing with the Lambda API<a name="config-codesigning-api"></a>

To manage code signing configurations with the AWS CLI or AWS SDK, use the following API operations:
+ [ListCodeSigningConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListCodeSigningConfigs.html)
+ [CreateCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateCodeSigningConfig.html)
+ [GetCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetCodeSigningConfig.html)
+ [UpdateCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateCodeSigningConfig.html)
+ [DeleteCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteCodeSigningConfig.html)

To manage the code signing configuration for a function, use the following API operations:
+  [CreateFunction](API_CreateFunction.md)
+ [GetFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionCodeSigningConfig.html)
+ [PutFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutFunctionCodeSigningConfig.html)
+ [DeleteFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionCodeSigningConfig.html)
+ [ListFunctionsByCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_ListFunctionsByCodeSigningConfig.html)