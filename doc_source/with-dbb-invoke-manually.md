# Test the Lambda Function \(Invoke Manually\)<a name="with-dbb-invoke-manually"></a>

In this step, you invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and the following sample DynamoDB event\.

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
      "Records":[
         {
            "eventID":"1",
            "eventName":"INSERT",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "NewImage":{
                  "Message":{
                     "S":"New item!"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"111",
               "SizeBytes":26,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         },
         {
            "eventID":"2",
            "eventName":"MODIFY",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "NewImage":{
                  "Message":{
                     "S":"This item has changed"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "OldImage":{
                  "Message":{
                     "S":"New item!"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"222",
               "SizeBytes":59,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         },
         {
            "eventID":"3",
            "eventName":"REMOVE",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "OldImage":{
                  "Message":{
                     "S":"This item has changed"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"333",
               "SizeBytes":38,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         }
      ]
   }
   ```

1. Execute the following `invoke` command\. 

   ```
   $ aws lambda invoke \
   --invocation-type RequestResponse \
   --function-name ProcessDynamoDBStream \
   --region us-east-1 \
   --payload file://file-path/input.txt \
   --profile adminuser \
   outputfile.txt
   ```

   Note that the `invoke` command specifies the `RequestResponse` as the invocation type, which requests synchronous execution\. For more information, see [Invoke](API_Invoke.md)\. The function returns the string message \(message in the `context.succeed()` in the code\) in the response body\. 

1. Verify the output in the `outputfile.txt` file\.

   You can monitor the activity of your Lambda function in the AWS Lambda console\. 
   + The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\. Sign in to the AWS Management Console at [https://console\.aws\.amazon\.com/](https://console.aws.amazon.com/)\.
   +  For each graph you can also click the **logs** link to view the CloudWatch logs directly\.