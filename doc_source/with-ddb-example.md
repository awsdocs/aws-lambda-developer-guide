# Tutorial: Using AWS Lambda with Amazon DynamoDB streams<a name="with-ddb-example"></a>

 In this tutorial, you create a Lambda function to consume events from an Amazon DynamoDB stream\.

## Prerequisites<a name="with-ddb-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
aws --version
```

You should see the following output:

```
aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\.

**Note**  
On Windows, some Bash CLI commands that you commonly use with Lambda \(such as `zip`\) are not supported by the operating system's built\-in terminals\. To get a Windows\-integrated version of Ubuntu and Bash, [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. 

## Create the execution role<a name="with-ddb-create-execution-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – Lambda\.
   + **Permissions** – **AWSLambdaDynamoDBExecutionRole**\.
   + **Role name** – **lambda\-dynamodb\-role**\.

The **AWSLambdaDynamoDBExecutionRole** has the permissions that the function needs to read items from DynamoDB and write logs to CloudWatch Logs\.

## Create the function<a name="with-ddb-example-create-function"></a>

The following example code receives a DynamoDB event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Note**  
For sample code in other languages, see [Sample function code](with-ddb-create-package.md)\.

**Example index\.js**  

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
    console.log(JSON.stringify(event, null, 2));
    event.Records.forEach(function(record) {
        console.log(record.eventID);
        console.log(record.eventName);
        console.log('DynamoDB Record: %j', record.dynamodb);
    });
    callback(null, "message");
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   aws lambda create-function --function-name ProcessDynamoDBRecords \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::123456789012:role/lambda-dynamodb-role
   ```

## Test the Lambda function<a name="with-dbb-invoke-manually"></a>

In this step, you invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and the following sample DynamoDB event\.

**Example input\.txt**  

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

Run the following `invoke` command\. 

```
aws lambda invoke --function-name ProcessDynamoDBRecords --payload file://input.txt outputfile.txt
```

The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

The function returns the string `message` in the response body\. 

Verify the output in the `outputfile.txt` file\.

## Create a DynamoDB table with a stream enabled<a name="with-ddb-create-buckets"></a>

Create an Amazon DynamoDB table with a stream enabled\.

**To create a DynamoDB table**

1. Open the [DynamoDB console](https://console.aws.amazon.com/dynamodb)\.

1. Choose **Create table**\.

1. Create a table with the following settings\.
   + **Table name** – **lambda\-dynamodb\-stream**
   + **Primary key** – **id** \(string\)

1. Choose **Create**\.

**To enable streams**

1. Open the [DynamoDB console](https://console.aws.amazon.com/dynamodb)\.

1. Choose **Tables**\.

1. Choose the **lambda\-dynamodb\-stream** table\.

1. Under **Exports and streams**, choose **DynamoDB stream details**\.

1. Choose **Enable**\.

1. Choose **Enable stream**\.

Write down the stream ARN\. You need this in the next step when you associate the stream with your Lambda function\. For more information on enabling streams, see [Capturing table activity with DynamoDB Streams](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)\.

## Add an event source in AWS Lambda<a name="with-ddb-attach-notification-configuration"></a>

Create an event source mapping in AWS Lambda\. This event source mapping associates the DynamoDB stream with your Lambda function\. After you create this event source mapping, AWS Lambda starts polling the stream\.

Run the following AWS CLI `create-event-source-mapping` command\. After the command runs, note down the UUID\. You'll need this UUID to refer to the event source mapping in any commands, for example, when deleting the event source mapping\.

```
aws lambda create-event-source-mapping --function-name ProcessDynamoDBRecords \
 --batch-size 100 --starting-position LATEST --event-source DynamoDB-stream-arn
```

 This creates a mapping between the specified DynamoDB stream and the Lambda function\. You can associate a DynamoDB stream with multiple Lambda functions, and associate the same Lambda function with multiple streams\. However, the Lambda functions will share the read throughput for the stream they share\. 

You can get the list of event source mappings by running the following command\.

```
aws lambda list-event-source-mappings
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the stream\) and `OK` \(indicates AWS Lambda successfully read records from the stream and invoked your Lambda function\) indicate that there are no issues\. If there are issues, you receive an error message\.

If you have a lot of event source mappings, use the function name parameter to narrow down the results\.

```
aws lambda list-event-source-mappings --function-name ProcessDynamoDBRecords
```

## Test the setup<a name="with-ddb-final-integration-test-no-iam"></a>

Test the end\-to\-end experience\. As you perform table updates, DynamoDB writes event records to the stream\. As AWS Lambda polls the stream, it detects new records in the stream and invokes your Lambda function on your behalf by passing events to the function\. 

1. In the DynamoDB console, add, update, and delete items to the table\. DynamoDB writes records of these actions to the stream\.

1. AWS Lambda polls the stream and when it detects updates to the stream, it invokes your Lambda function by passing in the event data it finds in the stream\.

1. Your function runs and creates logs in Amazon CloudWatch\. You can verify the logs reported in the Amazon CloudWatch console\.

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the DynamoDB table**

1. Open the [Tables page](https://console.aws.amazon.com/dynamodb/home#tables:) of the DynamoDB console\.

1. Select the table you created\.

1. Choose **Delete**\.

1. Enter **delete** in the text box\.

1. Choose **Delete**\.