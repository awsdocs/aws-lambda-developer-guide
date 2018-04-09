# Step 3\.3: \(Optional\) Try Other Blueprints<a name="get-started-step3-optional"></a>

You can optionally try the following exercises:
+ You used the **hello\-world\-python** blueprint in this Getting Started exercise\. This blueprint provides sample code authored in Python\. There is also the **hello\-world** blueprint that provides similar Lambda function code that is authored in Node\.js\.
+ Both the **hello\-world\-python** and the **hello\-world** blueprints process custom events\. For this Getting Started exercise, you used hand\-crafted sample event data\. Your can write Lambda functions to process events published by event sources such as Amazon S3 and DynamoDB\. This requires event source configuration in the console\. 

  For example, you can write a Lambda function to process Amazon S3 events\. Then, you configure Amazon S3 as the event source to publish object\-created events to AWS Lambda\. When you upload an object to your bucket, Amazon S3 detects the event and invokes your Lambda function\. Your Lambda function receives the event data as a parameter\. You can verify your Lambda function executed by reviewing the CloudWatch logs either in the Lambda console or the CloudWatch console\. 

  The Lambda console provide blueprint to set up an example Lambda function to process Amazon S3 events\. When creating a Lambda function in the console on the **Select blueprint** page, enter **s3** in the **Filter** box to search for a list of available blueprints\.

  For more information about working with different event sources, see [Use Cases](use-cases.md)\. 

**Next Step**  
 [What's Next?](get-started-what-next.md) 