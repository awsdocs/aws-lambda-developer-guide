# Using Lambda extensions<a name="using-extensions"></a>

You can use Lambda extensions to augment your Lambda functions\. For example, use Lambda extensions to integrate functions with your preferred monitoring, observability, security, and governance tools\. You can choose from a broad set of tools that [AWS Lambda Partners](http://aws.amazon.com/lambda/partners/) provides, or you can [create your own Lambda extensions](runtimes-extensions-api.md)\.

Lambda supports external and internal extensions\. An external extension runs as an independent process in the execution environment and continues to run after the function invocation is fully processed\. Because extensions run as separate processes, you can write them in a different language than the function\.

An internal extension runs as part of the runtime process\. Your function accesses internal extensions by using wrapper scripts or in\-process mechanisms such as `JAVA_TOOL_OPTIONS`\. For more information, see [Modifying the runtime environment](runtimes-modify.md)\.

You can add extensions to a function using the Lambda console, the AWS Command Line Interface \(AWS CLI\), or infrastructure as code \(IaC\) services and tools such as AWS CloudFormation, AWS Serverless Application Model \(AWS SAM\), and Terraform\.

The following [Lambda runtimes](lambda-runtimes.md) support extensions:
+ \.NET Core 3\.1 \(C\#/PowerShell\) \(`dotnetcore3.1`\)
+ Custom runtime \(`provided`\)
+ Custom runtime on Amazon Linux 2 \(`provided.al2`\)
+ Java 11 \(Corretto\) \(`java11`\)
+ Java 8 \(Corretto\) \(`java8.al2`\)
+ Node\.js 14\.x \(`nodejs14.x`\)
+ Node\.js 12\.x \(`nodejs12.x`\)
+ Node\.js 10\.x \(`nodejs10.x`\)
+ Python 3\.9 \(`python3.9`\)
+ Python 3\.8 \(`python3.8`\)
+ Python 3\.7 \(`python3.7`\)
+ Ruby 2\.7 \(`ruby2.7`\)
+ Ruby 2\.5 \(`ruby2.5`\)

Note that the Go 1\.x runtime does not support extensions\. To support extensions, you can create Go functions on the `provided.al2` runtime\. For more information, see [ Migrating Lambda functions to Amazon Linux 2](http://aws.amazon.com/blogs/compute/migrating-aws-lambda-functions-to-al2/)\.

You are charged for the execution time that the extension consumes \(in 1 ms increments\)\. For more pricing information for extensions, see [AWS Lambda Pricing](http://aws.amazon.com/lambda/pricing/)\. For pricing information for partner extensions, see those partners' websites\. There is no cost to install your own extensions\.

**Topics**
+ [Execution environment](#using-extensions-env)
+ [Impact on performance and resources](#using-extensions-reg)
+ [Permissions](#using-extensions-permissions)
+ [Configuring extensions \(\.zip file archive\)](#using-extensions-config)
+ [Using extensions in container images](#invocation-extensions-images)
+ [Next steps](#using-extensions-next)

## Execution environment<a name="using-extensions-env"></a>

Lambda invokes your function in an [execution environment](runtimes-context.md), which provides a secure and isolated runtime environment\. The execution environment manages the resources required to run your function and provides lifecycle support for the function's runtime and extensions\.

The lifecycle of the execution environment includes the following phases:
+ `Init`: In this phase, Lambda creates or unfreezes an execution environment with the configured resources, downloads the code for the function and all layers, initializes any extensions, initializes the runtime, and then runs the function’s initialization code \(the code outside the main handler\)\. The `Init` phase happens either during the first invocation, or in advance of function invocations if you have enabled [provisioned concurrency](provisioned-concurrency.md)\.

  The `Init` phase is split into three sub\-phases: `Extension init`, `Runtime init`, and `Function init`\. These sub\-phases ensure that all extensions and the runtime complete their setup tasks before the function code runs\.
+ `Invoke`: In this phase, Lambda invokes the function handler\. After the function runs to completion, Lambda prepares to handle another function invocation\.
+ `Shutdown`: This phase is triggered if the Lambda function does not receive any invocations for a period of time\. In the `Shutdown` phase, Lambda shuts down the runtime, alerts the extensions to let them stop cleanly, and then removes the environment\. Lambda sends a `Shutdown` event to each extension, which tells the extension that the environment is about to be shut down\.

During the `Init` phase, Lambda extracts layers containing extensions into the `/opt` directory in the execution environment\. Lambda looks for extensions in the `/opt/extensions/` directory, interprets each file as an executable bootstrap for launching the extension, and starts all extensions in parallel\.

## Impact on performance and resources<a name="using-extensions-reg"></a>

The size of your function's extensions counts towards the deployment package size limit\. For a \.zip file archive, the total unzipped size of the function and all extensions cannot exceed the unzipped deployment package size limit of 250 MB\.

Extensions can impact the performance of your function because they share function resources such as CPU, memory, and storage\. For example, if an extension performs compute\-intensive operations, you may see your function's execution duration increase\.

Each extension must complete its initialization before Lambda invokes the function\. Therefore, an extension that consumes significant initialization time can increase the latency of the function invocation\.

To measure the extra time that the extension takes after the function execution, you can use the `PostRuntimeExtensionsDuration` [function metric](monitoring-metrics.md)\. To measure the increase in memory used, you can use the `MaxMemoryUsed` metric\. To understand the impact of a specific extension, you can run different versions of your functions side by side\.

## Permissions<a name="using-extensions-permissions"></a>

Extensions have access to the same resources as functions\. Because extensions are executed within the same environment as the function, permissions are shared between the function and the extension\.

For a \.zip file archive, you can create an AWS CloudFormation template to simplify the task of attaching the same extension configuration—including AWS Identity and Access Management \(IAM\) permissions—to multiple functions\.

## Configuring extensions \(\.zip file archive\)<a name="using-extensions-config"></a>

You can add an extension to your function as a [Lambda layer](configuration-layers.md)\. Using layers enables you to share extensions across your organization or to the entire community of Lambda developers\. You can add one or more extensions to a layer\. You can register up to 10 extensions for a function\.

You add the extension to your function using the same method as you would for any layer\. For more information, see [Using layers with your Lambda function](invocation-layers.md)\.

**Add an extension to your function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose the **Code** tab if it is not already selected\.

1. Under **Layers**, choose **Edit**\.

1. For **Choose a layer**, choose **Specify an ARN**\.

1. For **Specify an ARN**, enter the Amazon Resource Name \(ARN\) of an extension layer\.

1. Choose **Add**\.

## Using extensions in container images<a name="invocation-extensions-images"></a>

You can add extensions to your [container image](lambda-images.md)\. The ENTRYPOINT container image setting specifies the main process for the function\. Configure the ENTRYPOINT setting in the Dockerfile, or as an override in the function configuration\. 

You can run multiple processes within a container\. Lambda manages the lifecycle of the main process and any additional processes\. Lambda uses the [Extensions API](runtimes-extensions-api.md) to manage the extension lifecycle\. 

### Example: Adding an external extension<a name="extensions-images-ex1"></a>

An external extension runs in a separate process from the Lambda function\. Lambda starts a process for each extension in the `/opt/extensions/` directory\. Lambda uses the Extensions API to manage the extension lifecycle\. After the function has run to completion, Lambda sends a `Shutdown` event to each external extension\.

**Example of adding an external extension to a Python base image**  

```
FROM public.ecr.aws/lambda/python:3.8

# Copy and install the app
COPY /app /app
WORKDIR /app
RUN pip install -r requirements.txt

# Add an extension from the local directory into /opt
ADD my-extension.zip /opt
CMD python ./my-function.py
```

## Next steps<a name="using-extensions-next"></a>

To learn more about extensions, we recommend the following resources:
+ For a basic working example, see [Building Extensions for AWS Lambda](http://aws.amazon.com/blogs/compute/building-extensions-for-aws-lambda-in-preview/) on the AWS Compute Blog\.
+ For information about extensions that AWS Lambda Partners provides, see [Introducing AWS Lambda Extensions](http://aws.amazon.com/blogs/compute/introducing-aws-lambda-extensions-in-preview/) on the AWS Compute Blog\.
+ To view available example extensions and wrapper scripts, see [AWS Lambda Extensions](https://github.com/aws-samples/aws-lambda-extensions) on the AWS Samples GitHub repository\.