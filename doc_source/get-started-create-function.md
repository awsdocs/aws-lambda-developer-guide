# Create a Simple Lambda Function<a name="get-started-create-function"></a>

Follow the steps in this section to create a simple Lambda function\.

**To create a Lambda function**

1. Sign in to the AWS Management Console and open the AWS Lambda console\.

1. Note that AWS Lambda offers a simple `Hello World` function upon introduction under the **How it works** label and includes a **Run** option, allowing you to invoke the function as a general introduction\. This tutorial introduces additional options you have to create, test and update your Lambda functions, as well as other features provided by the Lambda console and provides links to each, inviting you to explore each one in depth\. 

   Choose **Create a function** under the **Get Started** section to proceed\.
**Note**  
The console shows the **Get Started** page only if you do not have any Lambda functions created\. If you have created functions already, you will see the **Lambda > Functions** page\. On the list page, choose **Create a function** to go to the **Create function** page\. 

1. On the **Create function** page, you are presented with three options: 
   + **Author from scratch**
   + **Blueprints**
   + **Serverless Application Repository**

    For more information on using the Serverless Application Repository, see [What Is the AWS Serverless Application Repository?](http://docs.aws.amazon.com/serverlessrepo/latest/devguide//what-is-serverlessrepo.html) 

   1. If you'd like to review the blueprints, choose the **Blueprints** button, which will display the available blueprints\. You can also use the **Filter** to search for specific blueprints\. For example: 
      + Enter **S3** in **Filter** to get only the list of blueprints available to process Amazon S3 events\.
      + Enter **dynamodb** in **Filter** to get a list of available blueprints to process Amazon DynamoDB events\.

   1. For this Getting Started exercise, choose the **Author from scratch** button\.

1. In **Author from scratch**, do the following:
   + In **Name\***, specify your Lambda function name\.
   + In **Runtime\***, choose `Python 3.6`\.
   + In **Role\***, choose **Create new role from template\(s\):**
   + In **Role name\***, enter a name for your role\.
   + Leave the **Policy templates** field blank\. For the purposes of this introduction, your Lambda function will have the necessary execution permissions\.
**Note**  
For an in\-depth look at AWS Lambda's security polices, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)\.
   + Choose **Create Function\.**

1. Under your new ***function\-name*** page, note the following:   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/new_console1.png)

   In the **Add triggers** panel, you can optionally choose a service that automatically triggers your Lambda function by choosing one of the service options listed\.

   1. Depending on which service you select, you are prompted to provide relevant information for that service\. For example, if you select DynamoDB, you need to provide the following: 
      + The name of the DynamoDB table
      + Batch size
      + Starting position

   1. For this example, do not configure a trigger\.
   + In **Function code** note that code is provided\. It returns a simple "Hello from Lambda" greeting\.
   + **Handler** shows **lambda\_function\.lambda\_handler** value\. It is the *filename*\.*handler\-function*\. The console saves the sample code in the `lambda_function.py` file and in the code `lambda_handler` is the function name that receives the event as a parameter when the Lambda function is invoked\. For more information, see [Lambda Function Handler \(Python\)](python-programming-model-handler-types.md)\.
   + Note the embedded IDE \(Integrated Development Environment\)\. To learn more, see [Creating Functions Using the AWS Lambda Console Editor](code-editor.md)\.

1. Other configuration options on this page include:
   + **Environment variables** – for Lambda functions enable you to dynamically pass settings to your function code and libraries, without making changes to your code\. For more information, see [Environment Variables](env_variables.md)\.
   + **Tags** – are key\-value pairs that you attach to AWS resources to better organize them\. For more information, see [Tagging Lambda Functions](tagging.md)\.
   + **Execution role** – which allows you to administer security on your function, using defined roles and policies or creating new ones\. For more information, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)\.
   + **Basic settings** – allows you to dictate the memory allocation and timeout limit for your Lambda function\. For more information, see [AWS Lambda Limits](limits.md#limits-list)\.
   + **Network** – allows you to select a VPC your function will access\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\.
   + **Debugging and error handling** – allows you to select a [Dead Letter Queues](dlq.md) resource to analyze failed function invocation retries\. It also allows you to enable active tracing\. For more information, see [Using AWS X\-Ray](lambda-x-ray.md)\. 
   + **Concurrency** – allows you to allocate a specific limit of concurrent executions allowed for this function\. For more information, see [Function Level Concurrent Execution Limit](concurrent-executions.md#per-function-concurrency)\. 
   + **Auditing and compliance** – logs function invocations for operational and risk auditing, governance and compliance\. For more information, see [Using AWS Lambda with AWS CloudTrail](with-cloudtrail.md)\. 

## Invoke the Lambda Function Manually and Verify Results, Logs, and Metrics<a name="get-started-invoke-manually"></a>

Follow the steps to invoke your Lambda function using the sample event data provided in the console\.

1. On the ***yourfunction*** page, choose **Test**\.

1. In the **Configure test event** page, choose **Create new test event** and in **Event template**, leave the default **Hello World** option\. Enter an **Event name** and note the following sample event template:

   ```
   {
     "key3": "value3",
     "key2": "value2",
     "key1": "value1"
   }
   ```

   You can change key and values in the sample JSON, but don't change the event structure\. If you do change any keys and values, you must update the sample code accordingly\. 
**Note**  
If you choose to delete the test event, go to the **Configure test event** page and then choose **Delete**\.

1. Choose **Create** and then choose **Test**\. Each user can create up to 10 test events per function\. Those test events are not available to other users\.

1. AWS Lambda executes your function on your behalf\. The `handler` in your Lambda function receives and then processes the sample event\. 

1. Upon successful execution, view results in the console\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/getting-started-v2-execution-result.png)

   Note the following:
   + The **Execution result** section shows the execution status as **succeeded** and also shows the function execution results, returned by the `return` statement\.
**Note**  
The console always uses the `RequestResponse` invocation type \(synchronous invocation\) when invoking a Lambda function which causes AWS Lambda to return a response immediately\. For more information, see [Invocation Types](invocation-options.md)\.
   + The **Summary** section shows the key information reported in the **Log output** section \(the *REPORT* line in the execution log\)\.
   + The **Log output** section shows the log AWS Lambda generates for each execution\. These are the logs written to CloudWatch by the Lambda function\. The AWS Lambda console shows these logs for your convenience\.

   Note that the **Click here** link shows logs in the CloudWatch console\. The function then adds logs to Amazon CloudWatch in the log group that corresponds to the Lambda function\.

1. Run the Lambda function a few times to gather some metrics that you can view in the next step\.

1. Choose the **Monitoring** tab to view the CloudWatch metrics for your Lambda function\. This page shows the CloudWatch metrics\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/getting-started-v2-execution-metrics.png)

   Note the following:
   + The X\-axis shows the past 24 hours from the current time\.
   + Invocation count shows the number of invocations during this interval\.
   + Invocation duration shows how long it took for your Lambda function to run\. It shows minimum, maximum, and average time of execution\.
   + Invocation errors show the number of times your Lambda function failed\. You can compare the number of times your function executed and how many times it failed \(if any\)\.
   + Throttled invocation metrics show whether AWS Lambda throttled your Lambda function invocation\. For more information, see [AWS Lambda Limits](limits.md#limits-list)\.
   + Concurrent execution metrics show the number of concurrent invocations of your Lambda function\. For more information, see [Managing Concurrency](concurrent-executions.md)\.
   + The AWS Lambda console shows these CloudWatch metrics for your convenience\. You can see these metrics in the Amazon CloudWatch console by clicking any of these metrics\.

   For more information on these metrics and what they mean, see [AWS Lambda CloudWatch Metrics](monitoring-functions-metrics.md#lambda-cloudwatch-metrics)\.