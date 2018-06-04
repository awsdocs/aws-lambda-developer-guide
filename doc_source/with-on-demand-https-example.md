# Using AWS Lambda with Amazon API Gateway \(On\-Demand Over HTTPS\)<a name="with-on-demand-https-example"></a>

In this example you create a simple API \(`DynamoDBOperations`\) using Amazon API Gateway\. An Amazon API Gateway is a collection of resources and methods\. For this tutorial, you create one resource \(`DynamoDBManager`\) and define one method \(`POST`\) on it\. The method is backed by a Lambda function \(`LambdaFunctionOverHttps`\)\. That is, when you invoke the method through an HTTPS endpoint, Amazon API Gateway invokes the Lambda function\.

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
+ The following is a sample request payload for an `echo` operation\. You send an HTTPS POST request to the endpoint, using the following data in the request body\. 

  ```
  {
    "operation": "echo",
    "payload": {
      "somekey1": "somevalue1",
      "somekey2": "somevalue2"
    }
  }
  ```

You can also create and manage API endpoints from the AWS Lambda console\. For example, search for the **microservice** in the blueprints\. This tutorial does not use the console, instead it uses AWS CLI to provide you with more details of how the API works\.

**Note**  
API Gateway offers advanced capabilities, such as:   
**Pass through the entire request** – A Lambda function can receive the entire HTTP request \(instead of just the request body\) and set the HTTP response \(instead of just the response body\) using the `AWS_PROXY` integration type\.
**Catch\-all methods** – Map all methods of an API resource to a single Lambda function with a single mapping, using the `ANY` catch\-all method\.
**Catch\-all resources** – Map all sub\-paths of a resource to a Lambda function without any additional configuration using the new path parameter \(`{proxy+})`\.
To learn more about these API Gateway features, see [Configure Proxy Integration for a Proxy Resource](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html)\.

## Next Step<a name="with-on-demand-https-example-impl-summary-next-step"></a>

[Step 1: Prepare](with-on-demand-https-example-prepare.md)