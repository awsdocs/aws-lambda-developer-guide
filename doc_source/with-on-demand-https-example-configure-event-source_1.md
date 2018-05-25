# Create a Simple Microservice using Lambda and API Gateway<a name="with-on-demand-https-example-configure-event-source_1"></a>

In this exercise you will use the Lambda console to create a Lambda function \(`MyLambdaMicroservice`\), and an Amazon API Gateway endpoint to trigger that function\. You will be able to call the endpoint with any method \(`GET`, `POST`, `PATCH`, etc\.\) to trigger your Lambda function\. When the endpoint is called, the entire request will be passed through to your Lambda function\. Your function action will depend on the method you call your endpoint with: 
+ DELETE: delete an item from a DynamoDB table
+ GET: scan table and return all items
+ POST: Create an item
+ PUT: Update an item

## Next Step<a name="with-on-demand-https-example-exe-role-next-step_0"></a>

 [Step 3\.1: Create an API Using Amazon API Gateway](with-on-demand-https-example-configure-event-source_2.md) 