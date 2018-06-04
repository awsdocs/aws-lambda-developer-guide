# Step 2\.1: Create a Lambda Function Deployment Package<a name="with-userapp-walkthrough-custom-events-create-nodejs-function"></a>

Follow the instructions to create an AWS Lambda function deployment package\.

1. Open a text editor, and then copy the following code\. 

   ```
   console.log('Loading function');
   
   exports.handler = function(event, context, callback) {
       console.log('value1 =', event.key1);
       console.log('value2 =', event.key2);
       console.log('value3 =', event.key3);
       callback(null, "Success");
       
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes 6\.10 or 8\.10\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as `helloworld.js`\.

1. Zip the `helloworld.js` file as `helloworld.zip`\. 

**Note**  
To see more examples of using other AWS services within your function, including calling other Lambda functions, see [AWS SDK for JavaScript](http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/frames.html)