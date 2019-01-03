# AWS Lambda Function Logging in C\#<a name="dotnet-logging"></a>

Your Lambda function can contain logging statements and, in turn, AWS Lambda writes these logs to CloudWatch Logs\. 

In the C\# programming model, there are three ways to log data in your function: 
+ Use the static `Write` or `WriteLine` methods provided by the C\# `Console` class\. Anything written to standard out or standard error \- using Console\.Write or a similar method \- will be logged in CloudWatch Logs\. 

  ```
  public class ProductService
  {
     public async Task<Product> DescribeProduct(DescribeProductRequest request)
      {
         Console.WriteLine("DescribeProduct invoked with Id " + request.Id);
         return await catalogService.DescribeProduct(request.Id);
      }
  }
  ```
+ Use the `Log` method on the `Amazon.Lambda.Core.LambdaLogger` class\. This is a static class that can be used anywhere in your application\. To use this, you must include the `Amazon.Lambda.Core ` library\. 

  ```
  using Amazon.Lambda.Core;
                              
  public class ProductService
  {
     public async Task<Product> DescribeProduct(DescribeProductRequest request)
     {
         LambdaLogger.Log("DescribeProduct invoked with Id " + request.Id);
         return await catalogService.DescribeProduct(request.Id);
     }
  }
  ```

  Each call to `LambdaLogger.Log` results in a CloudWatch Logs event, provided the event size is within the allowed limits\. For information about CloudWatch Logs limits, see [CloudWatch Logs Limits](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/cloudwatch_limits_cwl.html) in the *Amazon CloudWatch User Guide*\.
+ Use the logger in `ILambdaContext`\. The `ILambdaContext` object \(if specified\) in your method contains a `Logger` property that represents a LambdaLogger\. The following is an example of using this method: 

  ```
  public class ProductService
  {
     public async Task<Product> DescribeProduct(DescribeProductRequest request, ILambdaContext context)
     {
         context.Logger.Log("DescribeProduct invoked with Id " + request.Id);
         return await catalogService.DescribeProduct(request.Id);
     }
  }
  ```

## How to Find Logs<a name="how-to-find-logs-dotnet"></a>

You can find the logs that your Lambda function writes, as follows:
+ Find logs in CloudWatch Logs\. The `ILambdaContext` object provides the `LogStreamName` and the `LogGroupName` properties\. Using these properties, you can find the specific log stream where logs are written\.
+ If you invoke a Lambda function via the console, the invocation type is always `RequestResponse` \(that is, synchronous execution\) and the console displays the logs that the Lambda function writes using the `LambdaLogger` object\. AWS Lambda also returns logs from `Console.Write` and `Console.WriteLine` methods\.
+ If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that is written to CloudWatch Logs\. For more information, see [Invoke](API_Invoke.md)\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. If you use the AWS Command Line Interface to invoke the function, you can specify the `--log-type` parameter with value `Tail`\. 