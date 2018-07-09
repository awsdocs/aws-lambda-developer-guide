# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-sqs-create-execution-role"></a>

In order for AWS Lambda to poll, process and delete messages on the Amazon SQS queue you have configured, you need to set permissions for the following Amazon SQS actions:
+ [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)
+ [ChangeMessageVisibility](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ChangeMessageVisibility.html)

You can do this in either of the following two ways:

**Note**  
If the Amazon SQS queue and Lambda function are associated with different AWS accounts, you must use a **resource\-based policy** to enable cross\-account access\.
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

  For more information, see [Overview of Managing Access Permissions to Your Amazon Simple Queue Service Resource](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-overview-of-managing-access.html)
+ **Resource\-based policy**: Alternatively, you can use an Amazon SQS resource\-based policy\.

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

## Next Step<a name="with-sqs-next-step-3"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-sqs-create-function.md)