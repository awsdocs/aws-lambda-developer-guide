# Using AWS Lambda with AWS CloudFormation<a name="services-cloudformation"></a>

As part of deploying AWS CloudFormation stacks, you can specify a Lambda function as a custom resource to execute any custom commands\. Associating a Lambda function with a custom resource enables you to invoke your Lambda function whenever you create, update, or delete AWS CloudFormation stacks\.

**Example AWS CloudFormation Message Event**  

```
{
  "StackId": "arn:aws:cloudformation:us-west-2:EXAMPLE/stack-name/guid",
  "ResponseURL": "http://pre-signed-S3-url-for-response",
  "ResourceProperties": {
    "StackName": "stack-name",
    "List": [
      "1",
      "2",
      "3"
    ]
  },
  "RequestType": "Create",
  "ResourceType": "Custom::TestResource",
  "RequestId": "unique id for this create request",
  "LogicalResourceId": "MyTestResource"
}
```

You configure event source mapping in AWS CloudFormation using stack definition\. For more information, see [AWS Lambda\-backed Custom Resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources-lambda.html) in the *AWS CloudFormation User Guide*\.