# Lambda API Permissions: Actions, Resources, and Conditions Reference<a name="lambda-api-permissions-ref"></a>

When you are setting up [Access Control](lambda-auth-and-access-control.md#access-control) and writing permissions policies that you can attach to an IAM identity \(identity\-based policies\), you can use the following table as a reference\. The table lists each AWS Lambda API operation, the corresponding actions for which you can grant permissions to perform the action, the AWS resource for which you can grant the permissions and condition keys for specified API actions\. You specify the actions in the policy's `Action` field, the resource value in the policy's `Resource` field and a condition key in the policy's `Condition keys` field\. 

To specify an action, use the `lambda:` prefix followed by the API operation name \(for example, `lambda:CreateFunction`\)\.

**Note**  
Permissions for the AWS Lambda `Invoke` API in the following table can also be granted by using resource\-based policies\. For more information, see [Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)](access-control-resource-based.md)\.

You can use AWS\-wide condition keys in your AWS Lambda policies to express conditions\. For a complete list of AWS\-wide keys, see [Available Keys for Conditions](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#AvailableKeys) in the *IAM User Guide*\. 

AWS Lambda also offers predefined condition keys to a limited set of API operations\. For example, you can: 
+ Restrict access based on the Lambda function ARN \(Amazon Resource Name\) to the following operations: 
  + CreateEventSourceMapping
  + DeleteEventSourceMapping
  + UpdateEventSourceMapping

  The following is an example policy that applies this condition:

  ```
  "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "DeleteEventSourceMappingPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:DeleteEventSourceMapping"
            ],
            "Resource": "arn:aws:lambda:region:account-id:event-source-mapping:UUID",
            "Condition": {"StringEquals": {"lambda:FunctionArn": "arn:aws:lambda:region:account-id:function:function-name}}
        }
    ]
  ```
+ Restrict mapping based on the AWS service principal to the following operations:
  + AddPermission
  + RemovePermission

   The following is an example policy that applies this condition:

  ```
   "Version": "2012-10-17",
    "Statement": [
       {
          "Sid": "AddPermissionPolicy",
          "Effect": "Allow",
          "Action": [
              "lambda:AddPermission"
          ],
          "Resource": "arn:aws:lambda:region:account-id:function:function-name",
          "Condition": {"StringEquals": {"lambda:Principal": "s3.amazonaws.com"}}
      }
   ]
  ```


**AWS Lambda API and Required Permissions for Actions**  

| API Actions | Resources | Condition Key | 
| --- | --- | --- | 
|   **API:** [AddLayerVersionPermission](API_AddLayerVersionPermission.md) **Required Permission:** `lambda:AddLayerVersionPermission` | arn:aws:lambda:region:account\-id:layer:layer\-name:1 | N/A | 
|   **API:** [AddPermission](API_AddPermission.md) **Required Permission:** `lambda:AddPermission`  |  `arn:aws:lambda:region:account-id:function:function-name`  | lambda:Principal | 
|   **API:** [CreateAlias](API_CreateAlias.md) **Required Permission:** `lambda:CreateAlias`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A  | 
|   **API:** [CreateEventSourceMapping](API_CreateEventSourceMapping.md)  **Required Permissions:** `lambda:CreateEventSourceMapping`  | \* |   `lambda:FunctionArn` | 
|  **API:** [CreateFunction](API_CreateFunction.md) **Required Permissions:** `lambda:CreateFunction` | arn:aws:lambda:region:account\-id:function:function\-name  | `lambda:Layer`  | 
| **API:** [DeleteAlias](API_DeleteAlias.md) **Required Permission:** `lambda:DeleteAlias`  | arn:aws:lambda:region:account\-id:function:function\-name | N/A | 
| **API:** [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) **Required Permission:** `lambda:DeleteEventSourceMapping`  |  `arn:aws:lambda:region:account-id:event-source-mapping:UUID`  |   `lambda:FunctionArn` | 
| **API:** [DeleteFunction](API_DeleteFunction.md) **Required Permission:** `lambda:DeleteFunction`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
|   **API:** [DeleteLayerVersion](API_DeleteLayerVersion.md) **Required Permission:** `lambda:DeleteLayerVersion`  |   `arn:aws:lambda:region:account-id:layer:layer-name:1`  | N/A | 
| **API:** [GetAccountSettings](API_GetAccountSettings.md) **Required Permission:** `lambda:GetAccountSettings`  |  `*`  | N/A | 
| **API:** [GetAlias](API_GetAlias.md) **Required Permission:** `lambda:GetAlias`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [GetEventSourceMapping](API_GetEventSourceMapping.md) **Required Permission:** `lambda:GetEventSourceMapping`  |  `*`  | N/A | 
| **API:** [GetFunction](API_GetFunction.md) **Required Permission:** `lambda:GetFunction`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [GetFunctionConfiguration](API_GetFunctionConfiguration.md) **Required Permission:** `lambda:GetFunctionConfiguration`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
|   **API:** [GetLayerVersion](API_GetLayerVersion.md) **Required Permission:** `lambda:GetLayerVersion`  |   `arn:aws:lambda:region:account-id:layer:layer-name:1`  | aws:PrincipalOrgID | 
|   **API:** [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md) **Required Permission:** `lambda:GetLayerVersionPolicy`  |  `arn:aws:lambda:region:account-id:layer:layer-name:1`  | N/A | 
| **API:** [GetPolicy](API_GetPolicy.md) **Required Permission:** `lambda:GetPolicy` | arn:aws:lambda:region:account\-id:function:function\-name | N/A | 
| **API:** [Invoke](API_Invoke.md) **Required Permission:** `lambda:InvokeFunction` |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [ListAliases](API_ListAliases.md) Required Permission: lambda:ListAliases |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [ListEventSourceMappings](API_ListEventSourceMappings.md) **Required Permission:** `lambda:ListEventSourceMappings`  |  `*`  | N/A | 
| **API:** [ListFunctions](API_ListFunctions.md)  **Required Permission:** `lambda:ListFunctions`  | `*` | N/A | 
|   **API:** [ListLayers](API_ListLayers.md) **Required Permission:** `lambda:ListLayers`  |   `*`  | N/A | 
|   **API:** [ListLayerVersions](API_ListLayerVersions.md) **Required Permission:** `lambda:ListLayerVersions`  |   `*`  | N/A | 
| **API:** [ListTags](API_ListTags.md)  **Required Permission:** `lambda:ListTags`  |  `*`  |  N/A  | 
| **API:** [ListVersionsByFunction](API_ListVersionsByFunction.md)  **Required Permission:** `lambda:ListVersionsByFunction` |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [PublishVersion](API_PublishVersion.md)  **Required Permission:** `lambda:PublishVersion` |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
|   **API:** [PublishLayerVersion](API_PublishLayerVersion.md) **Required Permission:** `lambda:PublishLayerVersion`  |  `arn:aws:lambda:region:account-id:layer:layer-name`  | N/A | 
|   **API:** [RemoveLayerVersionPermission](API_RemoveLayerVersionPermission.md) **Required Permission:** `lambda:RemoveLayerVersionPermission`  |  `arn:aws:lambda:region:account-id:layer:layer-name:1`  | N/A | 
| **API:** [RemovePermission](API_RemovePermission.md)  **Required Permission:** `lambda:RemovePermission`  |  `arn:aws:lambda:region:account-id:function:function-name` |  `lambda:Principal`  | 
| **API:** [TagResource](API_TagResource.md)  **Required Permission:** `lambda:TagResource`  |  `*`  |  N/A  | 
| **API:** [UntagResource](API_UntagResource.md)  **Required Permission:** `lambda:UntagResource`  |  `*`  |  N/A  | 
| **API:** [UpdateAlias](API_UpdateAlias.md) **Required Permission:** `lambda:UpdateAlias`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) **Required Permissions:** `lambda:UpdateEventSourceMapping`  |   `arn:aws:lambda:region:account-id:event-source-mapping:UUID`  |  `lambda:FunctionArn` | 
| **API:** [UpdateFunctionCode](API_UpdateFunctionCode.md) **Required Permissions:** `lambda:UpdateFunctionCode`  |  `arn:aws:lambda:region:account-id:function:function-name`  | N/A | 
| **API:** [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) **Required Permissions:** `lambda:UpdateFunctionConfiguration` | arn:aws:lambda:region:account\-id:function:function\-name | lambda:Layer | 