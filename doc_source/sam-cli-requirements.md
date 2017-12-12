# Install SAM Local<a name="sam-cli-requirements"></a>

SAM Local is a tool that also allows faster, iterative development of your Lambda function code, which is explained at [Test Your Serverless Applications Locally Using SAM Local \(Public Beta\)](test-sam-local.md)\. To use SAM Local, you first need need to install Docker\.

## Installing Docker<a name="sam-cli-requirements-docker"></a>

Docker is an open\-source software container platform that allows you to build, manage and test applications, whether you're running on Linux, Mac or Windows\. For more information and download instructions, see [Docker](https://www.docker.com)\. 

Once you have Docker installed, SAM Local automatically provides a customized Docker image called `docker-lambda`\. This image is designed specifically by an AWS partner to simulate the live AWS Lambda execution environment\. This environment includes installed software, libraries, security permissions, environment variables, and other features outlined at [Lambda Execution Environment and Available Libraries](current-supported-versions.md)\. 

Using `docker-lambda`, you can invoke your Lambda function locally\. In this environment, your serverless applications execute and perform much as in the AWS Lambda runtime, without your having to redeploy the runtime\. Their execution and performance in this environment reflect such considerations as timeouts and memory use\.

**Important**  
Because this is a simulated environment, there is no guarantee that your local testing results will exactly match those in the actual AWS runtime\. 

For more information, see [Docker Lambda](https://github.com/lambci/docker-lambda) on [GitHub](https://github.com/)\. \(If you don't have a Github account, you can create one for free and then access Docker Lambda\)\.

## Installing SAM Local<a name="sam-cli-requirements-cli"></a>

You can run SAM Local on Linux, Mac, and Windows environments\. The easiest way to install SAM Local is to use [NPM](https://npmjs.com/package/aws-sam-local)\.

```
npm install -g aws-sam-local
```

Then verify that the installation succeeded\.

```
sam --version
```

If NPM doesn't work for you, you can download the latest binary and start using SAM Local immediately\. You can find the binaries under the Releases section in the [SAM CLI GitHub Repository](https://github.com/awslabs/aws-sam-local/releases)\.

### Next Step<a name="setting-up-next-step-simple-function"></a>

[Create a Simple Lambda Function and Explore the Console](getting-started-create-function.md)