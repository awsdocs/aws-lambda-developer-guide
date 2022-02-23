# Using AWS Lambda with Amazon Lex<a name="services-lex"></a>

You can use Amazon Lex to integrate a conversation bot into your application\. The Amazon Lex bot provides a conversational interface with your users\. Amazon Lex provides prebuilt integration with Lambda, which enables you to use a Lambda function with your Amazon Lex bot\. 

When you configure an Amazon Lex bot, you can specify a Lambda function to perform validation, fulfillment, or both\. For validation, Amazon Lex invokes the Lambda function after each response from the user\. The Lambda function can validate the response and provide corrective feedback to the user, if necessary\. For fulfillment, Amazon Lex invokes the Lambda function to fulfill the user request after the bot successfully collects all of the required information and receives confirmation from the user\. 

You can [manage the concurrency](configuration-concurrency.md) of your Lambda function to control the maximum number of simultaneous bot conversations that you serve\. The Amazon Lex API returns an HTTP 429 status code \(Too Many Requests\) if the function is at maximum concurrency\. 

The API returns an HTTP 424 status code \(Dependency Failed Exception\) if the Lambda function throws an exception\. 

The Amazon Lex bot invokes your Lambda function [synchronously](invocation-sync.md)\. The event parameter contains information about the bot and the value of each slot in the dialog\. The `invocationSource` parameter indicates whether the Lambda function should validate the inputs \(DialogCodeHook\) or fulfill the intent \(FulfillmentCodeHook\)\.

**Example Amazon Lex message event**  

```
{
  "messageVersion": "1.0",
  "invocationSource": "FulfillmentCodeHook",
  "userId": "ABCD1234",
  "sessionAttributes": { 
     "key1": "value1",
     "key2": "value2",
  },
  "bot": {
    "name": "OrderFlowers",
    "alias": "prod",
    "version": "1"
  },
  "outputDialogMode": "Text",
  "currentIntent": {
    "name": "OrderFlowers",
    "slots": {
      "FlowerType": "lilies",
      "PickupDate": "2030-11-08",
      "PickupTime": "10:00"
    },
    "confirmationStatus": "Confirmed"
  }
}
```

Amazon Lex expects a response from a Lambda function in the following format\. The `dialogAction` field is required\. The `sessionAttributes` and the `recentIntentSummaryView` fields are optional\. 

**Example Amazon Lex message event**  

```
{
  "sessionAttributes": {
    "key1": "value1",
    "key2": "value2"
    ...
  },
  "recentIntentSummaryView": [
    {
       "intentName": "Name",
       "checkpointLabel": "Label",
       "slots": {
         "slot name": "value",
         "slot name": "value"
        },
        "confirmationStatus": "None, Confirmed, or Denied (intent confirmation, if configured)",
        "dialogActionType": "ElicitIntent, ElicitSlot, ConfirmIntent, Delegate, or Close",
        "fulfillmentState": "Fulfilled or Failed",
        "slotToElicit": "Next slot to elicit
    }
  ],
  "dialogAction": {
    "type": "Close",
    "fulfillmentState": "Fulfilled",
    "message": {
      "contentType": "PlainText",
      "content": "Thanks, your pizza has been ordered."
    },
    "responseCard": {
      "version": integer-value,
      "contentType": "application/vnd.amazonaws.card.generic",
      "genericAttachments": [
          {
             "title":"card-title",
             "subTitle":"card-sub-title",
             "imageUrl":"URL of the image to be shown",
             "attachmentLinkUrl":"URL of the attachment to be associated with the card",
             "buttons":[ 
                 {
                    "text":"button-text",
                    "value":"Value sent to server on button click"
                 }
             ]
          } 
      ] 
    }
  }
}
```

Note that the additional fields required for `dialogAction` vary based on the value of the `type` field\. For more information about the event and response fields, see [Lambda event and response format](https://docs.aws.amazon.com/lex/latest/dg/lambda-input-response-format.html) in the *Amazon Lex Developer Guide*\. For an example tutorial that shows how to use Lambda with Amazon Lex, see [Exercise 1: Create Amazon Lex bot using a blueprint](https://docs.aws.amazon.com/lex/latest/dg/gs-bp.html) in the *Amazon Lex Developer Guide*\.

## Roles and permissions<a name="services-cloudfront-permissions"></a>

You need to configure a service\-linked role as your function's [execution role](lambda-intro-execution-role.md)\. Amazon Lex defines the service\-linked role with predefined permissions\. When you create an Amazon Lex bot using the console, the service\-linked role is created automatically\. To create a service\-linked role with the AWS CLI, use the `create-service-linked-role` command\. 

```
aws iam create-service-linked-role --aws-service-name lex.amazonaws.com
```

This command creates the following role\.

```
{
  "Role": {
      "AssumeRolePolicyDocument": {
          "Version": "2012-10-17", 
          "Statement": [
              {
                  "Action": "sts:AssumeRole", 
                  "Effect": "Allow", 
                  "Principal": {
                      "Service": "lex.amazonaws.com"
                  }
              }
          ]
      },
      "RoleName": "AWSServiceRoleForLexBots", 
      "Path": "/aws-service-role/lex.amazonaws.com/", 
      "Arn": "arn:aws:iam::account-id:role/aws-service-role/lex.amazonaws.com/AWSServiceRoleForLexBots"
}
```

 If your Lambda function uses other AWS services, you need to add the corresponding permissions to the service\-linked role\. 

You use a resource\-based permissions policy to allow the Amazon Lex intent to invoke your Lambda function\. If you use the Amazon Lex console, the permissions policy is created automatically\. From the AWS CLI, use the Lambda `add-permission` command to set the permission\. The following example sets permission for the `OrderFlowers` intent\.

```
aws lambda add-permission \
    --function-name OrderFlowersCodeHook \
    --statement-id LexGettingStarted-OrderFlowersBot \
    --action lambda:InvokeFunction \
    --principal lex.amazonaws.com \
    --source-arn "arn:aws:lex:us-east-1:123456789012 ID:intent:OrderFlowers:*"
```