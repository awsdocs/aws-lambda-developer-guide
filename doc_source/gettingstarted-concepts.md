# AWS Lambda Concepts<a name="gettingstarted-concepts"></a>

AWS Lambda lets you run *functions* in a serverless environment to process events in the language of your choice\. Each instance of your function runs in an isolated execution context and processes one event at a time\. When it finishes processing the event, it returns a response and Lambda sends it another event\. Lambda automatically scales up the number of instances of your function to handle high numbers of events\.

For a hands\-on introduction to these concepts in your preferred programming language, see the following chapters\.
+ [Building Lambda Functions with Node\.js](programming-model.md)
+ [Building Lambda Functions with Python](python-programming-model.md)
+ [Building Lambda Functions with Ruby](lambda-ruby.md)
+ [Building Lambda Functions with Java](java-programming-model.md)
+ [Building Lambda Functions with Go](go-programming-model.md)
+ [Building Lambda Functions with C\#](dotnet-programming-model.md)
+ [Building Lambda Functions with PowerShell](powershell-programming-model.md)

**Topics**
+ [Function](#gettingstarted-concepts-function)
+ [Runtime](#gettingstarted-concepts-runtimes)
+ [Programming Model](#gettingstarted-concepts-programmingmodel)
+ [Concurrency](#gettingstarted-concepts-concurrency)
+ [Deployment Package](#gettingstarted-concepts-package)
+ [Trigger](#gettingstarted-concepts-trigger)
+ [Layers](#gettingstarted-concepts-layers)

## Function<a name="gettingstarted-concepts-function"></a>

A function is a resource that you can invoke to run your code in AWS Lambda\. A function has code that processes events, and a runtime that passes requests and responses between Lambda and the function code\. You provide the code, and you can use the [provided runtimes](lambda-runtimes.md) or create your own\.

## Runtime<a name="gettingstarted-concepts-runtimes"></a>

Lambda runtimes allow functions in different languages to run in the same base execution environment\. You configure your function to use a runtime that matches your programming language\. The runtime sits in between the Lambda service and your function code, relaying invocation events, context information, and responses between the two\. You can use runtimes provided by Lambda, or build your own\. For more information, see [AWS Lambda Runtimes](lambda-runtimes.md)\.

## Programming Model<a name="gettingstarted-concepts-programmingmodel"></a>

Authoring specifics vary between runtimes, but all runtimes share a common that defines the interface between your code and the runtime code\. You tell the runtime which method to run by defining a **handler** in the function configuration, and the runtime runs that method\. The runtime passes in objects to the handler that contain the invocation **event** and the **context**, such as the function name and request ID\.

The runtime captures **logging** output from your function and sends it to Amazon CloudWatch Logs\. You can use the standard logging functionality of your programming language\. If your function throws an **error**, the runtime returns that error to the client\.

**Note**  
Logging is subject to [CloudWatch Logs limits](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/cloudwatch_limits_cwl.html)\. Log data can be lost due to throttling or, in some cases, when the [execution context](running-lambda-code.md) is terminated\.

If your function exits without error, the runtime sends it another event\. The function's class stays in memory, so clients and variables declared outside of the handler method can be reused\. Your function also has access to local storage in the `/tmp` directory\.

Lambda scales your function by running additional instances of it as demand increases, and terminating instances as demand increases\. Unless noted otherwise, incoming requests may be processed out of order or concurrently\. Store your application's state in other services, and don't rely on instances of your function being long lived\. Use local storage and class\-level objects to increase performance, but keep the size of your deployment package and the amount of data that you transfer onto the execution environment to a minimum\.

## Concurrency<a name="gettingstarted-concepts-concurrency"></a>

When your function is invoked more quickly than a single instance of your function can process events, Lambda [scales up](scaling.md) by running additional instances\. Each instance of your function handles only one request at a time, so you don't need to worry about synchronizing threads or processes\. You can, however, use asynchronous language features to process batches of events in parallel, and save data to the `/tmp` directory for use in future invocations on the same instance\.

## Deployment Package<a name="gettingstarted-concepts-package"></a>

Your function's code consists of scripts or compiled programs and their dependencies\. When you author functions in the Lambda console or a toolkit, the client creates a ZIP archive of your code called a deployment package\. The client then sends the package to the Lambda service\. When you manage functions with the Lambda API, command line tools, or SDKs, you create the deployment package\. You also need to create a deployment package manually for compiled languages and to add dependencies to your function\.

For language specific instructions, see the following topics\.
+  [AWS Lambda Deployment Package in Node\.js](nodejs-create-deployment-pkg.md) 
+  [AWS Lambda Deployment Package in Python](lambda-python-how-to-create-deployment-package.md) 
+  [AWS Lambda Deployment Package in Ruby](ruby-package.md) 
+  [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md) 
+  [AWS Lambda Deployment Package in Go](lambda-go-how-to-create-deployment-package.md) 
+  [AWS Lambda Deployment Package in C\#](lambda-dotnet-how-to-create-deployment-package.md) 
+  [AWS Lambda Deployment Package in PowerShell](lambda-powershell-how-to-create-deployment-package.md) 

## Trigger<a name="gettingstarted-concepts-trigger"></a>

A trigger is a resource or configuration that invokes a Lambda function\. This includes AWS services that can be configured to invoke a function, applications that you develop, and event source mappings\. An event source mapping is a resource in Lambda that reads items from a stream or queue and invokes a function\.

For more information, see [Invoking AWS Lambda Functions](lambda-invocation.md) and [Using AWS Lambda with Other Services](lambda-services.md)\.

## Layers<a name="gettingstarted-concepts-layers"></a>

Lambda layers are a distribution mechanism for libraries, custom runtimes, and other function dependencies\. Layers let you manage your in\-development function code independently from the unchanging code and resources that it uses\. You can configure your function to use layers that you create, layers provided by AWS, or layers from other AWS customers\.

For more information, see [AWS Lambda Layers](configuration-layers.md)\.