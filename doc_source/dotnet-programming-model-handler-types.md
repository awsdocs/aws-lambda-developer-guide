# Lambda Function Handler \(C\#\)<a name="dotnet-programming-model-handler-types"></a>

When you create a Lambda function, you specify a handler that AWS Lambda can invoke when the service executes the function on your behalf\. 

You define a Lambda function handler as an instance or static method in a class\. If you want access to the Lambda context object, it is available by defining a method parameter of type *ILambdaContext*, an interface you can use to access information about the current execution, such as the name of the current function, the memory limit, execution time remaining, and logging\. 

```
returnType handler-name(inputType input, ILambdaContext context) {
   ...
}
```

In the syntax, note the following:
+  *inputType* – The first handler parameter is the input to the handler, which can be event data \(published by an event source\) or custom input that you provide such as a string or any custom data object\. 
+ *returnType* – If you plan to invoke the Lambda function synchronously \(using the `RequestResponse` invocation type\), you can return the output of your function using any of the supported data types\. For example, if you use a Lambda function as a mobile application backend, you are invoking it synchronously\. Your output data type will be serialized into JSON\. 

  If you plan to invoke the Lambda function asynchronously \(using the `Event` invocation type\), the `returnType` should be `void`\. For example, if you use AWS Lambda with event sources such as Amazon S3 or Amazon SNS, these event sources invoke the Lambda function using the `Event` invocation type\.

## Handling Streams<a name="dotnet-programming-model-handling-streams"></a>

Only the `System.IO.Stream` type is supported as an input parameter by default\. 

For example, consider the following C\# example code\. 

```
using System.IO;

namespace Example{

            
    public class Hello
    {
     public Stream MyHandler(Stream stream)
     {
       //function logic
     }
    }
}
```

In the example C\# code, the first handler parameter is the input to the handler \(MyHandler\), which can be event data \(published by an event source such as Amazon S3\) or custom input you provide such as a `Stream` \(as in this example\) or any custom data object\. The output is of type `Stream`\. 

## Handling Standard Data Types<a name="dotnet-programming-model-handling-standard-types"></a>

All other types, as listed below, require you to specify a serializer\.
+ Primitive \.NET types \(such as string or int\)\.
+ Collections and maps \- IList, IEnumerable, IList<T>, Array, IDictionary, IDictionary<TKey, TValue>
+ POCO types \(Plain old CLR objects\)
+ Predefined AWS event types
+ For asynchronous invocations the return\-type will be ignored by Lambda\. The return type may be set to void in such cases\.
+ If you are using \.NET asynchronous programming, the return type can be Task and Task<T> types and use `async` and `await` keywords\. For more information, see [Using Async in C\# Functions with AWS Lambda](#dot-net-async)\.

Unless your function input and output parameters are of type `System.IO.Stream`, you will need to serialize them\. AWS Lambda provides a default serializer that can be applied at the assembly or method level of your application, or you can define your own by implementing the `ILambdaSerializer` interface provided by the `Amazon.Lambda.Core` library\. For more information, see [Creating a Deployment Package \(C\#\)](lambda-dotnet-how-to-create-deployment-package.md)\.

 To add the default serializer attribute to a method, first add a dependency on `Amazon.Lambda.Serialization.Json` in your `project.json` file\. 

```
{
    "version": "1.0.0-*",
    "dependencies":{
        "Microsoft.NETCore.App": {
            "type": "platform",
            "version": "1.0.1"
        },
        "Amazon.Lambda.Serialization.Json": "1.3.0"
    },
    "frameworks": {
        "netcoreapp1.0": {
            "imports": "dnxcore50"
        }
    }
}
```

 The example below illustrates the flexibility you can leverage by specifying the default Json\.NET serializer on one method and another of your choosing on a different method:

```
public class ProductService{
    [LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]
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

## Handler Signatures<a name="dotnet-programming-model-handler-signatures"></a>

When creating Lambda functions, you have to provide a handler string that tells AWS Lambda where to look for the code to invoke\. In C\#, the format is:

 *ASSEMBLY::TYPE::METHOD* where:
+ *ASSEMBLY* is the name of the \.NET assembly file for your application\. When using the \.NET Core CLI to build your application, if you haven't set the assembly name using the `buildOptions.outputName` setting in project\.json, the *ASSEMBLY* name will be the name of the folder that contains your project\.json file\. For more information, see [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\. In this case, let's assume the folder name is `HelloWorldApp`\.
+ *TYPE* is the full name of the handler type, which consists of the *Namespace* and the *ClassName*\. In this case `Example.Hello`\.
+ *METHOD* is name of the function handler, in this case `MyHandler`\.

Ultimately, the signature will be of this format: *Assembly::Namespace\.ClassName::MethodName*

Again, consider the following example:

```
using System.IO;
{
  namespace Example
            
  public class Hello
  {
    public Stream MyHandler(Stream stream)
    {
       //function logic
    }
}
```

The handler string would be: `HelloWorldApp::Example.Hello::MyHandler` 

**Important**  
If the method specified in your handler string is overloaded, you must provide the exact signature of the method Lambda should invoke\. AWS Lambda will reject an otherwise valid signature if the resolution would require selecting among multiple \(overloaded\) signatures\. 

## Lambda Function Handler Restrictions<a name="dotnet-handler-restrictions"></a>

Note that there are some restrictions on the handler signature
+ It may not be `unsafe` and use pointer types in the handler signature, though `unsafe` context can be used inside the handler method and its dependencies\. For more information, see [unsafe \(C\# Reference\)](https://msdn.microsoft.com/en-us/library/chfa2zb8.aspx)\.
+ It may not pass a variable number of parameters using the `params` keyword, or use `ArgIterator` as an input or return parameter which is used to support variable number of parameters\.
+ The handler may not be a generic method \(e\.g\. IList<T> Sort<T>\(IList<T> input\)\)\.
+ Async handlers with signature `async void` are not supported\.

## Using Async in C\# Functions with AWS Lambda<a name="dot-net-async"></a>

If you know your Lambda function will require a long\-running process, such as uploading large files to Amazon S3 or reading a large stream of records from DynamoDB, you can take advantage of the async/await pattern\. By creating a handler with this signature, Lambda will execute the function synchronously and wait a maximum of 5 minutes for execution to complete before returning or timing out\. For example:

```
public async Task<Response> ProcessS3ImageResizeAsync(SimpleS3Event input)
{
   var response = await client.DoAsyncWork(input);
   return response;
}
```

If you use this pattern, there are some considerations you must take into account:
+ AWS Lambda will not support `async void` methods\.
+ If you create an async Lambda function without implementing the `await` operator, \.NET will issue a compiler warning and you will observe unexpected behavior\. For example, some async actions will execute while others won't\. Or some async actions won't complete before the function execution is complete\.

  ```
  public async Task ProcessS3ImageResizeAsync(SimpleS3Event event) // Compiler warning
  {
      client.DoAsyncWork(input); 
  }
  ```
+ Your Lambda function can include multiple async calls, which can be invoked in parallel\. You can use the `Task.WhenAll` and `Task.WhenAny` methods to work with multiple tasks\. To use the `Task.WhenAll` method, you pass a list of the operations as an array to the method\. Note that in the example below, if you neglect to include any operation to the array, that call may return before its operation completes\.

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
   
    // Lambda may return before printing all tests since we're only waiting for one to finish.
    await Task.WhenAny(task1, task2, task3);
  }
  ```