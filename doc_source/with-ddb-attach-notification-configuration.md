# Add an Event Source in AWS Lambda<a name="with-ddb-attach-notification-configuration"></a>

Run the following AWS CLI `create-event-source-mapping` command\. After the command executes, note down the UUID\. You'll need this UUID to refer to the event source mapping in any commands, for example, when deleting the event source mapping\.

```
$ aws lambda create-event-source-mapping \
--region us-east-1 \
--function-name ProcessDynamoDBStream \
--event-source DynamoDB-stream-arn \
--batch-size 100 \
--starting-position TRIM_HORIZON \
--profile adminuser
```

**Note**  
 This creates a mapping between the specified DynamoDB stream and the Lambda function\. You can associate a DynamoDB stream with multiple Lambda functions, and associate the same Lambda function with multiple streams\. However, the Lambda functions will share the read throughput for the stream they share\. 

You can get the list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings \
--region us-east-1 \
--function-name ProcessDynamoDBStream \
--event-source DynamoDB-stream-arn \
--profile adminuser
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the stream\) and `OK` \(indicates AWS Lambda successfully read records from the stream and invoked your Lambda function\) indicate that there no issues\. If there are issues, you receive an error message\.