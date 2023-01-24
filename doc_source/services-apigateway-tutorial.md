# Tutorial: Using Lambda with API Gateway<a name="services-apigateway-tutorial"></a>

In this tutorial, you use Amazon API Gateway to create a REST API and a resource \(`DynamoDBManager`\)\. You define one method \(`POST`\) on the resource, and create a Lambda function \(`LambdaFunctionOverHttps`\) that backs the `POST` method\. That way, when you call the API through an HTTPS endpoint, API Gateway invokes the Lambda function\.

The `POST` method that you define on the `DynamoDBManager` resource supports the following Amazon DynamoDB operations:
+ Create, update, and delete an item\.
+ Read an item\.
+ Scan an item\.
+ Other operations \(echo, ping\), not related to DynamoDB, that you can use for testing\.

Using API Gateway with Lambda also provides advanced capabilities, such as:
+ **Full request passthrough** – Using the Lambda proxy \(`AWS_PROXY`\) integration type, a Lambda function can receive an entire HTTP request \(instead of just the request body\) and set the HTTP response \(instead of just the response body\)\.
+ **Catch\-all methods** – Using the `ANY` catch\-all method, you can map all methods of an API resource to a single Lambda function with a single mapping\.
+ **Catch\-all resources** – Using a greedy path variable \(`{proxy+}`\), you can map all sub\-paths of a resource to a Lambda function without any additional configuration\.

For more information about these API Gateway features, see [Set up a proxy integration with a proxy resource](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html) in the *API Gateway Developer Guide*\.

**Topics**
+ [Prerequisites](#services-apigateway-tutorial-prereqs)
+ [Create an execution role](#services-apigateway-tutorial-role)
+ [Create the function](#services-apigateway-tutorial-function)
+ [Test the function](#services-apigateway-tutorial-test)
+ [Create a REST API using API Gateway](#services-apigateway-tutorial-api)
+ [Create a DynamoDB table](#services-apigateway-tutorial-table)
+ [Test the setup](#services-apigateway-tutorial-test-setup)
+ [Clean up your resources](#cleanup)

## Prerequisites<a name="services-apigateway-tutorial-prereqs"></a>

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

## Create an execution role<a name="services-apigateway-tutorial-role"></a>

Create an [execution role](lambda-intro-execution-role.md)\. This AWS Identity and Access Management \(IAM\) role uses a custom policy to give your Lambda function permission to access the required AWS resources\. Note that you must first create the policy and then create the execution role\.

**To create a custom policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. Choose **Create Policy**\.

1. Choose the **JSON** tab, and then paste the following custom policy into the JSON editor\.

   ```
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "Stmt1428341300017",
         "Action": [
           "dynamodb:DeleteItem",
           "dynamodb:GetItem",
           "dynamodb:PutItem",
           "dynamodb:Query",
           "dynamodb:Scan",
           "dynamodb:UpdateItem"
         ],
         "Effect": "Allow",
         "Resource": "*"
       },
       {
         "Sid": "",
         "Resource": "*",
         "Action": [
           "logs:CreateLogGroup",
           "logs:CreateLogStream",
           "logs:PutLogEvents"
         ],
         "Effect": "Allow"
       }
     ]
   }
   ```

   This policy includes permissions for your function to access DynamoDB and Amazon CloudWatch Logs\.

1. Choose **Next: Tags**\.

1. Choose **Next: Review**\.

1. Under **Review policy**, for the policy **Name**, enter **lambda\-apigateway\-policy**\.

1. Choose **Create policy**\.

**To create an execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Choose **Create role**\.

1. For the type of trusted entity, choose **AWS service**\.

1. For the use case, choose **Lambda**\.

1. Choose **Next**\.

1. In the policy search box, enter **lambda\-apigateway\-policy**\.

1. In the search results, select the policy that you created \(`lambda-apigateway-policy`\), and then choose **Next**\.

1. Under **Role details**, for the **Role name**, enter **lambda\-apigateway\-role**\.

1. Choose **Create role**\.

1. On the **Roles** page, choose the name of your role \(`lambda-apigateway-role`\)\.

1. On the **Summary** page, copy the **Role ARN**\. You need this later in the tutorial\.



## Create the function<a name="services-apigateway-tutorial-function"></a>

The following code example receives an API Gateway event input and processes the messages that this input contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

------
#### [ Node\.js ]

**Example index\.js**  

```
console.log('Loading function');

var AWS = require('aws-sdk');
var dynamo = new AWS.DynamoDB.DocumentClient();

/**
 * Provide an event that contains the following keys:
 *
 *   - operation: one of the operations in the switch statement below
 *   - tableName: required for operations that interact with DynamoDB
 *   - payload: a parameter to pass to the operation being performed
 */
exports.handler = function(event, context, callback) {
    //console.log('Received event:', JSON.stringify(event, null, 2));

    var operation = event.operation;

    if (event.tableName) {
        event.payload.TableName = event.tableName;
    }

    switch (operation) {
        case 'create':
            dynamo.put(event.payload, callback);
            break;
        case 'read':
            dynamo.get(event.payload, callback);
            break;
        case 'update':
            dynamo.update(event.payload, callback);
            break;
        case 'delete':
            dynamo.delete(event.payload, callback);
            break;
        case 'list':
            dynamo.scan(event.payload, callback);
            break;
        case 'echo':
            callback(null, "Success");
            break;
        case 'ping':
            callback(null, "pong");
            break;
        default:
            callback(`Unknown operation: ${operation}`);
    }
};
```

**To create the function**

1. Save the code example as a file named `index.js`\.

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create a Lambda function using the `create-function` AWS Command Line Interface \(AWS CLI\) command\. For the `role` parameter, enter the execution role's Amazon Resource Name \(ARN\), which you copied earlier\.

   ```
   aws lambda create-function --function-name LambdaFunctionOverHttps \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::123456789012:role/service-role/lambda-apigateway-role
   ```

------
#### [ Python 3 ]

**Example LambdaFunctionOverHttps\.py**  

```
from __future__ import print_function

import boto3
import json

print('Loading function')


def handler(event, context):
    '''Provide an event that contains the following keys:

      - operation: one of the operations in the operations dict below
      - tableName: required for operations that interact with DynamoDB
      - payload: a parameter to pass to the operation being performed
    '''
    #print("Received event: " + json.dumps(event, indent=2))

    operation = event['operation']

    if 'tableName' in event:
        dynamo = boto3.resource('dynamodb').Table(event['tableName'])

    operations = {
        'create': lambda x: dynamo.put_item(**x),
        'read': lambda x: dynamo.get_item(**x),
        'update': lambda x: dynamo.update_item(**x),
        'delete': lambda x: dynamo.delete_item(**x),
        'list': lambda x: dynamo.scan(**x),
        'echo': lambda x: x,
        'ping': lambda x: 'pong'
    }

    if operation in operations:
        return operations[operation](event.get('payload'))
    else:
        raise ValueError('Unrecognized operation "{}"'.format(operation))
```

**To create the function**

1. Save the code example as a file named `LambdaFunctionOverHttps.py`\.

1. Create a deployment package\.

   ```
   zip function.zip LambdaFunctionOverHttps.py
   ```

1. Create a Lambda function using the `create-function` AWS Command Line Interface \(AWS CLI\) command\. For the `role` parameter, enter the execution role's Amazon Resource Name \(ARN\), which you copied earlier\.

   ```
   aws lambda create-function --function-name LambdaFunctionOverHttps \
   --zip-file fileb://function.zip --handler LambdaFunctionOverHttps.handler --runtime python3.8 \
   --role arn:aws:iam::123456789012:role/service-role/lambda-apigateway-role
   ```

------

## Test the function<a name="services-apigateway-tutorial-test"></a>

Test the Lambda function manually using the following sample event data\. You can invoke the function using the `invoke` AWS CLI command or by [using the Lambda console](configuration-function-common.md#configuration-common-test)\.

**To test the Lambda function \(AWS CLI\)**

1. Save the following JSON as a file named `input.txt`\.

   ```
   {
       "operation": "echo",
       "payload": {
           "somekey1": "somevalue1",
           "somekey2": "somevalue2"
       }
   }
   ```

1. Run the following `invoke` AWS CLI command\.

   ```
   aws lambda invoke --function-name LambdaFunctionOverHttps \
   --payload file://input.txt outputfile.txt --cli-binary-format raw-in-base64-out
   ```

   The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

1. Verify the output in the file `outputfile.txt`\.

## Create a REST API using API Gateway<a name="services-apigateway-tutorial-api"></a>

In this section, you create an API Gateway REST API \(`DynamoDBOperations`\) with one resource \(`DynamoDBManager`\) and one method \(`POST`\)\. You associate the `POST` method with your Lambda function\. Then, you test the setup\.

When your API method receives an HTTP request, API Gateway invokes your Lambda function\.

### Create the API<a name="with-on-demand-https-create-api"></a>

In the following steps, you create the `DynamoDBOperations` REST API using the API Gateway console\.

**To create the API**

1. Open the [API Gateway console](https://console.aws.amazon.com/apigateway)\.

1. Choose **Create API**\.

1. In the **REST API** box, choose **Build**\.

1. Under **Create new API**, choose **New API**\.

1. Under **Settings**, do the following:

   1. For **API name**, enter **DynamoDBOperations**\.

   1. For **Endpoint Type**, choose **Regional**\.

1. Choose **Create API**\.

### Create a resource in the API<a name="with-on-demand-https-create-resource"></a>

In the following steps, you create a resource named `DynamoDBManager` in your REST API\.

**To create the resource**

1. In the [API Gateway console](https://console.aws.amazon.com/apigateway), in the **Resources** tree of your API, make sure that the root \(`/`\) level is highlighted\. Then, choose **Actions**, **Create Resource**\.

1. Under **New child resource**, do the following:

   1. For **Resource Name**, enter **DynamoDBManager**\.

   1. Keep **Resource Path** set to `/dynamodbmanager`\.

1. Choose **Create Resource**\.

### Create a POST method on the resource<a name="with-on-demand-https-create-method"></a>

In the following steps, you create a `POST` method on the `DynamoDBManager` resource that you created in the previous section\.

**To create the method**

1. In the [API Gateway console](https://console.aws.amazon.com/apigateway), in the **Resources** tree of your API, make sure that `/dynamodbmanager` is highlighted\. Then, choose **Actions**, **Create Method**\.

1. In the small dropdown menu that appears under `/dynamodbmanager`, choose `POST`, and then choose the check mark icon\.

1. In the method's **Setup** pane, do the following:

   1. For **Integration type**, choose **Lambda Function**\.

   1. For **Lambda Region**, choose the same AWS Region as your Lambda function\.

   1. For **Lambda Function**, enter the name of your function \(**LambdaFunctionOverHttps**\)\.

   1. Select **Use Default Timeout**\.

   1. Choose **Save**\.

1. In the **Add Permission to Lambda Function** dialog box, choose **OK**\.

## Create a DynamoDB table<a name="services-apigateway-tutorial-table"></a>

Create the DynamoDB table that your Lambda function uses\.

**To create the DynamoDB table**

1. Open the [Tables page](https://console.aws.amazon.com/dynamodbv2#tables) of the DynamoDB console\.

1. Choose **Create table**\.

1. Under **Table details**, do the following:

   1. For **Table name**, enter **lambda\-apigateway**\.

   1. For **Partition key**, enter **id**, and keep the data type set as **String**\.

1. Under **Settings**, keep the **Default settings**\.

1. Choose **Create table**\.

## Test the setup<a name="services-apigateway-tutorial-test-setup"></a>

You're now ready to test the setup\. You can send requests to your `POST` method directly from the API Gateway console\. In this step, you use a `create` operation followed by an `update` operation\.

**To create an item in your DynamoDB table**

Your Lambda function can use the `create` operation to create an item in your DynamoDB table\.

1. In the [API Gateway console](https://console.aws.amazon.com/apigateway), choose the name of your REST API \(`DynamoDBOperations`\)\.

1. In the **Resources** tree, under `/dynamodbmanager`, choose your `POST` method\.

1. In the **Method Execution** pane, in the **Client** box, choose **Test**\.

1. In the **Method Test** pane, keep **Query Strings** and **Headers** empty\. For **Request Body**, paste the following JSON:

   ```
   {
     "operation": "create",
     "tableName": "lambda-apigateway",
     "payload": {
       "Item": {
         "id": "1234ABCD",
         "number": 5
       }
     }
   }
   ```

1. Choose **Test**\.

The test results should show status `200`, indicating that the `create` operation was successful\. To confirm, you can check that your DynamoDB table now contains an item with `"id": "1234ABCD"` and `"number": "5"`\.

**To update the item in your DynamoDB table**

You can also update items in the table using the `update` operation\.

1. In the [API Gateway console](https://console.aws.amazon.com/apigateway), return to your POST method's **Method Test** pane\.

1. In the **Method Test** pane, keep **Query Strings** and **Headers** empty\. In **Request Body**, paste the following JSON:

   ```
   {
       "operation": "update",
       "tableName": "lambda-apigateway",
       "payload": {
           "Key": {
               "id": "1234ABCD"
           },
           "AttributeUpdates": {
               "number": {
                   "Value": 10
               }
           }
       }
   }
   ```

1. Choose **Test**\.

The test results should show status `200`, indicating that the `update` operation was successful\. To confirm, you can check that your DynamoDB table now contains an updated item with `"id": "1234ABCD"` and `"number": "10"`\.

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

**To delete the API**

1. Open the [APIs page](https://console.aws.amazon.com/apigateway/main/apis) of the API Gateway console\.

1. Select the API you created\.

1. Choose **Actions**, **Delete**\.

1. Choose **Delete**\.

**To delete the DynamoDB table**

1. Open the [Tables page](https://console.aws.amazon.com/dynamodb/home#tables:) of the DynamoDB console\.

1. Select the table you created\.

1. Choose **Delete**\.

1. Enter **delete** in the text box\.

1. Choose **Delete**\.