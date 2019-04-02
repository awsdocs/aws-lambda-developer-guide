# Resources and Conditions for Lambda Actions<a name="lambda-api-permissions-ref"></a>

You can restrict the scope of a user's permissions by specifying resources and conditions in an IAM policy\. Each API action supports a combination of resource and condition types that varies depending on the behavior of the action\.

Every IAM policy statement grants permission to an action that's performed on a resource\. When the action doesn't act on a named resource, or when you grant permission to perform the action on all resources, the value of the resource in the policy is a wildcard \(`*`\)\. For many API actions, you can restrict the resources that a user can modify by specifying the Amazon Resource Name \(ARN\) of a resource, or an ARN pattern that matches multiple resources\.

To restrict permissions by resource, specify the resource by ARN\.

**Lambda Resource ARN Format**
+ Function – `arn:aws:lambda:us-west-2:123456789012:function:my-function`
+ Function version – `arn:aws:lambda:us-west-2:123456789012:function:my-function:1`
+ Function alias – `arn:aws:lambda:us-west-2:123456789012:function:my-function:TEST`
+ Event source mapping – `arn:aws:lambda:us-west-2:123456789012:event-source-mapping:fa123456-14a1-4fd2-9fec-83de64ad683de6d47`
+ Layer – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer`
+ Layer version – `arn:aws:lambda:us-west-2:123456789012:layer:my-layer:1`

For example, the following policy allows a user in account `123456789012` to invoke a function named `my-function` in the US West \(Oregon\) Region\.

**Example Invoke a Function Policy**  

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

**Example Manage Function Policy Permissions**  

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

For more information on resources and conditions for Lambda and other AWS services, see [Actions, Resources, and Condition Keys](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_actions-resources-contextkeys.html) in the *IAM User Guide*\.

**Topics**
+ [Functions](#permissions-resources-function)
+ [Event Source Mappings](#permissions-resources-eventsource)
+ [Layers](#permissions-resources-layers)

## Functions<a name="permissions-resources-function"></a>

Actions that operate on a function can be restricted to a specific function by function, version, or alias ARN, as described in the following table\. Actions that don't support resource restrictions can only be granted for all resources \(`*`\)\.


**Functions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddPermission](API_AddPermission.md)  [RemovePermission](API_RemovePermission.md)  |  Function Function version Function alias  |  `lambda:Principal`  | 
|   [Invoke](API_Invoke.md) **Permission:** `lambda:InvokeFunction`  |  Function Function version Function alias  |  None  | 
|   [CreateFunction](API_CreateFunction.md)  [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)  |  Function  |  `lambda:Layer`  | 
|   [CreateAlias](API_CreateAlias.md)  [DeleteAlias](API_DeleteAlias.md)  [DeleteFunction](API_DeleteFunction.md)  [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)  [GetAlias](API_GetAlias.md)  [GetFunction](API_GetFunction.md)  [GetFunctionConfiguration](API_GetFunctionConfiguration.md)  [GetPolicy](API_GetPolicy.md)  [ListAliases](API_ListAliases.md)  [ListVersionsByFunction](API_ListVersionsByFunction.md)  [PublishVersion](API_PublishVersion.md)  [PutFunctionConcurrency](API_PutFunctionConcurrency.md)  [UpdateAlias](API_UpdateAlias.md)  [UpdateFunctionCode](API_UpdateFunctionCode.md)  |  Function  |  None  | 
|   [GetAccountSettings](API_GetAccountSettings.md)  [ListFunctions](API_ListFunctions.md)  [ListTags](API_ListTags.md)  [TagResource](API_TagResource.md)  [UntagResource](API_UntagResource.md)  |  `*`  |  None  | 

## Event Source Mappings<a name="permissions-resources-eventsource"></a>

For event source mappings, delete and update permissions can be restricted to a specific event source\. The `lambda:FunctionArn` condition lets you restrict which functions a user can configure an event source to invoke\.

For these actions, the resource is the event source mapping, so Lambda provides a condition that lets you restrict permission based on the function that the event source mapping invokes\.


**Event Source Mappings**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)  [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)  |  Event source mapping  |   `lambda:FunctionArn`  | 
|   [CreateEventSourceMapping](API_CreateEventSourceMapping.md)  |  `*`  |   `lambda:FunctionArn`   | 
|   [GetEventSourceMapping](API_GetEventSourceMapping.md)  [ListEventSourceMappings](API_ListEventSourceMappings.md)  |  `*`  |  None  | 

## Layers<a name="permissions-resources-layers"></a>

Layer actions let you restrict the layers that a user can manage or use with a function\. Actions related to layer use and permissions act on a version of a layer, while `PublishLayerVersion` acts on a layer name\. You can use either with wildcards to restrict the layers that a user can work with by name\.


**Layers**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddLayerVersionPermission](API_AddLayerVersionPermission.md)  [RemoveLayerVersionPermission](API_RemoveLayerVersionPermission.md)  [GetLayerVersion](API_GetLayerVersion.md)  [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md)  [DeleteLayerVersion](API_DeleteLayerVersion.md)  |  Layer version  | None | 
|   [PublishLayerVersion](API_PublishLayerVersion.md)  |  Layer  | None | 
|   [ListLayers](API_ListLayers.md)  [ListLayerVersions](API_ListLayerVersions.md)  |   `*`   |  None  | 