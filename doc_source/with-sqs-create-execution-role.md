# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-sqs-create-execution-role"></a>

In order for AWS Lambda to poll, process and delete messages on the Amazon SQS queue you have configured, you need to set permissions for the following Amazon SQS actions:
+ [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [ChangeMessageVisibility](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ChangeMessageVisibility.html)
+ [GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

You can do this in either of the following two ways:

**Note**  
If the Amazon SQS queue and Lambda function are associated with different user accounts, you must complete both of the following steps to enable cross\-account access\.
+ **Identity\-based policy**: Add an inline policy to the execution role that grants the permissions for the required actions listed previously, as shown in the following example:

  ```
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": sid,
              "Effect": "Allow",
              "Action": [
                  "sqs:DeleteMessage",
                  "sqs:ChangeMessageVisibility",
                  "sqs:ReceiveMessage",
                  "sqs:GetQueueAttributes"
              ],
              "Resource": “arn:aws:sqs:region:123456789012:test-queue“
          }
      ]
  }
  ```

  For more information, see [Customer Managed Policy Examples](access-control-identity-based.md#access-policy-examples-for-sdk-cli)\.
+ **Resource\-based policy**: Alternatively, you can use an Amazon SQS resource\-based policy\. Use the [AddPermission](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_AddPermission.html) API on Amazon Simple Queue Service and do the following: 
  + Specify the *function\-execution\-role* ARN \(Amazon Resource Name\) as the principal in the `&AWSAccountID` field\.
  + Specify `ReceiveMesage ` in an `&ActionName1` field\.
  + Specify `DeleteMesage ` in an `&ActionName2` field\.
  + Specify `GetQueueAttributes ` in an `&ActionName3` field\.
  + Specify `ChangeMessageVisibility` in an `&ActionName4` field\.

Once you have added the permissions for these actions to the function execution role, your policy should reflect the sample policy shown following:

```
{
  "Version": "2012-10-17",
  "Id": "arn:aws:sqs:region:123456789012:test-queue/mypolicy",
  "Statement": [
    {
      "Sid": sid,
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::123456789012:role/function-execution-role"
      },
      "Action": [
        "SQS:GetQueueAttributes",
        "SQS:ChangeMessageVisibility",
        "SQS:DeleteMessage",
        "SQS:ReceiveMessage"
      ],
      "Resource": "arn:aws:sqs:region:123456789012:test-queue"
    }
  ]
}
```

For more information, see [Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)](access-control-resource-based.md)\.

## Next Step<a name="with-sqs-next-step-3"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-sqs-create-function.md)