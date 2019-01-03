# Using AWS Lambda with an Application Load Balancer<a name="services-alb"></a>

You can use a Lambda function to process requests from an Application Load Balancer\. Elastic Load Balancing supports Lambda functions as a target for an Application Load Balancer\. Use load balancer rules to route HTTP requests to a function, based on path or header values\. Process the request and return an HTTP response from your Lambda function\.

Elastic Load Balancing invokes your Lambda function synchronously with an event that contains the request body and metadata\.

**Example Application Load Balancer Request Event**  

```
{
    'requestContext': {
        'elb': {
            'targetGroupArn': 'arn:aws:elasticloadbalancing:us-east-1:011685312445:targetgroup/lambda-target/d6190d154bc908a5'
        }
    },
    'httpMethod': 'GET',
    'path': '/health',
    'queryStringParameters': {},
    'headers': {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'accept-encoding': 'gzip',
        'accept-language': 'en-US,en;q=0.5',
        'connection': 'keep-alive',
        'cookie': 'cookie',
        'host': 'lambda-846800462.elb.amazonaws.com',
        'upgrade-insecure-requests': '1',
        'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:60.0) Gecko/20100101 Firefox/60.0',
        'x-amzn-trace-id': 'Root=1-5bdb40ca-556d8b0c50dc66f0511bf520',
        'x-forwarded-for': '72.21.198.66',
        'x-forwarded-port': '80',
        'x-forwarded-proto': 'http',
    },
    'body': '',
    'isBase64Encoded': False
}
```

Your function processes the event and returns a response to the load balancer in JSON\. Elastic Load Balancing converts the response to HTTP and returns it to the user\.

**Example Response Format**  

```
{
    "statusCode": 200,
    "statusDescription": "HTTP OK",
    "isBase64Encoded": False,
    "headers": {
        "server": "my-server",
        "set-cookie": "name=value",
        "Content-Type": "text/html; charset=utf-8"
    },
    "body": "Welcome"
}
```

To configure an Application Load Balancer as a function trigger, grant Elastic Load Balancing permission to execute the function, create a target group that routes requests to the function, and add a rule to the load balancer that sends requests to the target group\.

Use the `add-permission` command to add a permission statement to your function's resource\-based policy\.

```
$ aws lambda add-permission --function-name alb-function \
--statement-id load-balancer --action "lambda:InvokeFunction" \
--principal elasticloadbalancing.amazonaws.com
{
    "Statement": "{\"Sid\":\"load-balancer\",\"Effect\":\"Allow\",\"Principal\":{\"Service\":\"elasticloadbalancing.amazonaws.com\"},\"Action\":\"lambda:InvokeFunction\",\"Resource\":\"arn:aws:lambda:us-west-2:123456789012:function:alb-function\"}"
}
```

For instructions on configuring the Application Load Balancer listener and target group, see [Lambda Functions as a Target](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/lambda-functions.html) in the *User Guide for Application Load Balancers*\.