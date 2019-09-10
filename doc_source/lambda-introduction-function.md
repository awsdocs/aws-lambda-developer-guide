# Managing AWS Lambda Functions<a name="lambda-introduction-function"></a>

A function is a resource that you can invoke to run your code in AWS Lambda\. A function has code that processes events, and a runtime that passes requests and responses between Lambda and the function code\. You provide the code, and you can use the [provided runtimes](lambda-runtimes.md) or create your own\.

You can author functions in the Lambda console—or with an IDE toolkit, command line tools, or SDKs\. The Lambda console provides a [code editor](code-editor.md) for noncompiled languages that lets you modify and test code quickly\. The [AWS CLI](with-userapp.md) gives you direct access to the Lambda API for advanced configuration and automation use cases\.

Your function's code consists of scripts or compiled programs and their dependencies\. When you author functions in the Lambda console or a toolkit, the client creates a ZIP archive of your code called a [deployment package](deployment-package-v2.md)\. The client then sends the package to the Lambda service\. When you manage functions with the Lambda API, command line tools, or SDKs, you create the deployment package\. You also need to create a deployment package manually for compiled languages and to add dependencies to your function\.

Authoring specifics vary between runtimes, but all runtimes share a common [programming model](programming-model-v2.md) that defines the interface between your code and the runtime code\. You tell the runtime which method to run by defining a handler in the function configuration, and the runtime runs that method\. The runtime passes in objects to the handler that contain the invocation event and the context, such as the function name and request ID\.

For a hands\-on introduction to these concepts in your preferred programming language, see the following chapters\.
+ [Building Lambda Functions with Node\.js](programming-model.md)
+ [Building Lambda Functions with Python](python-programming-model.md)
+ [Building Lambda Functions with Ruby](lambda-ruby.md)
+ [Building Lambda Functions with Java](java-programming-model.md)
+ [Building Lambda Functions with Go](go-programming-model.md)
+ [Building Lambda Functions with C\#](dotnet-programming-model.md)
+ [Building Lambda Functions with PowerShell](powershell-programming-model.md)

**Topics**
+ [Creating Functions Using the AWS Lambda Console Editor](code-editor.md)
+ [Using AWS Lambda with the AWS Command Line Interface](with-userapp.md)
+ [Programming Model](programming-model-v2.md)
+ [Creating a Deployment Package](deployment-package-v2.md)
+ [Accessing AWS Resources from a Lambda Function](accessing-resources.md)