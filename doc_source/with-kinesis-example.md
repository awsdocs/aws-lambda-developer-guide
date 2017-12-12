# Tutorial: Using AWS Lambda with Kinesis<a name="with-kinesis-example"></a>

In this tutorial, you create a Lambda function to consume events from an Kinesis stream\. 

 The tutorial is divided into two main sections: 

+  First, you perform the necessary setup to create a Lambda function and then you test it by invoking it manually using sample event data \(you don't need an Kinesis stream\)\. 

+ Second, you create an Kinesis stream \(event source\)\. You add an event source mapping in AWS Lambda to associate the stream with your Lambda function\. AWS Lambda starts polling the stream, you add test records to the stream using the Kinesis API, and then you verify that AWS Lambda executed your Lambda function\.

**Important**  
Both the Lambda function and the Kinesis stream must be in the same AWS region\. This tutorial assumes that you create these resources in the `us-west-2` region\.

In this tutorial, you use the AWS Command Line Interface to perform AWS Lambda operations such as creating a Lambda function, creating a stream, and adding records to the stream\. You use the AWS Lambda console to manually invoke the function before you create a Kinesis stream\. You verify return values and logs in the console UI\. 

## Next Step<a name="with-kinesis-example-impl-summary-next-step"></a>

[Step 1: Prepare](with-kinesis-example-prepare.md)