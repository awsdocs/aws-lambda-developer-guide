# Lambda instruction set architectures<a name="foundation-arch"></a>

 The *instruction set architecture* of a Lambda function determines the type of computer processor that Lambda uses to run the function\. Lambda provides a choice of instruction set architectures:
+ arm64 – 64\-bit ARM architecture, for the AWS Graviton2 processor\. The arm64 architecture is available in most AWS Regions\.
+ x86\_64 – 64\-bit x86 architecture, for x86\-based processors\.

**Topics**
+ [Advantages of using arm64 architecture](#foundation-arch-adv)
+ [Function migration to arm64 architecture](#foundation-arch-consider)
+ [Function code compatibility with arm64 architecture](#foundation-arch-considerations)
+ [Suggested migration steps](#foundation-arch-steps)
+ [Configuring the instruction set architecture](#foundation-arch-config)

## Advantages of using arm64 architecture<a name="foundation-arch-adv"></a>

Lambda functions that use arm64 architecture \(AWS Graviton2 processor\) can achieve significantly better price and performance than the equivalent function running on x86\_64 architecture\. Consider using arm64 for compute\-intensive applications such as high\-performance computing, video encoding, and simulation workloads\.

The Graviton2 CPU uses the Neoverse N1 core and supports Armv8\.2 \(including CRC and crypto extensions\) plus several other architectural extensions\.

Graviton2 reduces memory read time by providing a larger L2 cache per vCPU, which improves the latency performance of web and mobile backends, microservices, and data processing systems\. Graviton2 also provides improved encryption performance and supports instruction sets that improve the latency of CPU\-based machine learning inference\.

For more information about AWS Graviton2, see [AWS Graviton Processor](http://aws.amazon.com/ec2/graviton)\.

## Function migration to arm64 architecture<a name="foundation-arch-consider"></a>

When you select a Lambda function to migrate to arm64 architecture, to ensure a smooth migration, make sure that your function meets the following requirements:
+ The function currently uses a Lambda Amazon Linux 2 runtime\.
+ The deployment package contains only open\-source components and source code that you control, so that you can make any necessary updates for the migration\.
+ If the function code includes third\-party dependencies, each library or package provides an arm64 version\.

## Function code compatibility with arm64 architecture<a name="foundation-arch-considerations"></a>

Your Lambda function code must be compatible with the instruction set architecture of the function\. Before you migrate a function to arm64 architecture, note the following points about the current function code:
+ If you added your function code using the embedded code editor, your code probably runs on either architecture without modification\.
+ If you uploaded your function code, you must upload new code that is compatible with your target architecture\.
+ If your function uses layers, you must [check each layer](invocation-layers.md#configuration-layers-finding) to ensure that it is compatible with the new architecture\. If a layer is not compatible, edit the function to replace the current layer version with a compatible layer version\.
+ If your function uses Lambda extensions, you must check each extension to ensure that it is compatible with the new architecture\.
+ If your function uses a container image deployment package type, you must create a new container image that is compatible with the architecture of the function\.

## Suggested migration steps<a name="foundation-arch-steps"></a>



To migrate a Lambda function to the arm64 architecture, we recommend following these steps:

1. Build the list of dependencies for your application or workload\. Common dependencies include:
   + All the libraries and packages that the function uses\.
   + The tools that you use to build, deploy, and test the function, such as compilers, test suites, continuous integration and continuous delivery \(CI/CD\) pipelines, provisioning tools, and scripts\.
   + The Lambda extensions and third\-party tools that you use to monitor the function in production\.

1. For each of the dependencies, check the version, and then check whether arm64 versions are available\.

1. Build an environment to migrate your application\.

1. Bootstrap the application\.

1. Test and debug the application\.

1. Test the performance of the arm64 function\. Compare the performance with the x86\_64 version\.

1. Update your infrastructure pipeline to support arm64 Lambda functions\.

1. Stage your deployment to production\.

   For example, use [alias routing configuration](configuration-aliases.md#configuring-alias-routing) to split traffic between the x86 and arm64 versions of the function, and compare the performance and latency\.

For more information about how to create a code environment for arm64 architecture, including language\-specific information for Java, Go, \.NET, and Python, see the [Getting started with AWS Graviton](https://github.com/aws/aws-graviton-getting-started) GitHub repository\.

## Configuring the instruction set architecture<a name="foundation-arch-config"></a>

You can configure the instruction set architecture for new Lambda functions using the Lambda console, AWS SDKs, AWS Command Line Interface \(AWS CLI\), or AWS CloudFormation\. You can deploy the function code to Lambda with either a \.zip archive file or a container image deployment package\.

Lambda provides the following runtimes for the arm64 architecture\. These runtimes all use the Amazon Linux 2 operating system\.
+ Node\.js 12, Node\.js 14, Node\.js 16
+ Python 3\.8, Python 3\.9 
+ Java 8 \(AL2\), Java 11
+ \.NET Core 3\.1
+ Ruby 2\.7
+ Custom Runtime on Amazon Linux 2

**Note**  
Runtimes that use the Amazon Linux operating system, such as Go 1\.x, do not support the arm64 architecture\. To use arm64 architecture, you can run Go with the provided\.al2 runtime\. For example, see [Build a Go function for the provided\.al2 runtime](golang-package.md#golang-package-al2) or [Create a Go image from the provided\.al2 base image](go-image.md#go-image-al2)\. 

For an example of how to create a function with arm64 architecture, see [AWS Lambda Functions Powered by AWS Graviton2 Processor](http://aws.amazon.com/blogs/aws/aws-lambda-functions-powered-by-aws-graviton2-processor-run-your-functions-on-arm-and-get-up-to-34-better-price-performance/)\.