# Tutorial: Using AWS Lambda with Amazon API Gateway<a name="with-on-demand-https-example"></a>

In this example you create a simple API using Amazon API Gateway\. An Amazon API Gateway is a collection of resources and methods\. For this tutorial, you create one resource \(`DynamoDBManager`\) and define one method \(`POST`\) on it\. The method is backed by a Lambda function \(`LambdaFunctionOverHttps`\)\. That is, when you call the API through an HTTPS endpoint, Amazon API Gateway invokes the Lambda function\.

The `POST` method on the `DynamoDBManager` resource supports the following DynamoDB operations:
+ Create, update, and delete an item\.
+ Read an item\.
+ Scan an item\.
+ Other operations \(echo, ping\), not related to DynamoDB, that you can use for testing\.

The request payload you send in the `POST` request identifies the DynamoDB operation and provides necessary data\. For example: 
+ The following is a sample request payload for a DynamoDB create item operation:

  ```
  {
      "operation": "create",
      "tableName": "LambdaTable",
      "payload": {
          "Item": {
              "Id": "1",
              "name": "Bob"
          }
      }
  }
  ```
+ The following is a sample request payload for a DynamoDB read item operation:

  ```
  {
      "operation": "read",
      "tableName": "LambdaTable",
      "payload": {
          "Key": {
              "Id": "1"
          }
      }
  }
  ```
+ The following is a sample request payload for an `echo` operation\. You send an HTTP POST request to the endpoint, using the following data in the request body\. 

  ```
  {
    "operation": "echo",
    "payload": {
      "somekey1": "somevalue1",
      "somekey2": "somevalue2"
    }
  }
  ```

**Note**  
API Gateway offers advanced capabilities, such as:   
**Pass through the entire request** – A Lambda function can receive the entire HTTP request \(instead of just the request body\) and set the HTTP response \(instead of just the response body\) using the `AWS_PROXY` integration type\.
**Catch\-all methods** – Map all methods of an API resource to a single Lambda function with a single mapping, using the `ANY` catch\-all method\.
**Catch\-all resources** – Map all sub\-paths of a resource to a Lambda function without any additional configuration using the new path parameter \(`{proxy+})`\.
To learn more about these API Gateway features, see [Configure Proxy Integration for a Proxy Resource](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html)\.

## Prerequisites<a name="with-on-demand-https-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the Execution Role<a name="with-on-demand-https-create-execution-role"></a>

Create the [execution role](intro-permission-model.md#lambda-intro-execution-role) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – Lambda\.
   + **Role name** – **lambda\-apigateway\-role**\.
   + **Permissions** – Custom policy with permission to DynamoDB and CloudWatch Logs\.

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

The custom policy has the permissions that the function needs to write data to DynamoDB and upload logs\.

## Create the Function<a name="with-on-demand-https-example-create-function"></a>

The following example code receives a Kinesis event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Note**  
For sample code in other languages, see [Sample Function Code](with-on-demand-https-create-package.md)\.

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
            callback('Unknown operation: ${operation}');
    }
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   $ zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name LambdaFunctionOverHttps \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs8.10 \
   --role role-arn
   ```

## <a name="with-on-demand-https-example-upload-deployment-pkg_1"></a>

### Test the Lambda Function<a name="walkthrough-on-demand-https-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the function manually using the sample event data\. We recommend that you invoke the function using the console because the console UI provides a user\-friendly interface for reviewing the execution results, including the execution summary, logs written by your code, and the results returned by the function \(because the console always performs synchronous execution—invokes the Lambda function using the `RequestResponse` invocation type\)\.

**To test the Lambda function**

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
       "operation": "echo",
       "payload": {
           "somekey1": "somevalue1",
           "somekey2": "somevalue2"
       }
   }
   ```

1.  Execute the following `invoke` command:

   ```
   $ aws lambda  invoke --function-name LambdaFunctionOverHttps \
   --payload fileb://input.txt outputfile.txt
   ```

## Create an API Using Amazon API Gateway<a name="with-on-demand-https-example-configure-event-source"></a>

In this step, you associate your Lambda function with a method in the API that you created using Amazon API Gateway and test the end\-to\-end experience\. That is, when an HTTP request is sent to an API method, Amazon API Gateway invokes your Lambda function\.

First, you create an API \(`DynamoDBOperations`\) using Amazon API Gateway with one resource \(`DynamoDBManager`\) and one method \(`POST`\)\. You associate the `POST` method with your Lambda function\. Then, you test the end\-to\-end experience\.

### Create the API<a name="with-on-demand-https-create-api"></a>

Run the following `create-rest-api` command to create the `DynamoDBOperations` API for this tutorial\.

```
$ aws apigateway create-rest-api --name DynamoDBOperations 
{
    "name": "DynamoDBOperations",
    "id": "api-id",
    "createdDate": 1447724091
}
```

Note the API ID\. You also need the ID of the API root resource\. To get the ID, run the `get-resources` command\.

```
$ aws apigateway get-resources --rest-api-id api-id
{
    "items": [
        {
            "path": "/",
            "id": "root-id"
        }
    ]
}
```

At this time you only have the root resource, but you add more resources in the next step\.

### Create a Resource in the API<a name="with-on-demand-https-create-resource"></a>

Run the following `create-resource` command to create a resource \(`DynamoDBManager`\) in the API that you created in the preceding section\.

```
$ aws apigateway create-resource --rest-api-id api-id \
--parent-id root-id --path-part DynamoDBManager
{
    "path": "/DynamoDBManager",
    "pathPart": "DynamoDBManager",
    "id": "resource-id",
    "parentId": "root-id"
}
```

Note the ID in the response\. This is the ID of the `DynamoDBManager` resource that you created\. 

### Create POST Method on the Resource<a name="with-on-demand-https-create-method"></a>

Run the following `put-method` command to create a `POST` method on the `DynamoDBManager` resource in your API\.

```
$ aws apigateway put-method --rest-api-id api-id \
--resource-id resource-id --http-method POST --authorization-type NONE
{
    "apiKeyRequired": false,
    "httpMethod": "POST",
    "authorizationType": "NONE"
}
```

We specify `NONE` for the `--authorization-type` parameter, which means that unauthenticated requests for this method are supported\. This is fine for testing but in production you should use either the key\-based or role\-base authentication\.

### Set the Lambda Function as the Destination for the POST Method<a name="with-on-demand-https-integrate-method-with-function"></a>

Run the following command to set the Lambda function as the integration point for the `POST` method \(this is the method Amazon API Gateway invokes when you make an HTTP request for the `POST` method endpoint\)\. 

```
$ aws apigateway put-integration --rest-api-id api-id \
--resource-id resource-id --http-method POST \
--type AWS --integration-http-method POST \
--uri arn:aws:apigateway:aws-region:lambda:path/2015-03-31/functions/arn:aws:lambda:aws-region:aws-acct-id:function:LambdaFunctionOverHttps/invocations
{
    "httpMethod": "POST",
    "type": "AWS",
    "uri": "arn:aws:apigateway:region:lambda:path/2015-03-31/functions/arn:aws:lambda:region:aws-acct-id:function:LambdaFunctionOverHttps/invocations",
    "cacheNamespace": "resource-id"
}
```

`--integration-http-method` is the method that API Gateway uses to communicate with AWS Lambda\. `--uri` is unique identifier for the endpoint to which Amazon API Gateway can send request\.

Set `content-type` of the `POST` method response and integration response to JSON as follows: 
+ Run the following command to set the `POST` method response to JSON\. This is the response type that your API method returns\.

  ```
  $ aws apigateway put-method-response --rest-api-id api-id \
  --resource-id resource-id --http-method POST \
  --status-code 200 --response-models "{\"application/json\": \"Empty\"}"
  ```
+ Run the following command to set the `POST` method integration response to JSON\. This is the response type that Lambda function returns\.

  ```
  $ aws apigateway put-integration-response --rest-api-id api-id \
  --resource-id resource-id --http-method POST \
  --status-code 200 --response-templates "{\"application/json\": \"\"}"
  ```

### Deploy the API<a name="with-on-demand-https-deploy-api-prod"></a>

In this step, you deploy the API that you created to a stage called `prod`\. 

```
$ aws apigateway create-deployment --rest-api-id api-id --stage-name prod
{
    "id": "deployment-id",
    "createdDate": 1447726017
}
```

## Grant Invoke Permission to the API<a name="with-on-demand-https-add-permission"></a>

Now that you have an API created using Amazon API Gateway and you've deployed it, you can test\. First, you need to add permissions so that Amazon API Gateway can invoke your Lambda function when you send HTTP request to the `POST` method\.

To do this, you need to add a permissions to the permissions policy associated with your Lambda function\. Run the following `add-permission` AWS Lambda command to grant the Amazon API Gateway service principal \(`apigateway.amazonaws.com`\) permissions to invoke your Lambda function \(`LambdaFunctionOverHttps`\)\. 

```
$ aws lambda add-permission --function-name LambdaFunctionOverHttps \
--statement-id apigateway-test-2 --action lambda:InvokeFunction \
--principal apigateway.amazonaws.com \
--source-arn "arn:aws:execute-api:region:aws-acct-id:api-id/*/POST/DynamoDBManager"
```

You must grant this permission to enable testing \(if you go to the Amazon API Gateway and choose **Test** to test the API method, you need this permission\)\. Note the `--source-arn` specifies a wildcard character \(\*\) as the stage value \(indicates testing only\)\. This allows you to test without deploying the API\.

Now, run the same command again, but this time you grant to your deployed API permissions to invoke the Lambda function\.

```
$ aws lambda add-permission --function-name LambdaFunctionOverHttps \
--statement-id apigateway-prod-2 --action lambda:InvokeFunction \
--principal apigateway.amazonaws.com \
--source-arn "arn:aws:execute-api:region:aws-acct-id:api-id/prod/POST/DynamoDBManager"
```

You grant this permission so that your deployed API has permissions to invoke the Lambda function\. Note that the `--source-arn` specifies a `prod` which is the stage name we used when deploying the API\.

## Trigger the Function with an HTTP Request<a name="with-on-demand-https-example-configure-event-source-test-end-to-end"></a>

In this step, you are ready to send an HTTP request to the `POST` method endpoint\. You can use either Curl or a method \(`test-invoke-method`\) provided by Amazon API Gateway\.

If you want to test operations that your Lambda function supports on a DynamoDB table, first you need to create a table in Amazon DynamoDB `LambdaTable (Id)`, where Id is the hash key of string type\.

If you are testing the `echo` and `ping` operations that your Lambda function supports, you don't need to create the DynamoDB table\.

You can use Amazon API Gateway CLI commands to send an HTTP `POST` request to the resource \(`DynamoDBManager`\) endpoint\. Because you deployed your Amazon API Gateway, you can use Curl to invoke the methods for the same operation\.

The Lambda function supports using the `create` operation to create an item in your DynamoDB table\. To request this operation, use the following JSON:

```
{
    "operation": "create",
    "tableName": "LambdaTable",
    "payload": {
        "Item": {
            "Id": "foo",
            "number": 5
        }
    }
}
```

Run the `test-invoke-method` Amazon API Gateway command to send an HTTP `POST` method request to the resource \(`DynamoDBManager`\) endpoint with the preceding JSON in the request body\.

```
$ aws apigateway test-invoke-method --rest-api-id api-id \
--resource-id resource-id --http-method POST --path-with-query-string "" \
--body "{\"operation\":\"create\",\"tableName\":\"LambdaTable\",\"payload\":{\"Item\":{\"Id\":\"1\",\"name\":\"Bob\"}}}"
```

Or, you can use the following Curl command:

```
$ curl -X POST -d "{\"operation\":\"create\",\"tableName\":\"LambdaTable\",\"payload\":{\"Item\":{\"Id\":\"1\",\"name\":\"Bob\"}}}" https://api-id.execute-api.aws-region.amazonaws.com/prod/DynamoDBManager
```

To send request for the `echo` operation that your Lambda function supports, you can use the following request payload:

```
{
  "operation": "echo",
  "payload": {
    "somekey1": "somevalue1",
    "somekey2": "somevalue2"
  }
}
```

Run the `test-invoke-method` Amazon API Gateway CLI command to send an HTTP `POST` method request to the resource \(`DynamoDBManager`\) endpoint using the preceding JSON in the request body\.

```
$ aws apigateway test-invoke-method --rest-api-id api-id \
--resource-id resource-id --http-method POST --path-with-query-string "" \
--body "{\"operation\":\"echo\",\"payload\":{\"somekey1\":\"somevalue1\",\"somekey2\":\"somevalue2\"}}"
```

Or, you can use the following Curl command:

```
$ curl -X POST -d "{\"operation\":\"echo\",\"payload\":{\"somekey1\":\"somevalue1\",\"somekey2\":\"somevalue2\"}}" https://api-id.execute-api.region.amazonaws.com/prod/DynamoDBManager
```