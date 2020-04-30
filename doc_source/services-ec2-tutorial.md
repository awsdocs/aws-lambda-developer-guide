# Tutorial: Using AWS SDK for \.NET to manage Amazon EC2 Spot Instances<a name="services-ec2-tutorial"></a>

You can use the AWS SDK for \.NET to manage Amazon EC2 spot instances with C\# code\. The SDK enables you to use the Amazon EC2 API to create spot instance requests, determine when the request is fulfilled, delete requests, and identify the instances created\.

This tutorial provides code that performs these tasks and a sample application that you can run locally or on AWS\. It includes a sample project that you can deploy to AWS Lambda's \.NET Core 2\.1 runtime\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-ec2spot.png)

For more information about spot instances usage and best practices, see [Spot Instances](https://docs.aws.amazon.com/AWSEC2/latest/DeveloperGuide/using-spot-instances.html) in the Amazon EC2 user guide\.

## Prerequisites<a name="services-ec2-tutorial-prereqs"></a>

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

This tutorial uses code from the developer guide's GitHub repository\. The repository also contains helper scripts and configuration files that are needed to follow its procedures\. Clone the repository at [github\.com/awsdocs/aws\-lambda\-developer\-guide](https://github.com/awsdocs/aws-lambda-developer-guide)\.

To use the sample code you need the following tools:
+ **AWS CLI** – To deploy the sample application to AWS, install the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\. The AWS CLI also provides credentials to the sample code when you run it locally\.
+ **\.NET Core CLI** – To run and test the code locally, install the [\.NET Core SDK 2\.1](https://dotnet.microsoft.com/download/dotnet-core/2.1)\.
+ **Lambda \.NET Core Global Tool** – To build the deployment package for Lambda, install the [\.NET Core global tool](https://dotnet.microsoft.com/download/dotnet-core/2.1) with the \.NET Core CLI\.

  ```
  $ dotnet tool install -g [Amazon\.Lambda\.Tools](https://www.nuget.org/packages/Amazon.Lambda.Tools)
  ```

The code in this tutorial manages spot requests that launch Amazon EC2 instances\. To run the code locally, you need SDK credentials with permission to use the following APIs\.
+ `ec2:RequestSpotInstance`
+ `ec2:GetSpotRequestState`
+ `ec2:CancelSpotRequest`
+ `ec2:TerminateInstances`

To run the sample application in AWS, you need [permission to use Lambda](lambda-permissions.md) and the following services\.
+ [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-iam-template.html) \([pricing](https://aws.amazon.com/cloudformation/pricing/)\)
+ [Amazon Elastic Compute Cloud](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-policies-for-amazon-ec2.html) \([pricing](https://aws.amazon.com/ec2/pricing/)\)

Standard charges apply for each service\.

## Review the code<a name="services-ec2-tutorial-code"></a>

Locate the sample project in the guide repository under [sample\-apps/ec2\-spot](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/ec2-spot)\. This directory contains Lambda function code, tests, project files, scripts, and a AWS CloudFormation template\.

The `Function` class includes a `FunctionHandler` method that calls other methods to create spot requests, check their status, and clean up\. It creates an Amazon EC2 client with the AWS SDK for \.NET in a static constructor to allow it to be used throughout the class\.

**Example [Function\.cs – FunctionHandler](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/ec2-spot/src/ec2spot/Function.cs#L17)**  

```
using Amazon.EC2;
...
    public class Function
    {
        private static AmazonEC2Client ec2Client;

        static Function() {
          AWSSDKHandler.RegisterXRayForAllServices();
          ec2Client = new AmazonEC2Client();
        }

        public async Task<string> FunctionHandler(Dictionary<string, string> input, ILambdaContext context)
        {
          // More AMI IDs: [aws\.amazon\.com/amazon\-linux\-2/release\-notes/](https://aws.amazon.com/amazon-linux-2/release-notes/)
          // us-east-2  HVM  EBS-Backed  64-bit  Amazon Linux 2
          string ami = "ami-09d9edae5eb90d556";
          string sg = "default";
          [InstanceType](https://docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/TInstanceType.html) type = InstanceType.T3aNano;
          string price = "0.003";
          int count = 1;
          var requestSpotInstances = await RequestSpotInstance(ami, sg, type, price, count);
          var spotRequestId = requestSpotInstances.SpotInstanceRequests[0].SpotInstanceRequestId;
```

The `RequestSpotInstance` method creates a spot instance request\.

**Example [Function\.cs – RequestSpotInstance](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/ec2-spot/src/ec2spot/Function.cs#L59)**  

```
using Amazon;
using Amazon.Util;
using Amazon.EC2;
using Amazon.EC2.Model;
...
    public async Task<RequestSpotInstancesResponse> RequestSpotInstance(
      string amiId,
      string securityGroupName,
      InstanceType instanceType,
      string spotPrice,
      int instanceCount)
    {
      var request = new RequestSpotInstancesRequest();

      var launchSpecification = new [LaunchSpecification](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_RequestSpotLaunchSpecification.html)();
      launchSpecification.ImageId = amiId;
      launchSpecification.InstanceType = instanceType;
      launchSpecification.SecurityGroups.Add(securityGroupName);

      request.SpotPrice = spotPrice;
      request.InstanceCount = instanceCount;
      request.LaunchSpecification = launchSpecification;

      RequestSpotInstancesResponse response =  await ec2Client.RequestSpotInstancesAsync(request);

      return response;
    }
...
```

Next, you need to wait until the spot request reaches the `Active` state before proceeding to the last step\. To determine the state of your spot request, use the [DescribeSpotInstanceRequests](https://docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/TDescribeSpotInstanceRequestsRequest.html) method to obtain the state of the spot request ID to monitor\.

```
public async Task<SpotInstanceRequest> GetSpotRequest(string spotRequestId)
{
  var request = new DescribeSpotInstanceRequestsRequest();
  request.SpotInstanceRequestIds.Add(spotRequestId);

  var describeResponse = await ec2Client.DescribeSpotInstanceRequestsAsync(request);

  return describeResponse.SpotInstanceRequests[0];
}
```

The final step is to clean up your requests and instances\. It is important to both cancel any outstanding requests and terminate any instances\. Just canceling your requests will not terminate your instances, which means that you will continue to be charged for them\. If you terminate your instances, your Spot requests may be canceled, but there are some scenarios, such as if you use persistent requests, where terminating your instances is not sufficient to stop your request from being re\-fulfilled\. Therefore, it is a best practice to both cancel any active requests and terminate any running instances\.

You use the [CancelSpotInstanceRequests](https://docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/MEC2CancelSpotInstanceRequestsCancelSpotInstanceRequestsRequest.html) method to cancel a Spot request\. The following example demonstrates how to cancel a Spot request\.

```
public async Task CancelSpotRequest(string spotRequestId)
{
  Console.WriteLine("Canceling request " + spotRequestId);
  var cancelRequest = new CancelSpotInstanceRequestsRequest();
  cancelRequest.SpotInstanceRequestIds.Add(spotRequestId);

  await ec2Client.CancelSpotInstanceRequestsAsync(cancelRequest);
}
```

You use the [TerminateInstances](https://docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/MEC2TerminateInstancesTerminateInstancesRequest.html) method to terminate an instance\.

```
public async Task TerminateSpotInstance(string instanceId)
{
  Console.WriteLine("Terminating instance " + instanceId);
  var terminateRequest = new TerminateInstancesRequest();
  terminateRequest.InstanceIds = new List<string>() { instanceId };
  try
  {
    var terminateResponse = await ec2Client.TerminateInstancesAsync(terminateRequest);
  }
  catch (AmazonEC2Exception ex)
  {
    // Check the ErrorCode to see if the instance does not exist.
    if ("InvalidInstanceID.NotFound" == ex.ErrorCode)
    {
      Console.WriteLine("Instance {0} does not exist.", instanceId);
    }
    else
    {
      // The exception was thrown for another reason, so re-throw the exception.
      throw;
    }
  }
}
```

## Run the code locally<a name="services-ec2-tutorial-run"></a>

Run the code on your local machine to create a spot instance request\. After the request is fulfilled, the code deletes the request and terminates the instance\.

**To run the application code**

1. Navigate to the `ec2Spot.Tests` directory\.

   ```
   $ cd test/ec2Spot.Tests
   ```

1. Use the \.NET CLI to run the project's unit tests\.

   ```
   test/ec2Spot.Tests$ dotnet test
   Starting test execution, please wait...
   sir-x5tgs5ij
   open
   open
   open
   open
   open
   active
   Canceling request sir-x5tgs5ij
   Terminating instance i-0b3fdff0e12e0897e
   Complete
   
   Test Run Successful.
   Total tests: 1
        Passed: 1
    Total time: 7.6060 Seconds
   ```

The unit test invokes the FunctionHandler method to create a spot instance request, monitor it, and clean up\. It is implemented in the [xUnit\.net](https://xunit.net/) testing framework\.

## Deploy the application<a name="services-ec2-tutorial-deploy"></a>

Run the code in Lambda as a starting point for creating a serverless application\.

**To deploy and test the application**

1. Set your region to `us-east-2`\.

   ```
   $ export AWS_DEFAULT_REGION=us-east-2
   ```

1. Create a bucket for deployment artifacts\.

   ```
   $ ./create-bucket.sh
   make_bucket: lambda-artifacts-63d5cbbf18fa5ecc
   ```

1. Create a deployment package and deploy the application\.

   ```
   $ ./deploy.sh
   Amazon Lambda Tools for .NET Core applications (3.3.0)
   Project Home: https://github.com/aws/aws-extensions-for-dotnet-cli, https://github.com/aws/aws-lambda-dotnet
   
   Executing publish command
   ...
   Created publish archive (ec2spot.zip)
   Lambda project successfully packaged: ec2spot.zip
   Uploading to ebd38e401cedd7d676d05d22b76f0209  1305107 / 1305107.0  (100.00%)
   Successfully packaged artifacts and wrote output template to file out.yaml.
   Execute the following command to deploy the packaged template
   aws cloudformation deploy --template-file out.yaml --stack-name <YOUR STACK NAME>
   
   Waiting for changeset to be created..
   Waiting for stack create/update to complete
   Successfully created/updated stack - ec2-spot
   ```

1. Open the [Applications page](https://us-east-2.console.aws.amazon.com/lambda/home?region=us-east-2#/applications/ec2-spot) of the Lambda console\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-ec2spot-application.png)

1. Under **Resources**, choose **function**\.

1. Choose **Test** and create a test event from the default template\.

1. Choose **Test** again to invoke the function\.

View the logs and trace information to see the spot request ID and sequence of calls to Amazon EC2\.

To view the service map, open the [Service map page](https://console.aws.amazon.com/xray/home#/service-map) in the X\-Ray console\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-ec2spot-servicemap.png)

Choose a node in the service map and then choose **View traces** to see a list of traces\. Choose a trace from the list to see the timeline of calls that the function made to Amazon EC2\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-ec2spot-timeline.png)

## Clean up<a name="services-ec2-tutorial-cleanup"></a>

The code provided in this tutorial is designed to create and delete spot instance requests, and to terminate the instances that they launch\. However, if an error occurs, the requests and instances might not be cleaned up automatically\. View the spot requests and instances in the Amazon EC2 console\.

**To confirm that Amazon EC2 resources are cleaned up**

1. Open the [Spot Requests page](https://console.aws.amazon.com/ec2sp/v1/spot/home) in the Amazon EC2 console\.

1. Verify that the state of the requests is **Cancelled**\.

1. Choose the instance ID in the **Capacity** column to view the instance\.

1. Verify that the state of the instances is **Terminated** or **Shutting down**\.

To clean up the sample function and support resources, delete its AWS CloudFormation stack and the artifacts bucket that you created\.

```
$ ./cleanup.sh
Delete deployment artifacts and bucket (lambda-artifacts-63d5cbbf18fa5ecc)?y
delete: s3://lambda-artifacts-63d5cbbf18fa5ecc/ebd38e401cedd7d676d05d22b76f0209
remove_bucket: lambda-artifacts-63d5cbbf18fa5ecc
```

The function's log group is not deleted automatically\. You can delete it in the [CloudWatch Logs console](https://console.aws.amazon.com/cloudwatch/home#logs:)\. Traces in X\-Ray expire after a few weeks and are deleted automatically\.