# Tutorial: Using AWS Lambda with Amazon SNS<a name="with-sns-example"></a>

 In this tutorial, you create a Lambda function in one AWS account to subscribe to an Amazon SNS topic in a separate AWS account\. 

 The tutorial is divided into three main sections: 

+  First, you perform the necessary setup to create a Lambda function\. 

+ Second, you create an Amazon SNS topic in a separate AWS account\. 

+ Third, you grant permissions from each account in order for the Lambda function to subscribe to the Amazon SNS topic\. Then, you test the end\-to\-end setup\. 

**Important**  
 This tutorial assumes that you create these resources in the `us-east-1` region\.

In this tutorial, you use the AWS Command Line Interface to perform AWS Lambda operations such as creating a Lambda function, creating an Amazon SNS topic and granting permissions to allow these two resources to access each other\. 

## Next Step<a name="wt-sns-next-step"></a>

[Step 1: Prepare](with-sns-example-prepare.md)