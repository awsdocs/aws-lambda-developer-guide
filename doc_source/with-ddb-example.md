# Tutorial: Using AWS Lambda with Amazon DynamoDB<a name="with-ddb-example"></a>

 In this tutorial, you create a Lambda function to consume events from a DynamoDB stream\. 

 The tutorial is divided into two main sections: 
+  First, you perform the necessary setup to create a Lambda function and then you test it by invoking it manually using sample event data\. 
+ Second, you create an DynamoDB stream\-enabled table and add an event source mapping in AWS Lambda to associate the stream with your Lambda function\. AWS Lambda starts polling the stream\. Then, you test the end\-to\-end setup\. As you create, update, and delete items from the table, Amazon DynamoDB writes records to the stream\. AWS Lambda detects the new records as it polls the stream and executes your Lambda function on your behalf\. 

**Important**  
Both the Lambda function and the DynamoDB stream must be in the same AWS region\. This tutorial assumes that you create these resources in the `us-east-1` region\.

In this tutorial, you use the AWS Command Line Interface to perform AWS Lambda operations such as creating a Lambda function, creating a stream, and adding records to the stream\. You use the AWS Lambda console to manually invoke the function before you create a DynamoDB stream\. You verify return values and logs in the console UI\. 

## Next Step<a name="wt-ddb-next-step"></a>

[Step 1: Prepare](with-ddb-prepare.md)