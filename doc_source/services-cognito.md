# Using AWS Lambda with Amazon Cognito<a name="services-cognito"></a>

The Amazon Cognito Events feature enables you to run Lambda functions in response to events in Amazon Cognito\. For example, you can invoke a Lambda function for the Sync Trigger events, that is published each time a dataset is synchronized\. To learn more and walk through an example, see [Introducing Amazon Cognito Events: Sync Triggers](http://aws.amazon.com/blogs/mobile/introducing-amazon-cognito-events-sync-triggers/) in the Mobile Development blog\. 

**Example Amazon Cognito message event**  

```
{
  "datasetName": "datasetName",
  "eventType": "SyncTrigger",
  "region": "us-east-1",
  "identityId": "identityId",
  "datasetRecords": {
    "SampleKey2": {
      "newValue": "newValue2",
      "oldValue": "oldValue2",
      "op": "replace"
    },
    "SampleKey1": {
      "newValue": "newValue1",
      "oldValue": "oldValue1",
      "op": "replace"
    }
  },
  "identityPoolId": "identityPoolId",
  "version": 2
}
```

You configure event source mapping using Amazon Cognito event subscription configuration\. For information about event source mapping and a sample event, see [Amazon Cognito events](https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-events.html) in the *Amazon Cognito Developer Guide*\.