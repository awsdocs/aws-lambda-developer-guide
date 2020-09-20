# Resources and conditions for Lambda actions<a name="lambda-api-permissions-ref"></a>

You can restrict the scope of a user's permissions by specifying resources and conditions in an IAM policy\. Each API action supports a combination of resource and condition types that varies depending on the behavior of the action\.

Every IAM policy statement grants permission to an action that's performed on a resource\. When the action doesn't act on a named resource, or when you grant permission to perform the action on all resources, the value of the resource in the policy is a wildcard \(`*`\)\. For many API actions, you can restrict the resources that a user can modify by specifying the Amazon Resource Name \(ARN\) of a resource, or an ARN pattern that matches multiple resources\.

To restrict permissions by resource, specify the resource by ARN\.

**Lambda resource ARN format**
+ Function – `arn:aws:lambda:us-west-2:123456789012:function:my-function`
+ Function version – `arn:aws:lambda:us-west-2:123456789012:function:my-function:1`
+ Function alias – `arn:aws:lambda:us-west-2:123456789012:function:my-function:TEST`
+ Event source mapping – `arn:aws:lambda:us-west-2:123456789012:event-source-mapping:fa123456-14a1-4fd2-9fec-83de64ad683de6d47`
+ Layer – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer`
+ Layer version – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer:1`

For example, the following policy allows a user in account `123456789012` to invoke a function named `my-function` in the US West \(Oregon\) Region\.

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

Conditions are an optional policy element that applies additional logic to determine if an action is allowed\. In addition to [common conditions](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition.html) supported by all actions, Lambda defines condition types that you can use to restrict the values of additional parameters on some actions\.

For example, the `lambda:Principal` condition lets you restrict the service or account that a user can grant invocation access to on a function's resource\-based policy\. The following policy lets a user grant permission to SNS topics to invoke a function named `test`\.

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

For more information on resources and conditions for Lambda and other AWS services, see [Actions, resources, and condition keys](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_actions-resources-contextkeys.html) in the *IAM User Guide*\.

**Topics**
+ [Function resource names](#function-resources)
+ [Functions](#permissions-resources-function)
+ [Event source mappings](#permissions-resources-eventsource)
+ [Layers](#permissions-resources-layers)

## Function resource names<a name="function-resources"></a>

You reference a Lambda function in a policy statement using an Amazon Resource Names \(ARN\)\. The format of a function ARN depends on whether you are referencing the whole function, a function [version](configuration-versions.md), or an [alias](configuration-aliases.md)\. 

When making Lambda API calls, users can specify a version or alias by passing a version ARN or alias ARN in the [https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html#API_GetFunction_RequestSyntax](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html#API_GetFunction_RequestSyntax) parameter, or by setting a value in the [https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html#API_GetFunction_RequestSyntax](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html#API_GetFunction_RequestSyntax) parameter\. Lambda makes authorization decisions by comparing the resource element in the IAM policy with the `FunctionName` passed in the API calls\.

You must use the correct function ARN types in your policies to achieve the results that you expect, especially in policies that deny access\. We recommend that you follow the best practices for using Deny statements with functions\.

### Best practices for using Deny statements with functions<a name="authorization-bp"></a>

The following table summarizes the resources to use in Deny effects\. In the **Resource** column, `MyFunction` is the name of the function, `:1` refers to version 1 of the function, and `MyAlias` is the name of a function alias\.


**Resource best practices**  

| Policy objective | Resource | 
| --- | --- | 
|  Deny access to all versions of a function  |  `MyFunction*`  | 
|  Deny access to a specific alias  |  `MyFunction:MyAlias` and `MyFunction`  | 
|  Deny access to a specific version of a function  |  `MyFunction:1` and `MyFunction`  | 

The following sections provide example policy statements for each of the policy objectives\.

**Note**  
You can use only identity\-based policies to deny specific function resources\. Currently, Lambda does not support the `Deny` effect in resource\-based policies\.

For the action list in a policy statement, you can add any of the [ actions defined by Lambda](https://docs.aws.amazon.com/IAM/latest/UserGuide/list_awslambda.html) that act on a function resource\.

#### Deny access to all function versions<a name="authorization-deny-all"></a>

The following identity\-based policy statement denies access to the `lambda:GetFunctionConfiguration` action for all versions of the `my-function` function\. The wildcard character at the end of the function ARN ensures that this policy applies to all version and alias ARNs\. 

**Example identity\-based policy**  

```
{
    "Version": "2020-07-20",
    "Statement": [
        {
            "Effect": "Deny",
            "Action": [
                "lambda:GetFunctionConfiguration" 
            ],
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:my-function*"
        }        
    ]
}
```

#### Deny access to a specific function alias<a name="authorization-deny-alias"></a>

To deny access to a specific alias, you must specify both the alias ARN and the unqualified function ARN in the policy\. This prevents users from accessing the specific alias by passing the unqualified ARN as the `FunctionName` and the alias as the `Qualifier`\.

**Note**  
If you create this type of policy, API calls need to refer to the unpublished version of the function by specifying a qualified ARN with the $LATEST suffix in the `FunctionName` parameter\. 

The following identity\-based policy statement denies access to the `lambda:InvokeFunction` action in the `my-alias` alias of the `my-function` function\.

**Example identity\-based policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "DenySpecificAlias",
            "Effect": "Deny",
            "Action": "lambda:InvokeFunction",
            "Resource": [
                "arn:aws:lambda:us-west-2:123456789012:function:my-function:my-alias",
                "arn:aws:lambda:us-west-2:123456789012:function:my-function"
                ]
        }
    ]
}
```

#### Deny access to a specific function version<a name="authorization-deny-specific"></a>

To deny access to a specific version, you must specify both the qualified ARN and the unqualified ARN in the policy\. This prevents users from accessing the specific version by passing the unqualified ARN as the `FunctionName` and the version as the `Qualifier`\.

**Note**  
If you create this type of policy, API calls need to refer to the unpublished version of the function by specifying a qualified ARN with the $LATEST suffix in the `FunctionName` parameter\. 

The following identity\-based policy statement denies access to the invoke action in version 1 of the `my-function` function\.

**Example identity\-based policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "DenySpecificFunctionVersion",
            "Effect": "Deny",
            "Action": [
                "lambda:InvokeFunction"
            ],
            "Resource": [
                "arn:aws:lambda:us-west-2:123456789012:function:my-function:1",
                "arn:aws:lambda:us-west-2:123456789012:function:my-function"
                ]
        }
    ]
}
```

## Functions<a name="permissions-resources-function"></a>

Actions that operate on a function can be restricted to a specific function by function, version, or alias ARN, as described in the following table\. Actions that don't support resource restrictions can only be granted for all resources \(`*`\)\.


**Functions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddPermission](API_AddPermission.md)  [RemovePermission](API_RemovePermission.md)  |  Function Function version Function alias  |  `lambda:Principal`  | 
|   [Invoke](API_Invoke.md) **Permission:** `lambda:InvokeFunction`  |  Function Function version Function alias  |  None  | 
|   [CreateFunction](API_CreateFunction.md)  [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)  |  Function  |  `lambda:Layer` `lambda:VpcIds` `lambda:SubnetIds` `lambda:SecurityGroupIds`  | 
|   [CreateAlias](API_CreateAlias.md)  [DeleteAlias](API_DeleteAlias.md)  [DeleteFunction](API_DeleteFunction.md)  [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)  [GetAlias](API_GetAlias.md)  [GetFunction](API_GetFunction.md)  [GetFunctionConfiguration](API_GetFunctionConfiguration.md)  [GetPolicy](API_GetPolicy.md)  [ListAliases](API_ListAliases.md)  [ListVersionsByFunction](API_ListVersionsByFunction.md)  [PublishVersion](API_PublishVersion.md)  [PutFunctionConcurrency](API_PutFunctionConcurrency.md)  [UpdateAlias](API_UpdateAlias.md)  [UpdateFunctionCode](API_UpdateFunctionCode.md)  |  Function  |  None  | 
|   [GetAccountSettings](API_GetAccountSettings.md)  [ListFunctions](API_ListFunctions.md)  [ListTags](API_ListTags.md)  [TagResource](API_TagResource.md)  [UntagResource](API_UntagResource.md)  |  `*`  |  None  | 

## Event source mappings<a name="permissions-resources-eventsource"></a>

For event source mappings, delete and update permissions can be restricted to a specific event source\. The `lambda:FunctionArn` condition lets you restrict which functions a user can configure an event source to invoke\.

For these actions, the resource is the event source mapping, so Lambda provides a condition that lets you restrict permission based on the function that the event source mapping invokes\.


**Event source mappings**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)  [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)  |  Event source mapping  |   `lambda:FunctionArn`  | 
|   [CreateEventSourceMapping](API_CreateEventSourceMapping.md)  |  `*`  |   `lambda:FunctionArn`   | 
|   [GetEventSourceMapping](API_GetEventSourceMapping.md)  [ListEventSourceMappings](API_ListEventSourceMappings.md)  |  `*`  |  None  | 

## Layers<a name="permissions-resources-layers"></a>

Layer actions let you restrict the layers that a user can manage or use with a function\. Actions related to layer use and permissions act on a version of a layer, while `PublishLayerVersion` acts on a layer name\. You can use either with wildcards to restrict the layers that a user can work with by name\.

**Note**  
Note: the [GetLayerVersion](API_GetLayerVersion.md) action also covers [GetLayerVersionByArn](API_GetLayerVersionByArn.md)\. Lambda does not support `GetLayerVersionByArn` as an IAM action\.


**Layers**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddLayerVersionPermission](API_AddLayerVersionPermission.md)  [RemoveLayerVersionPermission](API_RemoveLayerVersionPermission.md)  [GetLayerVersion](API_GetLayerVersion.md)  [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md)  [DeleteLayerVersion](API_DeleteLayerVersion.md)  |  Layer version  | None | 
|   [ListLayerVersions](API_ListLayerVersions.md)  [PublishLayerVersion](API_PublishLayerVersion.md)  |  Layer  | None | 
|   [ListLayers](API_ListLayers.md)  |   `*`   |  None  | 