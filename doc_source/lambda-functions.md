# Managing AWS Lambda functions<a name="lambda-functions"></a>

You can use the AWS Lambda API or console to configure settings on your Lambda functions\. [Basic function settings](configuration-console.md) include the description, role, and runtime that you specify when you create a function in the Lambda console\. You can configure more settings after you create a function, or use the API to set things like the handler name, memory allocation, and security groups during creation\.

To keep secrets out of your function code, store them in the function's configuration and read them from the execution environment during initialization\. [Environment variables](configuration-envvars.md) are always encrypted at rest, and can be encrypted client\-side as well\. Use environment variables to make your function code portable by removing connection strings, passwords, and endpoints for external resources\.

[Versions and aliases](configuration-versions.md) are secondary resources that you can create to manage function deployment and invocation\. Publish [versions](configuration-versions.md) of your function to store its code and configuration as a separate resource that cannot be changed, and create an [alias](configuration-aliases.md) that points to a specific version\. Then you can configure your clients to invoke a function alias, and update the alias when you want to point the client to a new version, instead of updating the client\.

As you add libraries and other dependencies to your function, creating and uploading a deployment package can slow down development\. Use [layers](configuration-layers.md) to manage your function's dependencies independently and keep your deployment package small\. You can also use layers to share your own libraries with other customers and use publicly available layers with your functions\.

To use your Lambda function with AWS resources in an Amazon VPC, configure it with security groups and subnets to [create a VPC connection](configuration-vpc.md)\. Connecting your function to a VPC lets you access resources in a private subnet such as relational databases and caches\. You can also [create a database proxy](configuration-database.md) for MySQL and Aurora DB instances\. A database proxy enables a function to reach high concurrency levels without exhausting database connections\.

**Topics**
+ [Configuring functions in the AWS Lambda console](configuration-console.md)
+ [Using AWS Lambda environment variables](configuration-envvars.md)
+ [Managing concurrency for a Lambda function](configuration-concurrency.md)
+ [Lambda function versions](configuration-versions.md)
+ [Lambda function aliases](configuration-aliases.md)
+ [AWS Lambda layers](configuration-layers.md)
+ [Configuring a Lambda function to access resources in a VPC](configuration-vpc.md)
+ [Configuring database access for a Lambda function](configuration-database.md)
+ [Configuring file system access for Lambda functions](configuration-filesystem.md)
+ [Tagging Lambda Functions](configuration-tags.md)