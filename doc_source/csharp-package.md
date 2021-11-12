# Deploy C\# Lambda functions with \.zip file archives<a name="csharp-package"></a>

A \.NET Core deployment package \(\.zip file archive\) contains your function's compiled assembly along with all of its assembly dependencies\. The package also contains a `proj.deps.json` file\. This signals to the \.NET Core runtime all of your function's dependencies and a `proj.runtimeconfig.json` file, which is used to configure the runtime\. The \.NET command line interface \(CLI\) `publish` command can create a folder with all of these files\. By default, the `proj.runtimeconfig.json` is not included because a Lambda project is typically configured to be a class library\. To force the `proj.runtimeconfig.json` to be written as part of the `publish` process, pass in the command line argument `/p:GenerateRuntimeConfigurationFiles=true` to the `publish` command\.

Although it is possible to create the deployment package with the `dotnet publish` command, we recommend that you create the deployment package with either the [\.NET Core CLI](csharp-package-cli.md) or the [AWS Toolkit for Visual Studio](csharp-package-toolkit.md)\. These are tools optimized specifically for Lambda to ensure that the `lambda-project.runtimeconfig.json` file exists and optimizes the package bundle, including the removal of any non\-Linux\-based dependencies\.

**Topics**
+ [\.NET Core CLI](csharp-package-cli.md)
+ [AWS Toolkit for Visual Studio](csharp-package-toolkit.md)