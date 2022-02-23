# Lambda function handler in C\#<a name="csharp-handler"></a>

The Lambda function *handler* is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

You define a Lambda function handler as an instance or static method in a class\. For access to the Lambda context object, you can define a method parameter of type *ILambdaContext*\. You can use this to access information about the current invocation, such as the name of the function, memory limit, remaining execution time, and logging\. 

```
returnType handler-name(inputType input, ILambdaContext context) {
   ...
}
```

In the syntax, note the following:
+ *inputType* – The first handler parameter is the input to the handler\. This can be event data \(that an event source publishes\) or custom input that you provide, such as a string or any custom data object\.
+ *returnType* – If you plan to invoke the Lambda function synchronously \(using the `RequestResponse` invocation type\), you can return the output of your function using any of the supported data types\. For example, if you use a Lambda function as a mobile application backend, you are invoking it synchronously\. Your output data type is serialized into JSON\.

  If you plan to invoke the Lambda function asynchronously \(using the `Event` invocation type\), the `returnType` should be `void`\. For example, if you use Lambda with event sources such as Amazon Simple Storage Service \(Amazon S3\) or Amazon Simple Notification Service \(Amazon SNS\), these event sources invoke the Lambda function using the `Event` invocation type\.
+ `ILambdaContext context` – The second argument in the handler signature is optional\. It provides access to the [context object](csharp-context.md), which has information about the function and request\.

## Handling streams<a name="csharp-handler-streams"></a>

By default, Lambda supports only the `System.IO.Stream` type as an input parameter\.

For example, consider the following C\# example code\.

```
using System.IO;

namespace Example
{            
  public class Hello
  {
    public Stream MyHandler(Stream stream)
    {
       //function logic
    }
  }
}
```

In the example C\# code, the first handler parameter is the input to the handler \(MyHandler\)\. This can be event data \(published by an event source such as Amazon S3\) or custom input that you provide, such as a `Stream` \(as in this example\) or any custom data object\. The output is of type `Stream`\.

## Handling standard data types<a name="csharp-handler-types"></a>

All the following other types require you to specify a serializer:
+ Primitive \.NET types \(such as string or int\)
+ Collections and maps – IList, IEnumerable, IList<T>, Array, IDictionary, IDictionary<TKey, TValue>
+ POCO types \(Plain old CLR objects\)
+ Predefined AWS event types
+ For asynchronous invocations, Lambda ignores the return type\. In such cases, the return type may be set to void\.
+ If you are using \.NET asynchronous programming, the return type can be Task and Task<T> types and use `async` and `await` keywords\. For more information, see [Using async in C\# functions with Lambda](#csharp-handler-async)\.

Unless your function input and output parameters are of type `System.IO.Stream`, you must serialize them\. Lambda provides default serializers that can be applied at the assembly or method level of your application, or you can define your own by implementing the `ILambdaSerializer` interface provided by the `Amazon.Lambda.Core` library\. For more information, see [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md)\.

 To add the default serializer attribute to a method, first add a dependency on `Amazon.Lambda.Serialization.SystemTextJson` in your `.csproj` file\.

```
<Project Sdk="Microsoft.NET.Sdk">

      <PropertyGroup>
        <TargetFramework>net6.0</TargetFramework>
        <ImplicitUsings>enable</ImplicitUsings>
        <Nullable>enable</Nullable>
        <GenerateRuntimeConfigurationFiles>true</GenerateRuntimeConfigurationFiles>
        <AWSProjectType>Lambda</AWSProjectType>
        <!-- Makes the build directory similar to a publish directory and helps the AWS .NET Lambda Mock Test Tool find project dependencies. -->
        <CopyLocalLockFileAssemblies>true</CopyLocalLockFileAssemblies>
        <!-- Generate ready to run images during publishing to improve cold start time. -->
        <PublishReadyToRun>true</PublishReadyToRun>
      </PropertyGroup>
    
      <ItemGroup>
        <PackageReference Include="Amazon.Lambda.Core" Version="2.1.0 " />
        <PackageReference Include="Amazon.Lambda.Serialization.SystemTextJson" Version="2.2.0" />
      </ItemGroup>
    
    </Project>
```

The example below illustrates the flexibility you can leverage by specifying the default System\.Text\.Json serializer on one method and another of your choosing on a different method:

```
public class ProductService
      {
      [LambdaSerializer(typeof(Amazon.Lambda.Serialization.SystemTextJson.DefaultLambdaJsonSerializer))]
      public Product DescribeProduct(DescribeProductRequest request)
      {
        return catalogService.DescribeProduct(request.Id);
      }
     
     [LambdaSerializer(typeof(MyJsonSerializer))]
     public Customer DescribeCustomer(DescribeCustomerRequest request)
     {
        return customerService.DescribeCustomer(request.Id);
     }
}
```

### Source generation for JSON serialization<a name="json-source-generation"></a>

C\# 9 provides source generators that allow code generation during compilation\. Starting with \.NET 6, the native JSON library `System.Text.Json` can use source generators, allowing JSON parsing without the need for reflection APIs\. This can help improve cold start performance\.

**To use the source generator**

1. In your project, define an empty, partial class that derives from `System.Text.Json.Serialization.JsonSerializerContext`\.

1. Add the `JsonSerializable` attribute for each \.NET type that the source generator must generate serialization code for\.

**Example API Gateway integration leveraging source generation**  

```
using System.Collections.Generic;
using System.Net;
using System.Text.Json.Serialization;

using Amazon.Lambda.Core;
using Amazon.Lambda.APIGatewayEvents;
using Amazon.Lambda.Serialization.SystemTextJson;

[assembly:
LambdaSerializer(typeof(SourceGeneratorLambdaJsonSerializer<SourceGeneratorExa
mple.HttpApiJsonSerializerContext>))]

namespace SourceGeneratorExample;

[JsonSerializable(typeof(APIGatewayHttpApiV2ProxyRequest))]
[JsonSerializable(typeof(APIGatewayHttpApiV2ProxyResponse))]
public partial class HttpApiJsonSerializerContext : JsonSerializerContext
{
}

public class Functions
{
    public APIGatewayProxyResponse Get(APIGatewayHttpApiV2ProxyRequest
request, ILambdaContext context)
    {
        context.Logger.LogInformation("Get Request");
        var response = new APIGatewayHttpApiV2ProxyResponse
        {
            StatusCode = (int)HttpStatusCode.OK,
            Body = "Hello AWS Serverless",
            Headers = new Dictionary<string, string> { { "Content-Type",
"text/plain" } }
            };
            
            return response;
    }
}
```

When you invoke your function, Lambda uses the source\-generated JSON serialization code to handle the serialization of Lambda events and responses\.

## Handler signatures<a name="csharp-handler-signatures"></a>

When creating Lambda functions, you have to provide a handler string that tells Lambda where to look for the code to invoke\. In C\#, the format is:

 *ASSEMBLY::TYPE::METHOD* where:
+ *ASSEMBLY* is the name of the \.NET assembly file for your application\. When using the \.NET Core CLI to build your application, if you haven't set the assembly name using the `AssemblyName` property in the \.csproj file, the *ASSEMBLY* name is the \.csproj file name\. For more information, see [\.NET Core CLI](csharp-package-cli.md)\. In this case, let's assume that the \.csproj file is `HelloWorldApp.csproj`\.
+ *TYPE* is the full name of the handler type, which consists of the *Namespace* and the *ClassName*\. In this case `Example.Hello`\.
+ *METHOD* is name of the function handler, in this case `MyHandler`\.

Ultimately, the signature is of this format: *Assembly::Namespace\.ClassName::MethodName*

Consider the following example:

```
using System.IO;

namespace Example
{            
  public class Hello
  {
    public Stream MyHandler(Stream stream)
    {
       //function logic
    }
  }
}
```

The handler string would be: `HelloWorldApp::Example.Hello::MyHandler` 

**Important**  
If the method specified in your handler string is overloaded, you must provide the exact signature of the method that Lambda should invoke\. If the resolution would require selecting among multiple \(overloaded\) signatures, Lambda will reject an otherwise valid signature\. 

## Using top\-level statements<a name="top-level-statements"></a>

Starting with \.NET 6, you can write functions using *top\-level statements*\. Top\-level statements remove some of the boilerplate code required for \.NET projects, reducing the number of lines of code that you write\. For example, you can rewrite the previous example using top\-level statements:

```
using Amazon.Lambda.RuntimeSupport;

var handler = (Stream stream) =>
{
  //function logic
};

await LambdaBootstrapBuilder.Create(handler).Build().RunAsync();
```

 When using top\-level statements, you only include the `ASSEMBLY` name when providing the handler signature\. Continuing from the previous example, the handler string would be `HelloWorldApp`\. 

 By setting the handler to the assembly name Lambda will treat the assembly as an executable and execute it at startup\. You must add the NuGet package `Amazon.Lambda.RuntimeSupport` to the project so that the executable that runs at startup starts the Lambda runtime client\. 

## Serializing Lambda functions<a name="csharp-handler-serializer"></a>

For any Lambda functions that use input or output types other than a `Stream` object, you must add a serialization library to your application\. You can do this in the following ways:
+ Use the `Amazon.Lambda.Serialization.SystemTextJson` NuGet package\. This library uses the native \.NET Core JSON serializer to handle serialization\. This package provides a performance improvement over `Amazon.Lambda.Serialization.Json`, but note the limitations described in the [ Microsoft documentation](https://docs.microsoft.com/en-us/dotnet/standard/serialization/system-text-json-how-to?pivots=dotnet-core-3-1)\. This library is available for \.NET Core 3\.1 and later runtimes\.
+ Use the `Amazon.Lambda.Serialization.Json` NuGet package\. This library uses JSON\.NET to handle serialization\.
+ Create your own serialization library by implementing the `ILambdaSerializer` interface, which is available as part of the `Amazon.Lambda.Core` library\. The interface defines two methods:
  + `T Deserialize<T>(Stream requestStream);`

     You implement this method to deserialize the request payload from the `Invoke` API into the object that is passed to the Lambda function handler\.
  + `T Serialize<T>(T response, Stream responseStream);`\.

     You implement this method to serialize the result returned from the Lambda function handler into the response payload that the `Invoke` API operation returns\.

To use the serializer, you must add a dependency to your `MyProject.csproj` file\.

```
...
 <ItemGroup>
    <PackageReference Include="Amazon.Lambda.Serialization.SystemTextJson" Version="2.1.0" />
    <!-- or -->
    <PackageReference Include="Amazon.Lambda.Serialization.Json" Version="2.0.0" />
  </ItemGroup>
```

Next, you must define the serializer\. The following example defines the ` Amazon.Lambda.Serialization.SystemTextJson` serializer in the AssemblyInfo\.cs file\.

```
[assembly: LambdaSerializer(typeof(DefaultLambdaJsonSerializer))]
```

The following example defines the `Amazon.Lambda.Serialization.Json` serializer in the AssemblyInfo\.cs file\.

```
[assembly: LambdaSerializer(typeof(JsonSerializer))]
```

You can define a custom serialization attribute at the method level, which overrides the default serializer specified at the assembly level\.

```
public class ProductService{

    [LambdaSerializer(typeof(JsonSerializer))]
    public Product DescribeProduct(DescribeProductRequest request)
    {
      return catalogService.DescribeProduct(request.Id);
    }
   
   [LambdaSerializer(typeof(MyJsonSerializer))]
   public Customer DescribeCustomer(DescribeCustomerRequest request)
   {
      return customerService.DescribeCustomer(request.Id);
   }
}
```

## Lambda function handler restrictions<a name="csharp-handler-restrictions"></a>

Note that there are some restrictions on the handler signature\.
+ It may not be `unsafe` and use pointer types in the handler signature, though you can use `unsafe` context inside the handler method and its dependencies\. For more information, see [unsafe \(C\# Reference\)](https://msdn.microsoft.com/en-us/library/chfa2zb8.aspx) on the Microsoft Docs website\.
+ It may not pass a variable number of parameters using the `params` keyword, or use `ArgIterator` as an input or a return parameter, which is used to support a variable number of parameters\.
+ The handler may not be a generic method, for example, IList<T> Sort<T>\(IList<T> input\)\.
+ Async handlers with signature `async void` are not supported\.

## Using async in C\# functions with Lambda<a name="csharp-handler-async"></a>

If you know that your Lambda function will require a long\-running process, such as uploading large files to Amazon S3 or reading a large stream of records from Amazon DynamoDB, you can take advantage of the async/await pattern\. When you use this signature, Lambda invokes the function synchronously and waits for the function to return a response or for execution to time out\.

```
public async Task<Response> ProcessS3ImageResizeAsync(SimpleS3Event input)
{
   var response = await client.DoAsyncWork(input);
   return response;
}
```

If you use this pattern, consider the following:
+ Lambda does not support `async void` methods\.
+ If you create an async Lambda function without implementing the `await` operator, \.NET will issue a compiler warning and you will observe unexpected behavior\. For example, some async actions will run while others won't\. Or some async actions won't complete before the function invocation completes\.

  ```
  public async Task ProcessS3ImageResizeAsync(SimpleS3Event event) // Compiler warning
  {
      client.DoAsyncWork(input); 
  }
  ```
+ Your Lambda function can include multiple async calls, which can be invoked in parallel\. You can use the `Task.WhenAll` and `Task.WhenAny` methods to work with multiple tasks\. To use the `Task.WhenAll` method, you pass a list of the operations as an array to the method\. Note that in the following example, if you neglect to include any operation to the array, that call may return before its operation completes\.

  ```
  public async Task DoesNotWaitForAllTasks1()
  {
     // In Lambda, Console.WriteLine goes to CloudWatch Logs.
     var task1 = Task.Run(() => Console.WriteLine("Test1"));
     var task2 = Task.Run(() => Console.WriteLine("Test2"));
     var task3 = Task.Run(() => Console.WriteLine("Test3"));
   
     // Lambda may return before printing "Test2" since we never wait on task2.
     await Task.WhenAll(task1, task3);
  }
  ```

  To use the `Task.WhenAny` method, you again pass a list of operations as an array to the method\. The call returns as soon as the first operation completes, even if the others are still running\.

  ```
  public async Task DoesNotWaitForAllTasks2()
  {
    // In Lambda, Console.WriteLine goes to CloudWatch Logs.
    var task1 = Task.Run(() => Console.WriteLine("Test1"));
    var task2 = Task.Run(() => Console.WriteLine("Test2"));
    var task3 = Task.Run(() => Console.WriteLine("Test3"));
   
    // Lambda may return before printing all tests since we're waiting for only one to finish.
    await Task.WhenAny(task1, task2, task3);
  }
  ```