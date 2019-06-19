# \.NET Core CLI<a name="lambda-dotnet-coreclr-deployment-package"></a>

The \.NET Core CLI offers a cross\-platform way for you to create \.NET\-based Lambda applications\. This section assumes you have installed the \.NET Core CLI\. If you haven't, do so [here](https://www.microsoft.com/net/download/windows)\.

In the \.NET CLI, you use the `new` command to create \.NET projects from a command line\. This is particularly useful if you want to create a platform\-independent project outside of Visual Studio\. To view a list of the available project types, open a command line and navigate to where you installed the \.NET Core runtime and enter the following:

```
dotnet new -all
```

You should see the following:

```
dotnet new -all
Usage: new [options]

Options:
  -h, --help          Displays help for this command.
  -l, --list          Lists templates containing the specified name. If no name is specified, lists all templates.
  -n, --name          The name for the output being created. If no name is specified, the name of the current directory is used.
  -o, --output        Location to place the generated output.
  -i, --install       Installs a source or a template pack.
  -u, --uninstall     Uninstalls a source or a template pack.
  --nuget-source      Specifies a NuGet source to use during install.
  --type              Filters templates based on available types. Predefined values are "project", "item" or "other".
  --force             Forces content to be generated even if it would change existing files.
  -lang, --language   Filters templates based on language and specifies the language of the template to create.


Templates                                         Short Name         Language          Tags                             
----------------------------------------------------------------------------------------------------------------------------
Console Application                               console            [C#], F#, VB      Common/Console                   
Class library                                     classlib           [C#], F#, VB      Common/Library                   
Unit Test Project                                 mstest             [C#], F#, VB      Test/MSTest                      
xUnit Test Project                                xunit              [C#], F#, VB      Test/xUnit                       
Razor Page                                        page               [C#]              Web/ASP.NET                      
MVC ViewImports                                   viewimports        [C#]              Web/ASP.NET                      
MVC ViewStart                                     viewstart          [C#]              Web/ASP.NET                      
ASP.NET Core Empty                                web                [C#], F#          Web/Empty                        
ASP.NET Core Web App (Model-View-Controller)      mvc                [C#], F#          Web/MVC                          
ASP.NET Core Web App                              razor              [C#]              Web/MVC/Razor Pages              
ASP.NET Core with Angular                         angular            [C#]              Web/MVC/SPA                      
ASP.NET Core with React.js                        react              [C#]              Web/MVC/SPA                      
ASP.NET Core with React.js and Redux              reactredux         [C#]              Web/MVC/SPA                      
Razor Class Library                               razorclasslib      [C#]              Web/Razor/Library/Razor Class Library
ASP.NET Core Web API                              webapi             [C#], F#          Web/WebAPI                       
global.json file                                  globaljson                           Config                           
NuGet Config                                      nugetconfig                          Config                           
Web Config                                        webconfig                            Config                           
Solution File                                     sln                                  Solution                         

Examples:
    dotnet new mvc --auth Individual
    dotnet new viewstart
    dotnet new --help
```

So, for example, if you wanted to create a console project, you would do the following: 

1. Make a directory where your project will be created using the following command: `mkdir` *example*

1. Navigate to that directory using the following command: `cd `*example*

1. Enter the following command: `dotnet new console -o myproject`

   This will create the following files in your *example* directory:
   + Program\.cs, which is where you write your Lambda function code\. 
   + MyProject\.csproj, an XML file that lists the files and dependencies that comprise your\.NET application\.

AWS Lambda offers additional templates via the [Amazon\.Lambda\.Templates](https://www.nuget.org/packages/Amazon.Lambda.Templates) nuget package\. To install this package, run the following command:

```
dotnet new -i Amazon.Lambda.Templates
```

Once the install is complete, the Lambda templates show up as part of `dotnet new`\. To verify this, again run the following command:

```
dotnet new -all
```

You should now see the following:

```
dotnet new -all
Usage: new [options]

Options:
  -h, --help          Displays help for this command.
  -l, --list          Lists templates containing the specified name. If no name is specified, lists all templates.
  -n, --name          The name for the output being created. If no name is specified, the name of the current directory is used.
  -o, --output        Location to place the generated output.
  -i, --install       Installs a source or a template pack.
  -u, --uninstall     Uninstalls a source or a template pack.
  --nuget-source      Specifies a NuGet source to use during install.
  --type              Filters templates based on available types. Predefined values are "project", "item" or "other".
  --force             Forces content to be generated even if it would change existing files.
  -lang, --language   Filters templates based on language and specifies the language of the template to create.


Templates                                                 Short Name                              Language          Tags
---------------------------------------------------------------------------------------------------------------------------------------------------------
Order Flowers Chatbot Tutorial                            lambda.OrderFlowersChatbot              [C#]              AWS/Lambda/Function
Lambda Detect Image Labels                                lambda.DetectImageLabels                [C#], F#          AWS/Lambda/Function
Lambda Empty Function                                     lambda.EmptyFunction                    [C#], F#          AWS/Lambda/Function
Lex Book Trip Sample                                      lambda.LexBookTripSample                [C#]              AWS/Lambda/Function
Lambda Simple DynamoDB Function                           lambda.DynamoDB                         [C#], F#          AWS/Lambda/Function
Lambda Simple Kinesis Firehose Function                   lambda.KinesisFirehose                  [C#]              AWS/Lambda/Function
Lambda Simple Kinesis Function                            lambda.Kinesis                          [C#], F#          AWS/Lambda/Function
Lambda Simple S3 Function                                 lambda.S3                               [C#], F#          AWS/Lambda/Function
Lambda ASP.NET Core Web API                               serverless.AspNetCoreWebAPI             [C#], F#          AWS/Lambda/Serverless
Lambda ASP.NET Core Web Application with Razor Pages      serverless.AspNetCoreWebApp             [C#]              AWS/Lambda/Serverless
Serverless Detect Image Labels                            serverless.DetectImageLabels            [C#], F#          AWS/Lambda/Serverless
Lambda DynamoDB Blog API                                  serverless.DynamoDBBlogAPI              [C#]              AWS/Lambda/Serverless
Lambda Empty Serverless                                   serverless.EmptyServerless              [C#], F#          AWS/Lambda/Serverless
Lambda Giraffe Web App                                    serverless.Giraffe                      F#                AWS/Lambda/Serverless
Serverless Simple S3 Function                             serverless.S3                           [C#], F#          AWS/Lambda/Serverless
Step Functions Hello World                                serverless.StepFunctionsHelloWorld      [C#], F#          AWS/Lambda/Serverless
Console Application                                       console                                 [C#], F#, VB      Common/Console
Class library                                             classlib                                [C#], F#, VB      Common/Library
Unit Test Project                                         mstest                                  [C#], F#, VB      Test/MSTest
xUnit Test Project                                        xunit                                   [C#], F#, VB      Test/xUnit
Razor Page                                                page                                    [C#]              Web/ASP.NET
MVC ViewImports                                           viewimports                             [C#]              Web/ASP.NET
MVC ViewStart                                             viewstart                               [C#]              Web/ASP.NET
ASP.NET Core Empty                                        web                                     [C#], F#          Web/Empty
ASP.NET Core Web App (Model-View-Controller)              mvc                                     [C#], F#          Web/MVC
ASP.NET Core Web App                                      razor                                   [C#]              Web/MVC/Razor Pages
ASP.NET Core with Angular                                 angular                                 [C#]              Web/MVC/SPA
ASP.NET Core with React.js                                react                                   [C#]              Web/MVC/SPA
ASP.NET Core with React.js and Redux                      reactredux                              [C#]              Web/MVC/SPA
Razor Class Library                                       razorclasslib                           [C#]              Web/Razor/Library/Razor Class Library
ASP.NET Core Web API                                      webapi                                  [C#], F#          Web/WebAPI
global.json file                                          globaljson                                                Config
NuGet Config                                              nugetconfig                                               Config
Web Config                                                webconfig                                                 Config
Solution File                                             sln                                                       Solution

Examples:
    dotnet new mvc --auth Individual
    dotnet new viewimports --namespace
    dotnet new --help
```

To examine details about a particular template, use the following command:

```
dotnet new lambda.EmptyFunction --help
```

Note the following:

```
 -p|--profile  The AWS credentials profile set in aws-lambda-tools-defaults.json and used as the default profile when interacting with AWS.
string - Optional
 
 -r|--region   The AWS region set in aws-lambda-tools-defaults.json and used as the default region when interacting with AWS.
string - Optional
```

These are optional values you can set when you create your Lambda function and will then be automatically written to the `aws-lambda-tools-defaults.json` file, which is built as part of the function\-creation process\. The following explains what they mean:
+ **\-\-profile** – Your [execution role](lambda-intro-execution-role.md)\.
+ **\-\-region** – The Region in which your function will reside\.

For example, to create a Lambda function, run the following command, substituting the values of the `--region` parameter with the region of your choice and `--profile` with your IAM profile: 

**Note**  
For more information on Lambda function requirements, see [CreateFunction](API_CreateFunction.md)

```
dotnet new lambda.EmptyFunction --name MyFunction --profile default --region region
```

This should create a directory structure similar to the following:

```
<dir>myfunction
     /src/myfunction
     /test/myfunction
```

Under the `src/myfunction` directory, examine the following files:
+ **aws\-lambda\-tools\-defaults\.json**: This is where you specify the command line options when deploying your Lambda function\. For example:

  ```
  "profile":"iam profile"",
    "region" : "region",
    "configuration" : "Release",
    "framework" : "netcoreapp2.1",
    "function-runtime":"dotnetcore2.1",
    "function-memory-size" : 256,
    "function-timeout" : 30,
    "function-handler" : "MyFunction::MyFunction.Function::FunctionHandler"
  ```
+ **Function\.cs**: Your Lambda handler function code\. It's a C\# template that includes the default `Amazon.Lambda.Core` library and a default `LambdaSerializer` attribute\. For more information on serialization requirements and options, see [Serializing Lambda Functions](#lambda-dotnet-add-serializer)\. It also includes a sample function that you can edit to apply your Lambda function code\. 

  ```
  using System;
  using System.Collections.Generic;
  using System.Linq;
  using System.Threading.Tasks;
  
  using Amazon.Lambda.Core;
  
  // Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
  [assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]
  
  namespace MyFunction
  {
      public class Function
      {      
        
          public string FunctionHandler1(string input, ILambdaContext context)
          {
              return input?.ToUpper();
          }
      }
  }
  ```
+ **MyFunction\.csproj**: An [MSBuild](https://msdn.microsoft.com/en-us/library/dd393574.aspx) file that lists the files and assemblies that comprise your application\.

  ```
  <Project Sdk="Microsoft.NET.Sdk">
  
    <PropertyGroup>
      <TargetFramework>netcoreapp2.1</TargetFramework>
    </PropertyGroup>
  
    <ItemGroup>
      <PackageReference Include="Amazon.Lambda.Core" Version="1.0.0 " />
      <PackageReference Include="Amazon.Lambda.Serialization.Json" Version="1.3.0" />
    </ItemGroup>
  
  </Project>
  ```
+ **Readme**: Use this file to document your Lambda function\.

Under the `myfunction/test directory, examine the following files:`
+ **myFunction\.Tests\.csproj**: As noted above, this is an [MSBuild](https://msdn.microsoft.com/en-us/library/dd393574.aspx) file that lists the files and assemblies that comprise your test project\. Note also that it includes the `Amazon.Lambda.Core` library, allowing you to seamlesssly integrate any Lambda templates required to test your function\.

  ```
  <Project Sdk="Microsoft.NET.Sdk">
     ... 
  
      <PackageReference Include="Amazon.Lambda.Core" Version="1.0.0 " />
     ...
  ```
+ **FunctionTest\.cs**: The same C\# code template file that it is included in the `src` directory\. Edit this file to mirror your function's production code and test it before uploading your Lambda function to a production environment\.

  ```
  using System;
  using System.Collections.Generic;
  using System.Linq;
  using System.Threading.Tasks;
  
  using Xunit;
  using Amazon.Lambda.Core;
  using Amazon.Lambda.TestUtilities;
  
  using MyFunction;
  
  namespace MyFunction.Tests
  {
      public class FunctionTest
      {
          [Fact]
          public void TestToUpperFunction()
          {
  
              // Invoke the lambda function and confirm the string was upper cased.
              var function = new Function();
              var context = new TestLambdaContext();
              var upperCase = function.FunctionHandler("hello world", context);
  
              Assert.Equal("HELLO WORLD", upperCase);
          }
      }
  }
  ```

Once your function has passed its tests, you can build and deploy using the Amazon\.Lambda\.Tools \.NET Core Global Tool\. To install the \.NET Core Global Tool run the following command\.

```
dotnet tool install -g Amazon.Lambda.Tools
```

If you already have the tool installed you can make sure you are using the latest version with the following command\.

```
dotnet tool update -g Amazon.Lambda.Tools
```

For more information about the Amazon\.Lambda\.Tools \.NET Core Global see its [GitHub repository](https://github.com/aws/aws-extensions-for-dotnet-cli)\.

With the Amazon\.Lambda\.Tools installed you can deploy your function with the following command:

```
dotnet lambda deploy-function MyFunction –-function-role role
```

After deployment, you can re\-test it in a production environment with the following command and pass in a different value to your Lambda function handler:

```
dotnet lambda invoke-function MyFunction --payload "Just Checking If Everything is OK"
```

Presuming everything was successful, you should see the following:

```
dotnet lambda invoke-function MyFunction --payload "Just Checking If Everything is OK"
Payload:
"JUST CHECKING IF EVERYTHING IS OK"

Log Tail:
START RequestId: id Version: $LATEST
END RequestId: id
REPORT RequestId: id  Duration: 0.99 ms       Billed Duration: 100 ms         Memory Size: 256 MB     Max Memory Used: 12 MB
```

## Serializing Lambda Functions<a name="lambda-dotnet-add-serializer"></a>

For any Lambda functions that use input or output types other than a `Stream` object, you will need to add a serialization library to your application\. You can do this in the following ways:
+ Use Json\.NET\. Lambda will provide an implementation for JSON serializer using JSON\.NET as a [NuGet](https://www.nuget.org) package\.
+ Create your own serialization library by implementing the `ILambdaSerializer` interface, which is available as part of the `Amazon.Lambda.Core` library\. The interface defines two methods:
  + `T Deserialize<T>(Stream requestStream);`

     You implement this method to deserialize the request payload from the `Invoke` API into the object that is passed to the Lambda function handler\.
  + `T Serialize<T>(T response, Stream responseStream);`\.

     You implement this method to serialize the result returned from the Lambda function handler into the response payload that is returned by the `Invoke` API\.

You use whichever serializer you wish by adding it as a dependency to your `MyProject.csproj` file\. 

```
...
 <ItemGroup>
    <PackageReference Include="Amazon.Lambda.Core" Version="1.0.0" />
    <PackageReference Include="Amazon.Lambda.Serialization.Json" Version="1.3.0" />
  </ItemGroup>
```

You then add it to your AssemblyInfo\.cs file\. For example, if you are using the default Json\.NET serializer, this is what you would add:

```
[assembly:LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]
```

**Note**  
You can define a custom serialization attribute at the method level, which will override the default serializer specified at the assembly level\. For more information, see [Handling Standard Data Types](dotnet-programming-model-handler-types.md#dotnet-programming-model-handling-standard-types)\.