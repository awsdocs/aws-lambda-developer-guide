# Step 3\.1: Create an API Using Amazon API Gateway<a name="with-on-demand-https-example-configure-event-source_2"></a>

Follow the steps in this section to create a new Lambda function and an API Gateway endpoint to trigger it:

1. Sign in to the AWS Management Console and open the AWS Lambda console\.

1. Choose **Create Lambda function**\.

1. On the **Select blueprint** page, choose the **microservice\-http\-endpoint** blueprint\. You can use the **Filter ** to find it\.

1. The **Configure triggers** page will be populated with an API Gateway trigger\. The default API name that will be created is `LambdaMicroservice` \(You can change this name via the **API Name** field if you wish\)\.
**Note**  
When you complete the wizard and create your function, Lambda automatically creates a proxy resource named `MyLambdaMicroservice` \(your function name\) under the API name you selected\. For more information about proxy resources, see [Configure Proxy Integration for a Proxy Resource](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html)\. A proxy resource has an `AWS_PROXY` integration type and a catch\-all method `ANY`\. The `AWS_PROXY` integration type applies a default mapping template to pass through the entire request to the Lambda function and transforms the output from the Lambda function to HTTP responses\. The `ANY` method defines the same integration setup for all the supported methods, including `GET`, `POST`, `PATCH`, `DELETE `and others\. 

   After reviewing your trigger, choose **Next**\.

1. On the **Configure function** page, do the following:

   1. Review the preconfigured Lambda function configuration information, including:
      + **Runtime** is `Node.js 6.10`
      + Code authored in JavaScript is provided\. The code performs DynamoDB operations based on the method called and payload provided\.
      + **Handler** shows `index.handler`\. The format is: `filename.handler-function` 

   1. Enter the function name `MyLambdaMicroservice` in **Name**\.

   1. In **Role**, enter a role name for the new role that will be created\.
**Note**  
The **microservice\-http\-endpoint** blueprint pre\-populates the Simple Microservice permission policy template in the **Policy templates** field, to be added to your new role upon creation\. This automatically adds the requisite permissions attached to that policy to your new role\. For more information, see [Policy Templates](policy-templates.md)\.

1. Choose **Create function**\.

## Next Step<a name="with-on-demand-https-example-exe-role-next-step_2"></a>

 [Step 3\.2: Test Sending an HTTPS Request](with-on-demand-https-example-configure-event-source-test-end-to-end_1.md) 