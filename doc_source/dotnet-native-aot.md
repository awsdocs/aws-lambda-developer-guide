# \.NET functions with native AOT compilation<a name="dotnet-native-aot"></a>

\.NET 7 supports native ahead\-of\-time \(AOT\) compilation\. With *native AOT*, you can compile your Lambda function code to a native runtime format, which removes the need to compile \.NET code at runtime\. Native AOT compilation can reduce the cold start time for Lambda functions that you write in \.NET\.

**Topics**
+ [Limitations](#dotnet-native-aot-limitations)
+ [Prerequisites](#dotnet-native-aot-prerequisites)
+ [Lambda runtime](#dotnet-native-aot-runtime)
+ [Set up your project](#dotnet-native-aot-runtime-setup)
+ [Edit your Lambda function code](#dotnet-native-aot-runtime-edit)
+ [Deploy your Lambda function](#dotnet-native-aot-deploy)
+ [Add support for complex types](#dotnet-native-aot-complex-types)
+ [Troubleshooting](#dotnet-native-aot-troubleshooting)

## Limitations<a name="dotnet-native-aot-limitations"></a>

There are limitations to functionality that you can include in native AOT functions\. For more information, see [Limitations of Native AOT deployment](https://learn.microsoft.com/dotnet/core/deploying/native-aot/#limitations-of-native-aot-deployment) on the Microsoft Learn website\.

## Prerequisites<a name="dotnet-native-aot-prerequisites"></a>

**Docker**  
You must compile your function with native AOT on the same operating system that your code will run on\. As a result, on any operating system other than Amazon Linux 2, you need Docker to develop Lambda functions that use native AOT\.

**\.NET 7 SDK**  
Native AOT compilation is a feature of \.NET 7\. You must install the [\.NET 7 SDK](https://dotnet.microsoft.com/en-us/download/dotnet/7.0) on your system, not only the runtime\.

**Amazon\.Lambda\.Tools**  
To create your Lambda functions, use the [https://www.nuget.org/packages/Amazon.Lambda.Tools](https://www.nuget.org/packages/Amazon.Lambda.Tools) [\.NET Core global tool](https://aws.amazon.com/blogs/developer/net-core-global-tools-for-aws/)\. The current version of the \.NET Core global tool for Lambda supports using Docker for native AOT\. To install Amazon\.Lambda\.Tools, run the following command:  

```
dotnet tool install -g Amazon.Lambda.Tools
```
For more information about the Amazon\.Lambda\.Tools \.NET Core global tool, see the [AWS Extensions for \.NET CLI](https://github.com/aws/aws-extensions-for-dotnet-cli) repository on GitHub\.

**Amazon\.Lambda\.Templates**  
To generate your Lambda function code, use the [https://www.nuget.org/packages/Amazon.Lambda.Templates](https://www.nuget.org/packages/Amazon.Lambda.Templates) NuGet package\. To install this template package, run the following command:  

```
dotnet new install Amazon.Lambda.Templates
```

## Lambda runtime<a name="dotnet-native-aot-runtime"></a>

Use the `provided.al2` custom runtime with the `x86_64` architecture to deploy a Lambda function that you build with native AOT compilation\. When you use a \.NET Lambda runtime, your application is compiled into Intermediate Language \(IL\) code\. At runtime, the just\-in\-time \(JIT\) compiler takes the IL code and compiles it into machine code as needed\. With a Lambda function that is compiled ahead of time with native AOT, the runtime environment doesn't include the \.NET SDK or \.NET runtime\. You compile your code into machine code before it runs\.

## Set up your project<a name="dotnet-native-aot-runtime-setup"></a>

Use the \.NET Core global tool for Lambda to create your new environment\. To initialize your project, run the following command:

```
dotnet new lambda.NativeAOT
```

## Edit your Lambda function code<a name="dotnet-native-aot-runtime-edit"></a>

The \.NET Core global tool for Lambda generates a basic Lambda function that accepts a `String` and returns a `String`\. Edit the function code as required for your use case\. For how to change the parameters and return types of your function, see [Add support for complex types](#dotnet-native-aot-complex-types)\.

## Deploy your Lambda function<a name="dotnet-native-aot-deploy"></a>

If you're using Windows or macOS, make sure that Docker is running\.

Then, to compile and deploy your Lambda function, run the following command:

```
dotnet lambda deploy-function
```

The `deploy-function` process automatically downloads a Docker image of Amazon Linux 2 to perform the native AOT compilation for your function\. After this container image has downloaded, the deploy process builds your function and then creates a `.zip` file that gets deployed into your AWS account\.

## Add support for complex types<a name="dotnet-native-aot-complex-types"></a>

The default Lambda function provides a basic starting point of a `String` parameter and a `String` return type\. To accept or return complex types, update your code to include functionality that generates serialization code at compile time, rather than at runtime\.

Add more JsonSerializable attributes to your custom serializer object definition as needed\.

### API Gateway example<a name="dotnet-native-aot-complex-types-apigateway-example"></a>

For example, to use Amazon API Gateway events, add a reference to the NuGet package `Amazon.Lambda.ApiGatewayEvents`\. Then, add the following `using` statement to your Lambda function source code:

```
using Amazon.Lambda.APIGatewayEvents;
```

Add the following attributes to your class definition of your custom serializer:

```
[JsonSerializable(typeof(APIGatewayHttpApiV2ProxyRequest))]
[JsonSerializable(typeof(APIGatewayHttpApiV2ProxyResponse))]
```

Update your function signature to the following:

```
public static async Task<APIGatewayHttpApiV2ProxyResponse> FunctionHandler(APIGatewayHttpApiV2ProxyRequest input, ILambdaContext context)
```

## Troubleshooting<a name="dotnet-native-aot-troubleshooting"></a>

**Error: Cross\-OS native compilation is not supported\.**  
Your version of the Amazon\.Lambda\.Tools \.NET Core global tool is out of date\. Update to the latest version and try again\.

**Docker: image operating system "linux" cannot be used on this platform\.**  
Docker on your system is configured to use Windows containers\. Swap to Linux containers to run the native AOT build environment\.

**Unhandled Exception: System\.ApplicationException: The serializer NativeAoT\.MyCustomJsonSerializerContext is missing a constructor that takes in JsonSerializerOptions object**  
If you encounter this error when you invoke your Lambda function, add an `rd.xml` file to your project, and then redeploy\.

For more information about common errors, see the [AWS NativeAOT for \.NET](https://github.com/awslabs/dotnet-nativeaot-labs#common-errors) repository on GitHub\.