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
+ [Function actions](#permissions-resources-function)
+ [Event source mapping actions](#permissions-resources-eventsource)
+ [Layer actions](#permissions-resources-layers)

## Function resource names<a name="function-resources"></a>

You reference a Lambda function in a policy statement using an Amazon Resource Name \(ARN\)\. The format of a function ARN depends on whether you are referencing the whole function \(unqualified\) or a function [version](configuration-versions.md) or [alias](configuration-aliases.md) \(qualified\)\. 

When making Lambda API calls, users can specify a version or alias by passing a version ARN or alias ARN in the [GetFunction](API_GetFunction.md) `FunctionName` parameter, or by setting a value in the [GetFunction](API_GetFunction.md) `Qualifier` parameter\. Lambda makes authorization decisions by comparing the resource element in the IAM policy with both the `FunctionName` and `Qualifier` passed in API calls\. If there is a misamtch, Lambda denies the request\.

Whether you are allowing or denying an action on your function, you must use the correct function ARN types in your policy statement to achieve the results that you expect\. For example, if your policy references the unqualified ARN, Lambda accepts requests that reference the unqualified ARN but denies requests that reference a qualified ARN\.

**Example allowing invocation of an unqualified arn**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:myFunction"
    ]
}
```

If your policy references a specific qualified ARN, Lambda accepts requests that reference that ARN but denies requests that reference the unqualified ARN or a different qualified ARN, for example, `myFunction:2`\.

**Example allowing invocation of a specific qualified arn**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:myFunction:1"
        }
    ]
}
```

If your policy references any qualified ARN using `:*`, Lambda accepts any qualified ARN but denies requests that reference the unqualified ARN\.

**Example allowing invocation of any qualified arn**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:myFunction:*"
        }
    ]
}
```

If your policy references any ARN using `*`, Lambda accepts any qualified or unqualified ARN\.

**Example allowing invocation of any qualified or unqualified arn**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:myFunction*"
        }
    ]
}
```

## Function actions<a name="permissions-resources-function"></a>

Actions that operate on a function can be restricted to a specific function by function, version, or alias ARN, as described in the following table\. Actions that don't support resource restrictions can only be granted for all resources \(`*`\)\.


**Functions**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddPermission](API_AddPermission.md)  [RemovePermission](API_RemovePermission.md)  |  Function Function version Function alias  |  `lambda:Principal`  | 
|   [Invoke](API_Invoke.md) **Permission:** `lambda:InvokeFunction`  |  Function Function version Function alias  |  None  | 
|   [CreateFunction](API_CreateFunction.md)  [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)  |  Function  |  `lambda:CodeSigningConfigArn` `lambda:Layer` `lambda:VpcIds` `lambda:SubnetIds` `lambda:SecurityGroupIds`  | 
|   [CreateAlias](API_CreateAlias.md)  [DeleteAlias](API_DeleteAlias.md)  [DeleteFunction](API_DeleteFunction.md)  [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)  [GetAlias](API_GetAlias.md)  [GetFunction](API_GetFunction.md)  [GetFunctionConfiguration](API_GetFunctionConfiguration.md)  [GetPolicy](API_GetPolicy.md)  [ListAliases](API_ListAliases.md)  [ListVersionsByFunction](API_ListVersionsByFunction.md)  [PublishVersion](API_PublishVersion.md)  [PutFunctionConcurrency](API_PutFunctionConcurrency.md)  [UpdateAlias](API_UpdateAlias.md)  [UpdateFunctionCode](API_UpdateFunctionCode.md)  |  Function  |  None  | 
|   [GetAccountSettings](API_GetAccountSettings.md)  [ListFunctions](API_ListFunctions.md)  [ListTags](API_ListTags.md)  [TagResource](API_TagResource.md)  [UntagResource](API_UntagResource.md)  |  `*`  |  None  | 

## Event source mapping actions<a name="permissions-resources-eventsource"></a>

For event source mappings, delete and update permissions can be restricted to a specific event source\. The `lambda:FunctionArn` condition lets you restrict which functions a user can configure an event source to invoke\.

For these actions, the resource is the event source mapping, so Lambda provides a condition that lets you restrict permission based on the function that the event source mapping invokes\.


**Event source mappings**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)  [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)  |  Event source mapping  |   `lambda:FunctionArn`  | 
|   [CreateEventSourceMapping](API_CreateEventSourceMapping.md)  |  `*`  |   `lambda:FunctionArn`   | 
|   [GetEventSourceMapping](API_GetEventSourceMapping.md)  [ListEventSourceMappings](API_ListEventSourceMappings.md)  |  `*`  |  None  | 

## Layer actions<a name="permissions-resources-layers"></a>

Layer actions let you restrict the layers that a user can manage or use with a function\. Actions related to layer use and permissions act on a version of a layer, while `PublishLayerVersion` acts on a layer name\. You can use either with wildcards to restrict the layers that a user can work with by name\.

**Note**  
Note: the [GetLayerVersion](API_GetLayerVersion.md) action also covers [GetLayerVersionByArn](API_GetLayerVersionByArn.md)\. Lambda does not support `GetLayerVersionByArn` as an IAM action\.


**Layers**  

| Action | Resource | Condition | 
| --- | --- | --- | 
|   [AddLayerVersionPermission](API_AddLayerVersionPermission.md)  [RemoveLayerVersionPermission](API_RemoveLayerVersionPermission.md)  [GetLayerVersion](API_GetLayerVersion.md)  [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md)  [DeleteLayerVersion](API_DeleteLayerVersion.md)  |  Layer version  | None | 
|   [ListLayerVersions](API_ListLayerVersions.md)  [PublishLayerVersion](API_PublishLayerVersion.md)  |  Layer  | None | 
|   [ListLayers](API_ListLayers.md)  |   `*`   |  None  | 