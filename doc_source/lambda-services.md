# Using AWS Lambda with Other Services<a name="lambda-services"></a>

AWS Lambda integrates with other AWS services to invoke functions\. You can configure triggers to invoke a function in response to resource lifecycle events, respond to incoming HTTP requests, consume events from a queue, or [run on a schedule](with-scheduled-events.md)\.

Each service that integrates with Lambda sends data to your function in JSON as an event\. The structure of the event document is different for each event type, and contains data about the resource or request that triggered the function\. Lambda runtimes convert the event into an object and pass it to your function\.

The following example shows a test event from an [Application Load Balancer](services-alb.md) that represents a GET request to `/lambda?query=1234ABCD`\.

**Example Event from an Application Load Balancer**  

```
{
    "requestContext": {
        "elb": {
            "targetGroupArn": "arn:aws:elasticloadbalancing:us-east-2:123456789012:targetgroup/lambda-279XGJDqGZ5rsrHC2Fjr/49e9d65c45c6791a"
        }
    },
    "httpMethod": "GET",
    "path": "/lambda",
    "queryStringParameters": {
        "query": "1234ABCD"
    },
    "headers": {
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "accept-encoding": "gzip",
        "accept-language": "en-US,en;q=0.9",
        "connection": "keep-alive",
        "host": "lambda-alb-123578498.us-east-2.elb.amazonaws.com",
        "upgrade-insecure-requests": "1",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
        "x-amzn-trace-id": "Root=1-5c536348-3d683b8b04734faae651f476",
        "x-forwarded-for": "72.12.164.125",
        "x-forwarded-port": "80",
        "x-forwarded-proto": "http",
        "x-imforwards": "20"
    },
    "body": "",
    "isBase64Encoded": false
}
```

**Note**  
The Lambda runtime converts the event document into an object and passes it to your [function handler](programming-model-v2.md)\. For compiled languages, Lambda provides definitions for event types in a library\. See the following topics for more information\.  
[Building Lambda Functions with Java](java-programming-model.md)
[Building Lambda Functions with Go](go-programming-model.md)
[Building Lambda Functions with C\#](dotnet-programming-model.md)

For services that generate a queue or data stream, you create an [event source mapping](intro-invocation-modes.md) in Lambda and grant Lambda permission to access the other service in the [execution role](lambda-intro-execution-role.md)\. Lambda reads data from the other service, creates an event, and invokes your function\.

**Services That Lambda Reads Events From**
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Simple Queue Service](with-sqs.md)

Other services invoke your function directly\. You grant the other service permission in the function's [resource\-based policy](access-control-resource-based.md), and configure the other service to generate events and invoke your function\. Depending on the service, the invocation can be synchronous or asynchronous\. For synchronous invocation, the other service waits for the response from your function and might [retry on errors](retries-on-errors.md)\.

**Services That Invoke Lambda Functions Synchronously**
+ [Elastic Load Balancing \(Application Load Balancer\)](services-alb.md)
+ [Amazon Cognito](services-cognito.md)
+ [Amazon Lex](services-lex.md)
+ [Amazon Alexa](services-alexa.md)
+ [Amazon API Gateway](with-on-demand-https.md)
+ [Amazon CloudFront \(Lambda@Edge\)](lambda-edge.md)
+ [Amazon Kinesis Data Firehose](services-kinesisfirehose.md)

For asynchronous invocation, Lambda queues the event before passing it to your function\. The other service gets a success response as soon as the event is queued and isn't aware of what happens afterwards\. If an error occurs, Lambda handles [retries](retries-on-errors.md), and can send failed events to a [dead\-letter queue](dlq.md) that you configure\.

**Services That Invoke Lambda Functions Asynchronously**
+ [Amazon Simple Storage Service](with-s3.md)
+ [Amazon Simple Notification Service](with-sns.md)
+ [Amazon Simple Email Service](services-ses.md)
+ [AWS CloudFormation](services-cloudformation.md)
+ [Amazon CloudWatch Logs](services-cloudwatchlogs.md)
+ [Amazon CloudWatch Events](with-scheduled-events.md)
+ [AWS CodeCommit](services-codecommit.md)
+ [AWS Config](services-config.md)

See the topics in this chapter for more details about each service, and example events that you can use to test your function\.