# Managing AWS Lambda functions<a name="lambda-managing"></a>

[Configuring AWS Lambda functions](lambda-functions.md) describes the how to configure the core capabilities and options for a function\. Lambda also provides advanced features such as concurrency control, network access, database interworking, file systems, and code signing\.

[Concurrency](configuration-concurrency.md) is the number of instances of your function that are active\. Lambda provides two types of concurrency controls: reserved concurrency and provisioned concurrency\.

To use your Lambda function with AWS resources in an Amazon VPC, configure it with security groups and subnets to [create a VPC connection](configuration-vpc.md)\. Connecting your function to a VPC lets you access resources in a private subnet such as relational databases and caches\. You can also [create a database proxy](configuration-database.md) for MySQL and Aurora DB instances\. A database proxy enables a function to reach high concurrency levels without exhausting database connections\.

To use [code signing](configuration-codesigning.md) with your Lambda function, configure it with a code\-signing configuration\. When a user attempts to deploy a code package, Lambda checks that the code package has a valid signature from a trusted publisher\. The code\-signing configuration includes a set of signing profiles, which define the trusted publishers for this function\.