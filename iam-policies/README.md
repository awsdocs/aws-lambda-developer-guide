# Lambda console IAM policy examples

The documents in this folder show AWS Identity and Access Management (IAM) policies related to the AWS Lambda console. They show permissions that you need to use the Lambda console, and permissions that the console can add to your function's execution role.

## Console Use Policies

These policies show the user permissions required to configure triggers in the Lambda console.

- console-apigateway.json
- console-cloudwatchevents.json
- console-cloudwatchlogs.json
- console-cognito.json
- console-dynamodb.json
- console-iot.json
- console-kinesis.json
- console-s3.json
- console-sns.json

## Execution Role Templates

These policies show the permissions that the Lambda console adds to your function's execution role when you create a new role from a template.

- template-atedge.json
- template-basic.json
- template-dlq-sns.json
- template-dlq-sqs.json
- template-dynamodb.json
- template-kinesis.json
- template-vpcaccess.json

## Blueprint Policies

These policies show the permissions that the Lambda console adds to your function's execution role when you create a function from a blueprint.

- blueprint-cloudformation.json
- blueprint-ec2ami.json
- blueprint-elasticsearch.json
- blueprint-iotbutton.json
- blueprint-kmsdecrypt.json
- blueprint-microservice.json
- blueprint-rekognition-nodata.json
- blueprint-rekognition-readonly.json
- blueprint-rekognition-writeonly.json
- blueprint-s3get.json
- blueprint-sesbounce.json
- blueprint-sqspoller.json
- blueprint-testharness.json
- blueprint-vpnmonitor.json
