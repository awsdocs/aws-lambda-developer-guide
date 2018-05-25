# Tutorial: Using AWS Lambda with Custom User Applications<a name="with-userapp-walkthrough-custom-events"></a>

In this tutorial, you use the AWS CLI to create and invoke a Lambda function and explore other AWS Lambda APIs\.

You'll do the following:
+  Create a Lambda function to process an event it receives as a parameter\. You use the following example Node\.js code to create your Lambda function\.

  ```
  console.log('Loading function');
  
  exports.handler = function(event, context, callback) {
      console.log('value1 =', event.key1);
      console.log('value2 =', event.key2);
      console.log('value3 =', event.key3);
      callback(null,"Success");
      
  };
  ```
**Note**  
The code sample is compliant with the Node\.js runtime v6\.10 or v8\.10\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

  The function is simple\. It processes incoming event data by logging it \(these logs are available in Amazon CloudWatch\), and in the request\-response model, you can request the log data be returned in the response\.
+  Simulate a user application that sends an event to your Lambda function by invoking your Lambda function manually using the following sample event data\.

  ```
  {
    "key1": "value1",
    "key2": "value2",
    "key3": "value3"
  }
  ```

**Note**  
This example is similar to the Getting Started exercise \(see [Getting Started](getting-started.md)\)\. The difference is that the Getting Started exercise provides a console\-based experience\. The console does many things for you, which simplifies the experience\. When using the AWS CLI, you get the experience of making the API calls, which can help you develop a better understanding of the AWS Lambda operations\. In addition to creating and invoking a Lambda function, you can explore other Lambda APIs\.

## Next Step<a name="with-userapp-walkthrough-custom-events-next-step"></a>

 [Step 1: Prepare](with-userapp-walkthrough-custom-events-deploy.md) 