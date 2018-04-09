# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-sqs-create-execution-role"></a>

**Note**  
MORE INFORMATION TO COME FROM Sarthak

In order for AWS Lambda to poll, process and delete messages on the Amazon SQS queue you have configured, you need to set permissions for the following Amazon SQS actions:
+ [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)

You can do this in either of the following two ways:
+ **Identity\-based policy**: Add policies for `sqs:ReceiveMessage` and `sqs:DeleteMessage` to your function's execution role \(IAM role\)\. For more information, see [Customer Managed Policy Examples](access-control-identity-based.md#access-policy-examples-for-sdk-cli)
+ **Resource\-based policy**: Use the [AddPermission](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_AddPermission.html) API on Amazon Simple Queue Service and do the following: 
  + Specify `lambda.amazonaws.com ` as the principal in the `&AWSAccountID` field\.
  + Specify `ReceiveMesage ` in an `&ActionName1` field\.
  + Specify `DeleteMesage ` in an `&ActionName2` field\.

## Next Step<a name="with-sqs-next-step-3"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-sqs-create-function.md)