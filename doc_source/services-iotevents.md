# Using AWS Lambda with AWS IoT Events<a name="services-iotevents"></a>

AWS IoT Events monitors the inputs from multiple IoT sensors and applications to recognize event patterns\. Then it takes appropriate actions when events occur\. AWS IoT Events receives its inputs as JSON payloads from many sources\. AWS IoT Events supports simple events \(where each input triggers an event\) and complex events \(where multiple inputs must occur to trigger the event\)\.

To use AWS IoT Events, you define a detector model, which is a state\-machine model of your equipment or process\. In addition to states, you define inputs and events for the model\. You also define the actions to take when an event occurs\. Use a Lambda function for an action when you want to invoke another AWS service \(such as Amazon Connect\), or take actions in an external application \(such as your enterprise resource planning \(ERP\) application\)\. 

When the event occurs, AWS IoT Events invokes your Lambda function asynchronously\. It provides information about the detector model and the event that triggered the action\. The following example message event is based on the definitions in the AWS IoT Events [simple step\-by\-step example](https://docs.aws.amazon.com/iotevents/latest/developerguide/iotevents-simple-example.html)\. 

**Example AWS IoT Events message event**  

```
{
  "event: ":{
    "eventName": "myChargedEvent",
    "eventTime": 1567797571647,
    "payload":{
      "detector":{
         "detectorModelName": "AWS_IoTEvents_Hello_World1567793458261",
         "detectorModelVersion": "4",
         "keyValue": "100009"
      },
      "eventTriggerDetails":{
         "triggerType": "Message",
         "inputName": "AWS_IoTEvents_HelloWorld_VoltageInput",
         "messageId": "64c75a34-068b-4a1d-ae58-c16215dc4efd"
      },
      "actionExecutionId": "49f0f32f-1209-38a7-8a76-d6ca49dd0bc4",
      "state":{
         "variables": {},
         "stateName": "Charged",
         "timers": {}
      }
    }
  }
}
```

The event that is passed into the Lambda function includes the following fields: 
+ `eventName` – The name for this event in the detector model\. 
+ `eventTime` – The time that the event occurred\.
+ `detector` – The name and version of the detector model\. 
+ `eventTriggerDetails` – A description of the input that triggered the event\. 
+ `actionExecutionId` – The unique execution identifier of the action\.
+ `state` – The state of the detector model when the event occurred\. 
  + `stateName` – The name of the state in the detector model\. 
  + `timers` – Any timers that are set in this state\. 
  + `variables` – Any variable values that are set in this state\. 

You need to grant permission for the AWS IoT Events service to invoke your Lambda function\. Use the `add-permission` command to add a permission statement to your function's resource\-based policy\.

```
aws lambda add-permission --function-name my-function \
--statement-id iot-events --action "lambda:InvokeFunction" --principal iotevents.amazonaws.com
```

You should see the following output:

```
{
    "Statement": "{\"Sid\":\"iot-events\",\"Effect\":\"Allow\",\"Principal\":{\"Service\":\"iotevents.amazonaws.com\"},\"Action\":\"lambda:InvokeFunction\",\"Resource\":\"arn:aws:lambda:us-west-2:123456789012:function:my-function\"}"
}
```

For more information about using Lambda with AWS IoT Events, see [Using AWS IoT Events with other services](https://docs.aws.amazon.com/iotevents/latest/developerguide/iotevents-with-others.html)\.