# Monitoring the state of a function with the Lambda API<a name="functions-states"></a>

When you create or update a function, Lambda provisions the compute and networking resources that enable it to run\. In most cases, this process is very fast, and your function is ready to be invoked or modified right away\.

If you configure your function to connect to a virtual private cloud \(VPC\), the process can take longer\. When you first connect a function to a VPC, Lambda provisions network interfaces, which takes about a minute\. To communicate the current state of your function, Lambda includes additional fields in the [function configuration](API_FunctionConfiguration.md) document that is returned by several Lambda API actions\.

When you create a function, the function is initially in the `Pending` state\. When the function is ready to be invoked, the state changes from `Pending` to `Active`\. While the state is `Pending`, invocations and other API actions that operate on the function return an error\. If you build automation around creating and updating functions, wait for the function to become active before performing additional actions that operate on the function\.

You can use the Lambda API to get information about a function's state\. State information is included in the [FunctionConfiguration](API_FunctionConfiguration.md) document returned by several API actions\. To view the function's state with the AWS CLI, use the `get-function-configuration` command\.

```
$ aws lambda get-function-configuration --function-name my-function
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs12.x",
    "Role": "arn:aws:iam::123456789012:role/lambda-role",
    "TracingConfig": {
        "Mode": "Active"
    },
    "State": "Pending",
    "StateReason": "The function is being created.",
    "StateReasonCode": "Creating",
    ...
}
```

The `StateReason` and `StateReasonCode` contain additional information about the state when it is not `Active`\. The following operations fail while function creation is pending:
+ [Invoke](API_Invoke.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)

When you update a function's configuration, the update can trigger an asynchronous operation to provision resources\. While this is in progress, you can invoke the function, but other operations on the function fail\. Invocations that occur while the update is in progress run against the previous configuration\. The function's state is `Active`, but its `LastUpdateStatus` is `InProgress`\.

**Example Function configuration – Connecting to a VPC**  

```
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs12.x",
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

The following operations fail while an asynchronous update is in progress:
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)

Other operations, including invocation, work while updates are in progress\.

For example, when you connect your function to a virtual private cloud \(VPC\), Lambda provisions an elastic network interface for each subnet\. This process can leave your function in a pending state for a minute or so\. Lambda also reclaims network interfaces that are not in use, placing your function in an `Inactive` state\. When the function is inactive, an invocation causes it to enter the `Pending` state while network access is restored\. The invocation that triggers restoration, and further invocations while the operation is pending, fail with `ResourceNotReadyException`\.

If Lambda encounters an error when restoring a function's network interface, the function goes back to the `Inactive` state\. The next invocation can trigger another attempt\. For some configuration errors, Lambda waits at least 5 minutes before attempting to create another network interface\. These errors have the following `LastUpdateStatusReasonCode` values:
+ `InsufficientRolePermission` – Role doesn't exist or is missing permissions\.
+ `SubnetOutOfIPAddresses` – All IP addresses in a subnet are in use\.

For more information on how states work with VPC connectivity, see [Configuring a Lambda function to access resources in a VPC](configuration-vpc.md)\.