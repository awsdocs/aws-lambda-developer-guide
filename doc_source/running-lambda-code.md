# AWS Lambda Execution Model<a name="running-lambda-code"></a>

When AWS Lambda executes your Lambda function on your behalf, it takes care of provisioning and managing resources needed to run your Lambda function\. When you create a Lambda function, you specify configuration information, such as the amount of memory and maximum execution time that you want to allow for your Lambda function\. When a Lambda function is invoked, AWS Lambda launches a container \(that is, an execution environment\) based on the configuration settings you provided\. 

**Note**  
The content of this section is for information only\. AWS Lambda manages container creations and deletion, there is no AWS Lambda API for you to manage containers\. 

It takes time to set up a container and do the necessary bootstrapping, which adds some latency each time the Lambda function is invoked\. You typically see this latency when a Lambda function is invoked for the first time or after it has been updated because AWS Lambda tries to reuse the container for subsequent invocations of the Lambda function\.

After a Lambda function is executed, AWS Lambda maintains the container for some time in anticipation of another Lambda function invocation\. In effect, the service freezes the container after a Lambda function completes, and thaws the container for reuse, if AWS Lambda chooses to reuse the container when the Lambda function is invoked again\. This container reuse approach has the following implications: 

+ Any declarations in your Lambda function code \(outside the `handler` code, see [Programming Model](programming-model-v2.md)\) remains initialized, providing additional optimization when the function is invoked again\. For example, if your Lambda function establishes a database connection, instead of reestablishing the connection, the original connection is used in subsequent invocations\. We suggest adding logic in your code to check if a connection exists before creating one\.

   

+ Each container provides 500MB of additional disk space in the `/tmp` directory\. The directory content remains when the container is frozen, providing transient cache that can be used for multiple invocations\. You can add extra code to check if the cache has the data that you stored\. For information on deployment limits, see [AWS Lambda Limits](limits.md)\.

   

+ Background processes or callbacks initiated by your Lambda function that did not complete when the function ended resume if AWS Lambda chooses to reuse the container\. You should make sure any background processes or callbacks \(in case of Node\.js\) in your code are complete before the code exits\.

**Note**  
When you write your Lambda function code, do not assume that AWS Lambda automatically reuses the container for subsequent function invocations\. Other factors may dictate a need for AWS Lambda to create a new container, which can lead to unexpected results, such as database connection failures\. As mentioned previous, add logic to your Lambda function code to check for the existence of a container\.