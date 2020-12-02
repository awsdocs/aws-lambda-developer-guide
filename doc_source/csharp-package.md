# Deploy C\# Lambda functions with \.zip file archives<a name="csharp-package"></a>

To create a container image to deploy your function code, see [Using container images with Lambda](lambda-images.md)\.

A \.zip file archive is a deployment package that contains your function code and dependencies\. You must create a \.zip file archive if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon Simple Storage Service \(Amazon S3\) bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

A \.NET Core deployment package \(\.zip file archive\) contains your function's compiled assembly along with all of its assembly dependencies\. The package also contains a `proj.deps.json` file\. This signals to the \.NET Core runtime all of your function's dependencies and a `proj.runtimeconfig.json` file, which is used to configure the runtime\. The \.NET command line interface \(CLI\) `publish` command can create a folder with all of these files\. However, by default the `proj.runtimeconfig.json` is not included because a Lambda project is typically configured to be a class library\. To force the `proj.runtimeconfig.json` to be written as part of the `publish` process, pass in the command line argument `/p:GenerateRuntimeConfigurationFiles=true` to the `publish` command\.

Although it is possible to create the deployment package with the `dotnet publish` command, we recommend that you create the deployment package with either the [\.NET Core CLI](csharp-package-cli.md) or the [AWS Toolkit for Visual Studio](csharp-package-toolkit.md)\. These are tools optimized specifically for Lambda to ensure that the `lambda-project.runtimeconfig.json` file exists and optimizes the package bundle, including the removal of any non\-Linux\-based dependencies\.

**Topics**
+ [\.NET Core CLI](csharp-package-cli.md)
+ [AWS Toolkit for Visual Studio](csharp-package-toolkit.md)