# Lambda function states<a name="functions-states"></a>

Lambda includes a state field in the function configuration for all functions to indicate when your function is ready to invoke\. `State` provides information about the current status of the function, including whether you can successfully invoke the function\. Function states do not change the behavior of function invocations or how your function runs the code\. Function states include:
+ `Pending` – After Lambda creates the function, it sets the state to pending\. While in pending state, Lambda attempts to create or configure resources for the function, such as VPC or EFS resources\. Lambda does not invoke a function during pending state\. Any invocations or other API actions that operate on the function will fail\.
+ `Active` – Your function transitions to active state after Lambda completes resource configuration and provisioning\. Functions can only be successfully invoked while active\.
+ `Failed` – Indicates that resource configuration or provisioning encountered an error\.
+ `Inactive` – A function becomes inactive when it has been idle long enough for Lambda to reclaim the external resources that were configured for it\. When you try to invoke a function that is inactive, the invocation fails and Lambda sets the function to pending state until the function resources are recreated\. If Lambda fails to recreate the resources, the function is set to the inactive state\.

If you are using SDK\-based automation workflows or calling Lambda’s service APIs directly, ensure that you check a function's state before invocation to verify that it is active\. You can do this with the Lambda API action [GetFunction](API_GetFunction.md), or by configuring a waiter using the [AWS SDK for Java 2\.0](https://github.com/aws/aws-sdk-java-v2)\.

```
aws lambda get-function --function-name my-function --query 'Configuration.[State, LastUpdateStatus]'
```

You should see the following output:

```
[
 "Active",
 "Successful" 
]
```

Functions have two other attributes, `StateReason` and `StateReasonCode`\. These provide information and context about the function’s state when it is not active for troubleshooting issues\.

The following operations fail while function creation is pending:
+ [Invoke](API_Invoke.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)

## Function states while updating<a name="functions-states-updating"></a>

Lambda provides additional context for functions undergoing updates with the `LastUpdateStatus` attribute, which can have the following statuses:
+ `InProgress` – An update is happening on an existing function\. While a function update is in progress, invocations go to the function’s previous code and configuration\.
+ `Successful` – The update has completed\. Once Lambda finishes the update, this stays set until a further update\.
+ `Failed` – The function update has failed\. Lambda aborts the update and the function’s previous code and configuration remain available\.

**Example**  
The following is the result of `get-function-configuration` on a function undergoing an update\.  

```
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs16.x",
    "VpcConfig": {
        "SubnetIds": [
            "subnet-071f712345678e7c8",
            "subnet-07fd123456788a036",
            "subnet-0804f77612345cacf"
        ],
        "SecurityGroupIds": [
            "sg-085912345678492fb"
        ],
        "VpcId": "vpc-08e1234569e011e83"
    },
    "State": "Active",
    "LastUpdateStatus": "InProgress",
    ...
}
```

[FunctionConfiguration](API_FunctionConfiguration.md) has two other attributes, `LastUpdateStatusReason` and `LastUpdateStatusReasonCode`, to help troubleshoot issues with updating\.

The following operations fail while an asynchronous update is in progress:
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)