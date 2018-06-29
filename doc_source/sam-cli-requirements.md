# Install SAM CLI<a name="sam-cli-requirements"></a>

SAM CLI is a tool that also allows faster, iterative development of your Lambda function code, which is explained at [Test Your Serverless Applications Locally Using SAM CLI \(Public Beta\)](test-sam-cli.md)\. To use SAM CLI, you first need to install Docker\.

## Installing Docker<a name="sam-cli-requirements-docker"></a>

Docker is an open\-source software container platform that allows you to build, manage and test applications, whether you're running on Linux, Mac or Windows\. For more information and download instructions, see [Docker](https://www.docker.com)\. 

Once you have Docker installed, SAM CLI automatically provides a customized Docker image called `docker-lambda`\. This image is designed specifically by an AWS partner to simulate the live AWS Lambda execution environment\. This environment includes installed software, libraries, security permissions, environment variables, and other features outlined at [Lambda Execution Environment and Available Libraries](current-supported-versions.md)\. 

Using `docker-lambda`, you can invoke your Lambda function locally\. In this environment, your serverless applications execute and perform much as in the AWS Lambda runtime, without your having to redeploy the runtime\. Their execution and performance in this environment reflect such considerations as timeouts and memory use\.

**Important**  
Because this is a simulated environment, there is no guarantee that your local testing results will exactly match those in the actual AWS runtime\. 

For more information, see [Docker Lambda](https://github.com/lambci/docker-lambda) on [GitHub](https://github.com/)\. \(If you don't have a Github account, you can create one for free and then access Docker Lambda\)\.

## Installing SAM CLI<a name="sam-cli-requirements-cli"></a>

The easiest way to install SAM CLI is to use [pip](https://pypi.org/project/pip/)\.

You can run SAM CLI on Linux, Mac, or Windows environments\. The easiest way to install SAM CLI is to use [pip](https://pypi.org/project/pip/)\.

To use pip, you must have [Python 2\.7](https://www.python.org/downloads/release/python-2715/) installed and added to your system's Environment path\. 

**Note**  
In a Windows environment, you run pip from the `\Python27\Scripts` directory\.

```
pip install aws-sam-cli
```

Then verify that the installation succeeded\.

```
sam --version
```

You should see something similar to the following:

```
SAM CLI, version 0.3.0
```

To begin using the SAM CLI with your serverless applications, see [Test Your Serverless Applications Locally Using SAM CLI \(Public Beta\)](test-sam-cli.md) 

### Next Step<a name="setting-up-next-step-simple-function"></a>

[Create a Simple Lambda Function and Explore the Console](getting-started-create-function.md)