# Using AWS Lambda with AWS IoT<a name="services-iot"></a>

AWS IoT provides secure communication between internet\-connected devices \(such as sensors\) and the AWS Cloud\. This makes it possible for you to collect, store, and analyze telemetry data from multiple devices\.

You can create AWS IoT rules for your devices to interact with AWS services\. The AWS IoT [Rules Engine](https://docs.aws.amazon.com/iot/latest/developerguide/iot-rules.html) provides a SQL\-based language to select data from message payloads and send the data to other services, such as Amazon S3, Amazon DynamoDB, and AWS Lambda\. You define a rule to invoke a Lambda function when you want to invoke another AWS service or a third\-party service\. 

When an incoming IoT message triggers the rule, AWS IoT invokes your Lambda function [asynchronously](invocation-async.md) and passes data from the IoT message to the function\. 

The following example shows a moisture reading from a greenhouse sensor\. The **row** and **pos** values identify the location of the sensor\. This example event is based on the greenhouse type in the [AWS IoT Rules tutorials](https://docs.aws.amazon.com/iot/latest/developerguide/iot-rules-tutorial.html)\. 

**Example AWS IoT message event**  

```
{
    "row" : "10",
    "pos" : "23",
    "moisture" : "75"
}
```

For asynchronous invocation, Lambda queues the message and [retries](invocation-retries.md) if your function returns an error\. Configure your function with a [destination](invocation-async.md#invocation-async-destinations) to retain events that your function could not process\.

You need to grant permission for the AWS IoT service to invoke your Lambda function\. Use the `add-permission` command to add a permission statement to your function's resource\-based policy\.

```
aws lambda add-permission --function-name my-function \
--statement-id iot-events --action "lambda:InvokeFunction" --principal iot.amazonaws.com
```

You should see the following output:

```
{
    "Statement": "{\"Sid\":\"iot-events\",\"Effect\":\"Allow\",\"Principal\":{\"Service\":\"iot.amazonaws.com\"},\"Action\":\"lambda:InvokeFunction\",\"Resource\":\"arn:aws:lambda:us-west-2:123456789012:function:my-function\"}"
}
```

For more information about how to use Lambda with AWS IoT, see [Creating an AWS Lambda rule](https://docs.aws.amazon.com/iot/latest/developerguide/iot-lambda-rule.html)\. 