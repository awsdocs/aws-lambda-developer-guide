# Create a simple microservice using Lambda and API Gateway<a name="services-apigateway-blueprint"></a>

In this tutorial you will use the Lambda console to create a Lambda function, and an Amazon API Gateway endpoint to trigger that function\. You will be able to call the endpoint with any method \(`GET`, `POST`, `PATCH`, etc\.\) to trigger your Lambda function\. When the endpoint is called, the entire request will be passed through to your Lambda function\. Your function action will depend on the method you call your endpoint with: 
+ DELETE: delete an item from a DynamoDB table
+ GET: scan table and return all items
+ POST: Create an item
+ PUT: Update an item

## Create an API using Amazon API Gateway<a name="services-apigateway-blueprint-create"></a>

Follow the steps in this section to create a new Lambda function and an API Gateway endpoint to trigger it:

**To create an API**

1. Sign in to the AWS Management Console and open the AWS Lambda console\.

1. Choose **Create Lambda function**\.

1. Choose **Use a blueprint**\.

1. Enter **microservice** in the search bar\. Choose the **microservice\-http\-endpoint** blueprint\.

1. Configure your function with the following settings\.
   + **Name** – **lambda\-microservice**\.
   + **Role** – **Create a new role from AWS policy templates**\.
   + **Role name** – **lambda\-apigateway\-role**\.
   + **Policy templates** – **Simple microservice permissions**\.
   + **API** – **Create an API**\.
   + **API Type** – **HTTP API**\.
   + **Security** – **Open**\.

   Choose **Create function**\.

When you complete the wizard and create your function, Lambda creates a proxy resource named `lambda-microservice` under the API name you selected\. For more information about proxy resources, see [Configure proxy integration for a proxy resource](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html)\.

A proxy resource has an `AWS_PROXY` integration type and a catch\-all method `ANY`\. The `AWS_PROXY` integration type applies a default mapping template to pass through the entire request to the Lambda function and transforms the output from the Lambda function to HTTP responses\. The `ANY` method defines the same integration setup for all the supported methods, including `GET`, `POST`, `PATCH`, `DELETE `and others\. 

## Test sending an HTTP request<a name="services-apigateway-blueprint-test"></a>

In this step, you will use the console to test the Lambda function\. In addition, you can run a `curl` command to test the end\-to\-end experience\. That is, [send an HTTP request](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html) to your API method and have Amazon API Gateway invoke your Lambda function\. In order to complete the steps, make sure you have created a DynamoDB table and named it "MyTable"\. For more information, see [Create a DynamoDB table with a stream enabled](with-ddb-example.md#with-ddb-create-buckets)\.

**To test the API**

1. With your `lambda-microservice` function still open in the Lambda console, choose the **Test** tab\.

1. Choose **New event**\.

1. Choose the **Hello World** template\.

1. In **Name**, enter a name for the test event\.

1. In the text entry panel, replace the existing text with the following:

   ```
   {
   	"httpMethod": "GET",
   	"queryStringParameters": {
   	"TableName": "MyTable"
       }
   }
   ```

   This `GET` command scans your DynamoDB table and returns all items found\.

1. After entering the text above choose **Test**\.

Verify that the test succeeded\. In the function response, you should see the following:

```
{
  "statusCode": "200",
  "body": "{\"Items\":[],\"Count\":0,\"ScannedCount\":0}",
  "headers": {
    "Content-Type": "application/json"
  }
}
```