using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.Util;
using Amazon.EC2;
using Amazon.EC2.Model;
using Amazon.Lambda.Core;
using Amazon.XRay.Recorder.Core;
using Amazon.XRay.Recorder.Handlers.AwsSdk;

[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace ec2spot
{
    public class Function
    {
        private static AmazonEC2Client ec2Client;

        static Function() {
          AWSSDKHandler.RegisterXRayForAllServices();
          ec2Client = new AmazonEC2Client();
        }

        public async Task<string> FunctionHandler(Dictionary<string, string> input, ILambdaContext context)
        {
          // More AMI IDs: aws.amazon.com/amazon-linux-2/release-notes/
          // us-east-2  HVM  EBS-Backed  64-bit  Amazon Linux 2
          string ami = "ami-09d9edae5eb90d556";
          string sg = "default";
          // docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/TInstanceType.html
          InstanceType type = InstanceType.T3aNano;
          string price = "0.003";
          int count = 1;
          var requestSpotInstances = await RequestSpotInstance(ami, sg, type, price, count);
          var spotRequestId = requestSpotInstances.SpotInstanceRequests[0].SpotInstanceRequestId;
          Console.WriteLine(spotRequestId);

          string instanceId;
          while (true)
          {
            SpotInstanceRequest spotRequest = await GetSpotRequest(spotRequestId);
            Console.WriteLine(spotRequest.State);
            if (spotRequest.State == SpotInstanceState.Active) {
              instanceId = spotRequest.InstanceId;
              break;
            }
          }
          var cancelRequest = CancelSpotRequest(spotRequestId);
          var terminateRequest = TerminateSpotInstance(instanceId);

          await Task.WhenAll(cancelRequest, terminateRequest);

          Console.WriteLine("Complete");
          return spotRequestId;
        }

        public async Task<RequestSpotInstancesResponse> RequestSpotInstance(
          string amiId,
          string securityGroupName,
          InstanceType instanceType,
          string spotPrice,
          int instanceCount)
        {
          var request = new RequestSpotInstancesRequest();

          // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_RequestSpotLaunchSpecification.html
          var launchSpecification = new LaunchSpecification();
          launchSpecification.ImageId = amiId;
          launchSpecification.InstanceType = instanceType;
          launchSpecification.SecurityGroups.Add(securityGroupName);

          request.SpotPrice = spotPrice;
          request.InstanceCount = instanceCount;
          request.LaunchSpecification = launchSpecification;

          RequestSpotInstancesResponse response =  await ec2Client.RequestSpotInstancesAsync(request);

          return response;
        }
        public async Task<SpotInstanceRequest> GetSpotRequest(string spotRequestId)
        {
          var request = new DescribeSpotInstanceRequestsRequest();
          request.SpotInstanceRequestIds.Add(spotRequestId);

          var describeResponse = await ec2Client.DescribeSpotInstanceRequestsAsync(request);

          return describeResponse.SpotInstanceRequests[0];
        }
        public async Task CancelSpotRequest(string spotRequestId)
        {
          Console.WriteLine("Canceling request " + spotRequestId);
          var cancelRequest = new CancelSpotInstanceRequestsRequest();
          cancelRequest.SpotInstanceRequestIds.Add(spotRequestId);

          await ec2Client.CancelSpotInstanceRequestsAsync(cancelRequest);
        }
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
    }
}
