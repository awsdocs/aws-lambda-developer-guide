# Create a Lambda Function Authored in Java<a name="get-started-step4-optional"></a>

The blueprints provide sample code authored either in Python or Node\.js\. You can easily modify the example using the inline editor in the console\. However, if you want to author code for your Lambda function in Java, there are no blueprints provided\. Also, there is no inline editor for you to write Java code in the AWS Lambda console\. 

That means, you must write your Java code and also create your deployment package outside the console\. After you create the deployment package, you can use the console to upload the package to AWS Lambda to create your Lambda function\. You can also use the console to test the function by manually invoking it\.

In this section you create a Lambda function using the following Java code example\. 

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Hello {
    public String myHandler(int myCount, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("received : " + myCount);
        return String.valueOf(myCount);
    }
}
```

The programming model explains how to write your Java code in detail, for example the input/output types AWS Lambda supports\. For more information about the programming model, see [Building Lambda Functions with Java](java-programming-model.md)\. For now, note the following about this code:
+ When you package and upload this code to create your Lambda function, you specify the `example.Hello::myHandler` method reference as the handler\. 
+ The handler in this example uses the `int` type for input and the `String` type for output\. 

  AWS Lambda supports input/output of JSON\-serializable types and InputStream/OutputStream types\. When you invoke this function you will pass a sample int \(for example, 123\)\. 
+ You can use the Lambda console to manually invoke this Lambda function\. The console always uses the `RequestResponse` invocation type \(synchronous\) and therefore you will see the response in the console\. 
+ The handler includes the optional `Context` parameter\. In the code we use the `LambdaLogger` provided by the `Context` object to write log entries to CloudWatch logs\. For information about using the `Context` object, see [AWS Lambda Context Object in Java](java-context-object.md)\.

First, you need to package this code and any dependencies into a deployment package\. Then, you can use the Getting Started exercise to upload the package to create your Lambda function and test using the console\. For more information creating a deployment package, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)\.