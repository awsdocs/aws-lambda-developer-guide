# Step 3\.2: Test Sending an HTTPS Request<a name="with-on-demand-https-example-configure-event-source-test-end-to-end_1"></a>

In this step, you will use the console to test the Lambda function\. In addition, you can run a `curl` command to test the end\-to\-end experience\. That is, send an HTTPS request to your API method and have Amazon API Gateway invoke your Lambda function\. In order to complete the steps, make sure you have created a DynamoDB table and named it "MyTable"\. For more information, see [Step 3\.1: Create a DynamoDB Table with a Stream Enabled](with-ddb-configure-ddb.md#with-ddb-create-buckets)

1. With your `MyLambdaMicroService` function still open in the console, choose the **Actions** tab and then choose **Configure test event**\.

1. Replace the existing text with the following:

   ```
   {
   	"httpMethod": "GET",
   	"queryStringParameters": {
   	"TableName": "MyTable"
       }
   }
   ```

1. After entering the text above choose **Save and test**\.