# \.NET Core CLI<a name="lambda-dotnet-coreclr-deployment-package"></a>

The \.NET Core CLI offers a cross\-platform way for you to create \.NET\-based Lambda applications\. 

## Before You Begin<a name="lambda-dotnet-getready"></a>

This section assumes you have done the following:

+ Installed the \.NET Core CLI\. If you haven't, do so [here](https://www.microsoft.com/net/core#windowsvs2015)\.

## Create a \.NET Project<a name="lambda-dotnet-create-project"></a>

To create an application using the \.NET Core CLI, open a command prompt and navigate to the folder where you installed the \.NET Core runtime and follow these steps: 

1. Make a directory where your project will be created using the following command: `mkdir` *example*

1. Navigate to that directory using the following command: `cd `*example*

1. Enter the following command: `dotnet new console `

   This will create two files in your *example* directory:

   + Program\.cs, which is where you write your Lambda function code\. 

   + project\.json, which is the file is where you declare Nuget dependencies \(or dependencies on local projects\)\. NuGet is the package manager for the \.NET platform\. For more information, see [Nuget\.org](https://www.nuget.org)\.
**Note**  
 Lambda methods don't use the `Main()` entry point provided by default in \.NET, so open the project\.json file and remove the "buildOptions" property\. After this, your project\.json should look something like this \(exact versions may differ depending on when you installed the NetCore CLI\): 

   ```
   {
     "version": "1.0.0-*",
     "dependencies": {},
     "frameworks": {
       "netcoreapp1.0": {
         "dependencies": {
           "Microsoft.NETCore.App": {
             "type": "platform",
             "version": "1.1.0"
           }
         },
         "imports": "dnxcore50"
       }
     }
   }
   ```

1. Open the `Program.cs` file using an editor of you choice, such as Microsoft Visual Studio\.

   + Replace the default code that is provided with your Lambda function handler code:

     At this point, your \.cs file structure should resemble this: 

     ```
     using System;
     using System.IO;
     
     namespace CSharpLambdaFunction
     {
         public class LambdaHandler
         {
            public Stream myHandler(Stream inputStream)
            {
                //function logic
            }
         }
     }
     ```

Your Lambda function handler signature should be of the format *Assembly::Namespace\.ClassName::MethodName*\. For more information, see [Handler Signatures](dotnet-programming-model-handler-types.md#dotnet-programming-model-handler-signatures)\.

### Using a Serializer<a name="lambda-dotnet-add-serializer"></a>

For any Lambda functions that use input or output types other than a `Stream` object, you will need to add a serialization library to your application\. You can do this in the following ways:

+ Use Json\.NET\. Lambda will provide an implementation for JSON serializer using JSON\.NET as a [NuGet](https://www.nuget.org) package\.

+ Create your own serialization library by implementing the `ILambdaSerializer` interface, which is available as part of the `Amazon.Lambda.Core` library\. The interface defines two methods:

  + `T Deserialize<T>(Stream requestStream);`

     You implement this method to deserialize the request payload from the `Invoke` API into the object that is passed to the Lambda function handler\.

  + `T Serialize<T>(T response, Stream responseStream);`\.

     You implement this method to serialize the result returned from the Lambda function handler into the response payload that is returned by the `Invoke` API\.

You use whichever serializer you wish by adding it as a dependency to your `project.json` file\. 

```
{
  "version": "1.0.0-*",
  "buildOptions": {
  },
 
  "dependencies": {
    "Microsoft.NETCore.App": {
      "type": "platform",
      "version": "1.0.1"
    },
 
    "Newtonsoft.Json": "9.0.1",
 
    "Amazon.Lambda.Core": "1.0.0*",
    "Amazon.Lambda.Serialization.Json": "1.0.0",
 
    "Amazon.Lambda.Tools" : {
      "type" :"build",
      "version":"0.9.0-preview1"
    }
  },
 
  "tools": {
    "Amazon.Lambda.Tools" : "0.9.0-preview1"
  },
 
  "frameworks": {
    "netcoreapp1.0": {
      "imports": "dnxcore50"
    }
  }
}
```

You then add it to your AssemblyInfo\.cs file\. For example, if you are using the default Json\.NET serializer, this is what you would add:

```
[assembly:LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]
```

**Note**  
You can define a custom serialization attribute at the method level, which will override the default serializer specified at the assembly level\. For more information, see [Handling Standard Data Types](dotnet-programming-model-handler-types.md#dotnet-programming-model-handling-standard-types)\.

### Create the Deployment Package<a name="lambda-dotnet-create-deployment-package"></a>

To create the deployment package, open a command prompt and navigate to the folder that contains your `project.json` file and run the following commands:

+ `dotnet restore` which will restore any references to dependencies of the project that may have changed during the development process\.

+ `dotnet publish` which compiles the application and packages the source code and any dependencies into a folder\. The output of the command window will instruct you where the folder was created\. For example:

  ```
  publish: Published to C:\Users\yourname\project-folder\bin\debug\netcoreapp1.1\publish
  ```

  The contents of this folder represent your application and at a minimum would look something like this:

  *application\-name*\.deps\.json

  *application\-name*\.dll

  *application\-name*\.pdb

  *application\-name*\.runtimeconfig\.json

Zip the contents of the folder \(not the folder itself\)\. This is your deployment package\.