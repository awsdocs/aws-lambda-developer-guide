# Step 2\.4: \(Optional\) Create a Lambda Function Authored in C\#<a name="get-started-step5-optional"></a>

The AWS Lambda console blueprints provide sample code authored either in Python or Node\.js\. You can easily modify the example using the inline editor in the console\. However, if you want to author code for your Lambda function in C\#, there are no blueprints provided\. Also, there is no inline editor for you to write C\# code in the AWS Lambda console\. 

While the Lambda console does not offer editing for compiled languages such as Java and C\#, you can use your choice of IDEs, such as Visual Studio, to create and package your C\# code and libraries\. Once packaged as a ZIP file, you can use the AWS Lambda console to upload and test C\# Lambda functions and to view logs and metrics for them\.

In this section you create a Lambda function using the following C\# code example\. 

```
using Amazon.Lambda.Core; 
namespace LambdaFunctionExample{
public class Hello {
    public string MyHandler(int count, ILambdaContext context) {
        var logger = context.Logger;
        logger.Log("received : " + count);
        return count.ToString();
    }
  }
}
```

Your Lambda function handler signature should be of the format *Assembly::Namespace\.ClassName::MethodName*\. The programming model explains how to write your C\# code in detail, for example the input/output types AWS Lambda supports\. For more information about the programming model, see [Programming Model for Authoring Lambda Functions in C\#](dotnet-programming-model.md)\. For now, note the following about this code:

+ The handler in this example uses the `int` type for input and the `string` type for output\. 

  When you invoke this function you will pass a sample int \(for example, 123\)\. 

+ In this exercise you use the console to manually test this Lambda function\. The console always uses the `RequestResponse` invocation type \(synchronous\) and therefore you will see the response in the console\. 

+ The handler includes the optional `ILambdaContext` parameter\. In the code we use the `LambdaLogger` provided by the `Amazon.Lambda.Core.LambdaLogger` object to write log entries to CloudWatch logs\. For information about using the `ILambdaContext` object, see [The Context Object \(C\#\)](dotnet-context-object.md)\.

First, you need to package this code and any dependencies into a deployment package\. Then, you can use the Getting Started exercise to upload the package to create your Lambda function and test using the console\. For more information, see [Creating a Deployment Package \(C\#\)](lambda-dotnet-how-to-create-deployment-package.md)\.