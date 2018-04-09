# Step 3: Create an API Using Amazon API Gateway and Test It<a name="with-on-demand-https-example-configure-event-source"></a>

In this step, you associate your Lambda function with a method in the API that you created using Amazon API Gateway and test the end\-to\-end experience\. That is, when an HTTPS request is sent to an API method, Amazon API Gateway invokes your Lambda function\.

First, you create an API \(`DynamoDBOperations`\) using Amazon API Gateway with one resource \(`DynamoDBManager`\) and one method \(`POST`\)\. You associate the `POST` method with your Lambda function\. Then, you test the end\-to\-end experience\.

## Step 3\.1: Create the API<a name="with-on-demand-https-create-api"></a>

Run the following `create-rest-api` command to create the `DynamoDBOperations` API for this tutorial\.

```
$ aws apigateway create-rest-api \
--name DynamoDBOperations \
--region region \
--profile profile
```

The following is an example response:

```
{
    "name": "DynamoDBOperations",
    "id": "api-id",
    "createdDate": 1447724091
}
```

Note the API ID\.

You also need the ID of the API root resource\. To get the ID, run the `get-resources` command\.

```
$ aws apigateway get-resources \
--rest-api-id api-id
```

The following is example response \(at this time you only have the root resource, but you add more resources in the next step\):

```
{
    "items": [
        {
            "path": "/",
            "id": "root-id"
        }
    ]
}
```

## Step 3\.2: Create a Resource \(DynamoDBManager\) in the API<a name="with-on-demand-https-create-resource"></a>

Run the following `create-resource` command to create a resource \(`DynamoDBManager`\) in the API that you created in the preceding section\.

```
$ aws apigateway create-resource \
--rest-api-id api-id \
--parent-id root-id \
--path-part DynamoDBManager
```

The following is an example response:

```
{
    "path": "/DynamoDBManager",
    "pathPart": "DynamoDBManager",
    "id": "resource-id",
    "parentId": "root-id"
}
```

Note the ID in the response\. This is the ID of the resource \(`DynamoDBManager`\) that you created\. 

## Step 3\.3: Create Method \(POST\) on the Resource<a name="with-on-demand-https-create-method"></a>

Run the following `put-method` command to create a method \(`POST`\) on the resource \(`DynamoDBManager`\) in your API \(`DynamoDBOperations`\)\.

```
$ aws apigateway put-method \
--rest-api-id api-id \
--resource-id resource-id \
--http-method POST \
--authorization-type NONE
```

We specify `NONE` for the `--authorization-type` parameter, which means that unauthenticated requests for this method are supported\. This is fine for testing but in production you should use either the key\-based or role\-base authentication\.

The following is an example response:

```
{
    "apiKeyRequired": false,
    "httpMethod": "POST",
    "authorizationType": "NONE"
}
```

## Step 3\.4: Set the Lambda Function as the Destination for the POST Method<a name="with-on-demand-https-integrate-method-with-function"></a>

Run the following command to set the Lambda function as the integration point for the `POST` method \(this is the method Amazon API Gateway invokes when you make an HTTPS request for the `POST` method endpoint\)\. 

```
$ aws apigateway put-integration \
--rest-api-id api-id \
--resource-id resource-id \
--http-method POST \
--type AWS \
--integration-http-method POST \
--uri arn:aws:apigateway:aws-region:lambda:path/2015-03-31/functions/arn:aws:lambda:aws-region:aws-acct-id:function:your-lambda-function-name/invocations
```

**Note**  
`--rest-api-id` is the ID of the API \(`DynamoDBOperations`\) that you created in Amazon API Gateway\.
`--resource-id` is the resource ID of the resource \(`DynamoDBManager`\) you created in the API
\-`-http-method` is the API Gateway method and `--integration-http-method` is the method that API Gateway uses to communicate with AWS Lambda\.
`--uri` is unique identifier for the endpoint to which Amazon API Gateway can send request\.

The following is an example response:

```
{
    "httpMethod": "POST",
    "type": "AWS",
    "uri": "arn:aws:apigateway:region:lambda:path/2015-03-31/functions/arn:aws:lambda:region:aws-acct-id:function:LambdaFunctionForAPIGateway/invocations",
    "cacheNamespace": "resource-id"
}
```

Set `content-type` of the `POST` method response and integration response to JSON as follows: 
+ Run the following command to set the `POST` method response to JSON\. This is the response type that your API method returns\.

  ```
  $ aws apigateway put-method-response \
  --rest-api-id api-id \
  --resource-id resource-id \
  --http-method POST \
  --status-code 200 \
  --response-models "{\"application/json\": \"Empty\"}"
  ```
+ Run the following command to set the `POST` method integration response to JSON\. This is the response type that Lambda function returns\.

  ```
  $ aws apigateway put-integration-response \
  --rest-api-id api-id \
  --resource-id resource-id \
  --http-method POST \
  --status-code 200 \
  --response-templates "{\"application/json\": \"\"}"
  ```

## Step 3\.5: Deploy the API<a name="with-on-demand-https-deploy-api-prod"></a>

In this step, you deploy the API that you created to a stage called `prod`\. 

```
$ aws apigateway create-deployment \
--rest-api-id api-id \
--stage-name prod
```

The following is an example response:

```
{
    "id": "deployment-id",
    "createdDate": 1447726017
}
```

## Step 3\.6: Grant Permissions that Allows Amazon API Gateway to Invoke the Lambda Function<a name="with-on-demand-https-add-permission"></a>

Now that you have an API created using Amazon API Gateway and you've deployed it, you can test\. First, you need to add permissions so that Amazon API Gateway can invoke your Lambda function when you send HTTPS request to the `POST` method\.

To do this, you need to add a permissions to the permissions policy associated with your Lambda function\. Run the following `add-permission` AWS Lambda command to grant the Amazon API Gateway service principal \(`apigateway.amazonaws.com`\) permissions to invoke your Lambda function \(`LambdaFunctionForAPIGateway`\)\. 

```
$ aws lambda add-permission \
--function-name LambdaFunctionOverHttps \
--statement-id apigateway-test-2 \
--action lambda:InvokeFunction \
--principal apigateway.amazonaws.com \
--source-arn "arn:aws:execute-api:region:aws-acct-id:api-id/*/POST/DynamoDBManager"
```

You must grant this permission to enable testing \(if you go to the Amazon API Gateway and choose **Test** to test the API method, you need this permission\)\. Note the `--source-arn` specifies a wildcard character \(\*\) as the stage value \(indicates testing only\)\. This allows you to test without deploying the API\.

Now, run the same command again, but this time you grant to your deployed API permissions to invoke the Lambda function\.

```
$ aws lambda add-permission \
--function-name LambdaFunctionOverHttps \
--statement-id apigateway-prod-2 \
--action lambda:InvokeFunction \
--principal apigateway.amazonaws.com \
--source-arn "arn:aws:execute-api:region:aws-acct-id:api-id/prod/POST/DynamoDBManager"
```

You grant this permission so that your deployed API has permissions to invoke the Lambda function\. Note that the `--source-arn` specifies a `prod` which is the stage name we used when deploying the API\.

## Step 3\.7: Test Sending an HTTPS Request<a name="with-on-demand-https-example-configure-event-source-test-end-to-end"></a>

In this step, you are ready to send an HTTPS request to the `POST` method endpoint\. You can use either Curl or a method \(`test-invoke-method`\) provided by Amazon API Gateway\.

If you want to test operations that your Lambda function supports on a DynamoDB table, first you need to create a table in Amazon DynamoDB `LambdaTable (Id)`, where Id is the hash key of string type\.

If you are testing the `echo` and `ping` operations that your Lambda function supports, you don't need to create the DynamoDB table\.

You can use Amazon API Gateway CLI commands to send an HTTPS `POST` request to the resource \(`DynamoDBManager`\) endpoint\. Because you deployed your Amazon API Gateway, you can use Curl to invoke the methods for the same operation\.

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

Run the `test-invoke-method` Amazon API Gateway command to send an HTTPS `POST` method request to the resource \(`DynamoDBManager`\) endpoint with the preceding JSON in the request body\.

```
$ aws apigateway test-invoke-method \
--rest-api-id api-id \
--resource-id resource-id \
--http-method POST \
--path-with-query-string "" \
--body "{\"operation\":\"create\",\"tableName\":\"LambdaTable\",\"payload\":{\"Item\":{\"Id\":\"1\",\"name\":\"Bob\"}}}"
```

Or, you can use the following Curl command:

```
curl -X POST -d "{\"operation\":\"create\",\"tableName\":\"LambdaTable\",\"payload\":{\"Item\":{\"Id\":\"1\",\"name\":\"Bob\"}}}" https://api-id.execute-api.aws-region.amazonaws.com/prod/DynamoDBManager
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

Run the `test-invoke-method` Amazon API Gateway CLI command to send an HTTPS `POST` method request to the resource \(`DynamoDBManager`\) endpoint using the preceding JSON in the request body\.

```
$ aws apigateway test-invoke-method \
--rest-api-id api-id \
--resource-id resource-id \
--http-method POST \
--path-with-query-string "" \
--body "{\"operation\":\"echo\",\"payload\":{\"somekey1\":\"somevalue1\",\"somekey2\":\"somevalue2\"}}"
```

Or, you can use the following Curl command:

```
curl -X POST -d "{\"operation\":\"echo\",\"payload\":{\"somekey1\":\"somevalue1\",\"somekey2\":\"somevalue2\"}}" https://api-id.execute-api.region.amazonaws.com/prod/DynamoDBManager
```