# Resources and conditions for Lambda actions<a name="lambda-api-permissions-ref"></a>

You can restrict the scope of a user's permissions by specifying resources and conditions in an AWS Identity and Access Management \(IAM\) policy\. Each action in a policy supports a combination of resource and condition types that varies depending on the behavior of the action\.

Every IAM policy statement grants permission to an action that's performed on a resource\. When the action doesn't act on a named resource, or when you grant permission to perform the action on all resources, the value of the resource in the policy is a wildcard \(`*`\)\. For many actions, you can restrict the resources that a user can modify by specifying the Amazon Resource Name \(ARN\) of a resource, or an ARN pattern that matches multiple resources\.

To restrict permissions by resource, specify the resource by ARN\.

**Lambda resource ARN format**
+ Function – `arn:aws:lambda:us-west-2:123456789012:function:my-function`
+ Function version – `arn:aws:lambda:us-west-2:123456789012:function:my-function:1`
+ Function alias – `arn:aws:lambda:us-west-2:123456789012:function:my-function:TEST`
+ Event source mapping – `arn:aws:lambda:us-west-2:123456789012:event-source-mapping:fa123456-14a1-4fd2-9fec-83de64ad683de6d47`
+ Layer – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer`
+ Layer version – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer:1`

For example, the following policy allows a user in AWS account `123456789012` to invoke a function named `my-function` in the US West \(Oregon\) AWS Region\.

**Example invoke function policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Invoke",
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction"
            ],
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:my-function"
        }
    ]
}
```

This is a special case where the action identifier \(`lambda:InvokeFunction`\) differs from the API operation \([Invoke](API_Invoke.md)\)\. For other actions, the action identifier is the operation name prefixed by `lambda:`\.

**Topics**
+ [Policy conditions](#authorization-conditions)
+ [Function resource names](#function-resources)
+ [Function actions](#permissions-resources-function)
+ [Event source mapping actions](#permissions-resources-eventsource)
+ [Layer actions](#permissions-resources-layers)

## Policy conditions<a name="authorization-conditions"></a>

Conditions are an optional policy element that applies additional logic to determine if an action is allowed\. In addition to common [conditions](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition.html) that all actions support, Lambda defines condition types that you can use to restrict the values of additional parameters on some actions\.

For example, the `lambda:Principal` condition lets you restrict the service or account that a user can grant invocation access to on a function's [resource\-based policy](access-control-resource-based.md)\. The following policy lets a user grant permission to Amazon Simple Notification Service \(Amazon SNS\) topics to invoke a function named `test`\.

**Example manage function policy permissions**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ManageFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission"
            ],
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:test:*",
            "Condition": {
                "StringEquals": {
                    "lambda:Principal": "sns.amazonaws.com"
                }
            }
        }
    ]
}
```

The condition requires that the principal is Amazon SNS and not another service or account\. The resource pattern requires that the function name is `test` and includes a version number or alias\. For example, `test:v1`\.

For more information on resources and conditions for Lambda and other AWS services, see [Actions, resources, and condition keys for AWS services](https://docs.aws.amazon.com/service-authorization/latest/reference/reference_policies_actions-resources-contextkeys.html) in the *Service Authorization Reference*\.

## Function resource names<a name="function-resources"></a>

You reference a Lambda function in a policy statement using an Amazon Resource Name \(ARN\)\. The format of a function ARN depends on whether you are referencing the whole function \(unqualified\) or a function [version](configuration-versions.md) or [alias](configuration-aliases.md) \(qualified\)\. 

When making Lambda API calls, users can specify a version or alias by passing a version ARN or alias ARN in the [GetFunction](API_GetFunction.md) `FunctionName` parameter, or by setting a value in the [GetFunction](API_GetFunction.md) `Qualifier` parameter\. Lambda makes authorization decisions by comparing the resource element in the IAM policy with both the `FunctionName` and `Qualifier` passed in API calls\. If there is a mismatch, Lambda denies the request\.

Whether you are allowing or denying an action on your function, you must use the correct function ARN types in your policy statement to achieve the results that you expect\. For example, if your policy references the unqualified ARN, Lambda accepts requests that reference the unqualified ARN but denies requests that reference a qualified ARN\.

**Note**  
You can't use a wildcard character \(\*\) to match the account ID\. For more information on accepted syntax, see [IAM JSON policy reference](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies.html) in the *IAM User Guide*\.

**Example allowing invocation of an unqualified ARN**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:myFunction"
        }
    ]
}
```

If your policy references a specific qualified ARN, Lambda accepts requests that reference that ARN but denies requests that reference the unqualified ARN or a different qualified ARN, for example, `myFunction:2`\.

**Example allowing invocation of a specific qualified ARN**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:myFunction:1"
        }
    ]
}
```

If your policy references any qualified ARN using `:*`, Lambda accepts any qualified ARN but denies requests that reference the unqualified ARN\.

**Example allowing invocation of any qualified ARN**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:myFunction:*"
        }
    ]
}
```

If your policy references any ARN using `*`, Lambda accepts any qualified or unqualified ARN\.

**Example allowing invocation of any qualified or unqualified ARN**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:myFunction*"
        }
    ]
}
```

## Function actions<a name="permissions-resources-function"></a>

Actions that operate on a function can be restricted to a specific function by function, version, or alias ARN, as described in the following table\. Actions that don't support resource restrictions are granted for all resources \(`*`\)\.


**Function actions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddPermission](API_AddPermission.md)  [RemovePermission](API_RemovePermission.md)  |  Function Function version Function alias  |  `lambda:Principal` `aws:ResourceTag/${TagKey}` `lambda:FunctionUrlAuthType`  | 
|   [Invoke](API_Invoke.md) **Permission:** `lambda:InvokeFunction`  |  Function Function version Function alias  |  `aws:ResourceTag/${TagKey}`  | 
|   [CreateFunction](API_CreateFunction.md)  |  Function  |  `lambda:CodeSigningConfigArn` `lambda:Layer` `lambda:VpcIds` `lambda:SubnetIds` `lambda:SecurityGroupIds` `aws:ResourceTag/${TagKey}` `aws:RequestTag/${TagKey}` `aws:TagKeys`  | 
|   [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)  |  Function  |  `lambda:CodeSigningConfigArn` `lambda:Layer` `lambda:VpcIds` `lambda:SubnetIds` `lambda:SecurityGroupIds` `aws:ResourceTag/${TagKey}`  | 
|   [CreateAlias](API_CreateAlias.md)  [DeleteAlias](API_DeleteAlias.md)  [DeleteFunction](API_DeleteFunction.md)  [DeleteFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionCodeSigningConfig.html)  [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)  [GetAlias](API_GetAlias.md)  [GetFunction](API_GetFunction.md)  [GetFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionCodeSigningConfig.html)  [GetFunctionConcurrency](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionConcurrency.html)  [GetFunctionConfiguration](API_GetFunctionConfiguration.md)  [GetPolicy](API_GetPolicy.md)  [ListProvisionedConcurrencyConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListProvisionedConcurrencyConfigs.html)  [ListAliases](API_ListAliases.md)  [ListTags](API_ListTags.md)  [ListVersionsByFunction](API_ListVersionsByFunction.md)  [PublishVersion](API_PublishVersion.md)  [PutFunctionCodeSigningConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutFunctionCodeSigningConfig.html)  [PutFunctionConcurrency](API_PutFunctionConcurrency.md)  [UpdateAlias](API_UpdateAlias.md)  [UpdateFunctionCode](API_UpdateFunctionCode.md)  |  Function  |  `aws:ResourceTag/${TagKey}`  | 
|   [CreateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateFunctionUrlConfig.html)  [DeleteFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionUrlConfig.html)  [GetFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionUrlConfig.html)  [UpdateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionUrlConfig.html)  |  Function Function alias  |  `lambda:FunctionUrlAuthType` `lambda:FunctionArn`  | 
|   [ListFunctionUrlConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListFunctionUrlConfigs.html)  |  Function  |  `lambda:FunctionUrlAuthType`  | 
|   [DeleteProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteProvisionedConcurrencyConfig.html)  [GetProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetProvisionedConcurrencyConfig.html)  [PutProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutProvisionedConcurrencyConfig.html)  |  Function alias Function version  |  `aws:ResourceTag/${TagKey}`  | 
|   [GetAccountSettings](API_GetAccountSettings.md)  [ListFunctions](API_ListFunctions.md)  |  `*`  |  None  | 
|   [TagResource](API_TagResource.md)  |  Function  |  `aws:ResourceTag/${TagKey}` `aws:RequestTag/${TagKey}` `aws:TagKeys`  | 
|   [UntagResource](API_UntagResource.md)  |  Function  |  `aws:ResourceTag/${TagKey}` `aws:TagKeys`  | 

## Event source mapping actions<a name="permissions-resources-eventsource"></a>

For [event source mappings](invocation-eventsourcemapping.md), you can restrict delete and update permissions to a specific event source\. The `lambda:FunctionArn` condition lets you restrict which functions a user can configure an event source to invoke\.

For these actions, the resource is the event source mapping, so Lambda provides a condition that lets you restrict permission based on the function that the event source mapping invokes\.


**Event source mapping actions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)  [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)  |  Event source mapping  |   `lambda:FunctionArn`  | 
|   [CreateEventSourceMapping](API_CreateEventSourceMapping.md)  |  `*`  |   `lambda:FunctionArn`   | 
|   [GetEventSourceMapping](API_GetEventSourceMapping.md)  [ListEventSourceMappings](API_ListEventSourceMappings.md)  |  `*`  |  None  | 

## Layer actions<a name="permissions-resources-layers"></a>

Layer actions let you restrict the layers that a user can manage or use with a function\. Actions related to layer use and permissions act on a version of a layer, while `PublishLayerVersion` acts on a layer name\. You can use either with wildcards to restrict the layers that a user can work with by name\.

**Note**  
The [GetLayerVersion](API_GetLayerVersion.md) action also covers [GetLayerVersionByArn](API_GetLayerVersionByArn.md)\. Lambda does not support `GetLayerVersionByArn` as an IAM action\.


**Layer actions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddLayerVersionPermission](API_AddLayerVersionPermission.md)  [RemoveLayerVersionPermission](API_RemoveLayerVersionPermission.md)  [GetLayerVersion](API_GetLayerVersion.md)  [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md)  [DeleteLayerVersion](API_DeleteLayerVersion.md)  |  Layer version  | None | 
|   [ListLayerVersions](API_ListLayerVersions.md)  [PublishLayerVersion](API_PublishLayerVersion.md)  |  Layer  | None | 
|   [ListLayers](API_ListLayers.md)  |   `*`   |  None  | 