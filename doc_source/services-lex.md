# Using AWS Lambda with Amazon Lex<a name="services-lex"></a>

Amazon Lex is an AWS service for building conversational interfaces into applications using voice and text\. Amazon Lex provides pre\-build integration with AWS Lambda, allowing you to create Lambda functions for use as code hook with your Amazon Lex bot\. In your intent configuration, you can identify your Lambda function to perform initialization/validation, fulfillment, or both\. 

**Example Amazon Lex Message Event**  

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
    "name": "my-bot",
    "alias": "prod",
    "version": "1"
  },
  "outputDialogMode": "Text",
  "currentIntent": {
    "name": "my-intent",
    "slots": {
      "slot-name": "value",
      "slot-name": "value",
      "slot-name": "value"
    },
    "confirmationStatus": "Confirmed"
  }
}
```

For more information, see [Using Lambda Functions](https://docs.aws.amazon.com/lex/latest/dg/using-lambda.html)\. For an example use case, see [Exercise 1: Create Amazon Lex Bot Using a Blueprint](https://docs.aws.amazon.com/lex/latest/dg/gs-bp.html)\.