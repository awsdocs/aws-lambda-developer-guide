# Using AWS Lambda with Amazon API Gateway<a name="services-apigateway"></a>

You can create a web API with an HTTP endpoint for your Lambda function by using Amazon API Gateway\. API Gateway provides tools for creating and documenting web APIs that route HTTP requests to Lambda functions\. You can secure access to your API with authentication and authorization controls\. Your APIs can serve traffic over the internet or can be accessible only within your VPC\.

Resources in your API define one or more methods, such as GET or POST\. Methods have an integration that routes requests to a Lambda function or another integration type\. You can define each resource and method individually, or use special resource and method types to match all requests that fit a pattern\. A *proxy resource* catches all paths beneath a resource\. The `ANY` method catches all HTTP methods\.

**Topics**
+ [Adding an endpoint to your Lambda function](#apigateway-add)
+ [Proxy integration](#apigateway-proxy)
+ [Event format](#apigateway-example-event)
+ [Response format](#apigateway-types-transforms)
+ [Permissions](#apigateway-permissions)
+ [Handling errors with an API Gateway API](#services-apigateway-errors)
+ [Choosing an API type](#services-apigateway-apitypes)
+ [Sample applications](#services-apigateway-samples)
+ [Tutorial: Using Lambda with API Gateway](services-apigateway-tutorial.md)
+ [Sample function code](services-apigateway-code.md)
+ [Create a simple microservice using Lambda and API Gateway](services-apigateway-blueprint.md)
+ [AWS SAM template for an API Gateway application](services-apigateway-template.md)

## Adding an endpoint to your Lambda function<a name="apigateway-add"></a>

**To add a public endpoint to your Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Select **API Gateway**\.

1. Choose **Create an API** or **Use an existing API**\.

   1. **New API:** For **API type**, choose **HTTP API**\. For more information, see [API types](#services-apigateway-apitypes)\.

   1. **Existing API:** Select the API from the dropdown menu or enter the API ID \(for example, r3pmxmplak\)\.

1. For **Security**, choose **Open**\.

1. Choose **Add**\.

## Proxy integration<a name="apigateway-proxy"></a>

API Gateway APIs are comprised of stages, resources, methods, and integrations\. The stage and resource determine the path of the endpoint:

**API path format**
+ `/prod/` – The `prod` stage and root resource\.
+ `/prod/user` – The `prod` stage and `user` resource\.
+ `/dev/{proxy+}` – Any route in the `dev` stage\.
+ `/` – \(HTTP APIs\) The default stage and root resource\.

A Lambda integration maps a path and HTTP method combination to a Lambda function\. You can configure API Gateway to pass the body of the HTTP request as\-is \(custom integration\), or to encapsulate the request body in a document that includes all of the request information including headers, resource, path, and method\.

## Event format<a name="apigateway-example-event"></a>

Amazon API Gateway invokes your function [synchronously](invocation-sync.md) with an event that contains a JSON representation of the HTTP request\. For a custom integration, the event is the body of the request\. For a proxy integration, the event has a defined structure\. The following example shows a proxy event from an API Gateway REST API\.

**Example [event\.json](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig/event.json) API Gateway proxy event \(REST API\)**  

```
{
      "resource": "/",
      "path": "/",
      "httpMethod": "GET",
      "requestContext": {
          "resourcePath": "/",
          "httpMethod": "GET",
          "path": "/Prod/",
          ...
      },
      "headers": {
          "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
          "accept-encoding": "gzip, deflate, br",
          "Host": "70ixmpl4fl.execute-api.us-east-2.amazonaws.com",
          "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36",
          "X-Amzn-Trace-Id": "Root=1-5e66d96f-7491f09xmpl79d18acf3d050",
          ...
      },
      "multiValueHeaders": {
          "accept": [
              "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
          ],
          "accept-encoding": [
              "gzip, deflate, br"
          ],
          ...
      },
      "queryStringParameters": null,
      "multiValueQueryStringParameters": null,
      "pathParameters": null,
      "stageVariables": null,
      "body": null,
      "isBase64Encoded": false
  }
```

## Response format<a name="apigateway-types-transforms"></a>

API Gateway waits for a response from your function and relays the result to the caller\. For a custom integration, you define an integration response and a method response to convert the output from the function to an HTTP response\. For a proxy integration, the function must respond with a representation of the response in a specific format\.

The following example shows a response object from a Node\.js function\. The response object represents a successful HTTP response that contains a JSON document\.

**Example [index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig/function/index.js) – Proxy integration response object \(Node\.js\)**  

```
var response = {
      "statusCode": 200,
      "headers": {
        "Content-Type": "application/json"
      },
      "isBase64Encoded": false,
      "multiValueHeaders": { 
        "X-Custom-Header": ["My value", "My other value"],
      },
      "body": "{\n  \"TotalCodeSize\": 104330022,\n  \"FunctionCount\": 26\n}"
    }
```

The Lambda runtime serializes the response object into JSON and sends it to the API\. The API parses the response and uses it to create an HTTP response, which it then sends to the client that made the original request\.

**Example HTTP response**  

```
< HTTP/1.1 200 OK
  < Content-Type: application/json
  < Content-Length: 55
  < Connection: keep-alive
  < x-amzn-RequestId: 32998fea-xmpl-4268-8c72-16138d629356
  < X-Custom-Header: My value
  < X-Custom-Header: My other value
  < X-Amzn-Trace-Id: Root=1-5e6aa925-ccecxmplbae116148e52f036
  <
  {
    "TotalCodeSize": 104330022,
    "FunctionCount": 26
  }
```

## Permissions<a name="apigateway-permissions"></a>

Amazon API Gateway gets permission to invoke your function from the function's [resource\-based policy](access-control-resource-based.md)\. You can grant invoke permission to an entire API, or grant limited access to a stage, resource, or method\.

When you add an API to your function by using the Lambda console, using the API Gateway console, or in an AWS SAM template, the function's resource\-based policy is updated automatically\. The following is an example function policy\.

**Example function policy**  

```
{
  "Version": "2012-10-17",
  "Id": "default",
  "Statement": [
    {
      "Sid": "nodejs-apig-functiongetEndpointPermissionProd-BWDBXMPLXE2F",
      "Effect": "Allow",
      "Principal": {
        "Service": "apigateway.amazonaws.com"
      },
      "Action": "lambda:InvokeFunction",
      "Resource": "arn:aws:lambda:us-east-2:111122223333:function:nodejs-apig-function-1G3MXMPLXVXYI",
      "Condition": {
        "StringEquals": {
          "aws:SourceAccount": "111122223333"
        },
        "ArnLike": {
          "aws:SourceArn": "arn:aws:execute-api:us-east-2:111122223333:ktyvxmpls1/*/GET/"
        }
      }
    }
  ]
}
```

You can manage function policy permissions manually with the following API operations:
+ [AddPermission](API_AddPermission.md)
+ [RemovePermission](API_RemovePermission.md)
+ [GetPolicy](API_GetPolicy.md)

To grant invocation permission to an existing API, use the `add-permission` command\.

```
aws lambda add-permission --function-name my-function \
--statement-id apigateway-get --action lambda:InvokeFunction \
--principal apigateway.amazonaws.com \
--source-arn "arn:aws:execute-api:us-east-2:123456789012:mnh1xmpli7/default/GET/"
```

You should see the following output:

```
{
    "Statement": "{\"Sid\":\"apigateway-test-2\",\"Effect\":\"Allow\",\"Principal\":{\"Service\":\"apigateway.amazonaws.com\"},\"Action\":\"lambda:InvokeFunction\",\"Resource\":\"arn:aws:lambda:us-east-2:123456789012:function:my-function\",\"Condition\":{\"ArnLike\":{\"AWS:SourceArn\":\"arn:aws:execute-api:us-east-2:123456789012:mnh1xmpli7/default/GET\"}}}"
}
```

**Note**  
If your function and API are in different regions, the region identifier in the source ARN must match the region of the function, not the region of the API\. When API Gateway invokes a function, it uses a resource ARN that is based on the ARN of the API, but modified to match the function's region\.

The source ARN in this example grants permission to an integration on the GET method of the root resource in the default stage of an API, with ID `mnh1xmpli7`\. You can use an asterisk in the source ARN to grant permissions to multiple stages, methods, or resources\.

**Resource patterns**
+ `mnh1xmpli7/*/GET/*` – GET method on all resources in all stages\.
+ `mnh1xmpli7/prod/ANY/user` – ANY method on the `user` resource in the `prod` stage\.
+ `mnh1xmpli7/*/*/*` – Any method on all resources in all stages\.

For details on viewing the policy and removing statements, see [Cleaning up resource\-based policies](access-control-resource-based.md#permissions-resource-cleanup)\.

## Handling errors with an API Gateway API<a name="services-apigateway-errors"></a>

API Gateway treats all invocation and function errors as internal errors\. If the Lambda API rejects the invocation request, API Gateway returns a 500 error code\. If the function runs but returns an error, or returns a response in the wrong format, API Gateway returns a 502\. In both cases, the body of the response from API Gateway is `{"message": "Internal server error"}`\.

**Note**  
API Gateway does not retry any Lambda invocations\. If Lambda returns an error, API Gateway returns an error response to the client\.

The following example shows an X\-Ray trace map for a request that resulted in a function error and a 502 from API Gateway\. The client receives the generic error message\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/tracemap-apig-502.png)

To customize the error response, you must catch errors in your code and format a response in the required format\.

**Example [index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig/function/index.js) – Error formatting**  

```
var formatError = function(error){
  var response = {
    "statusCode": error.statusCode,
    "headers": {
      "Content-Type": "text/plain",
      "x-amzn-ErrorType": error.code
    },
    "isBase64Encoded": false,
    "body": error.code + ": " + error.message
  }
  return response
}
```

API Gateway converts this response into an HTTP error with a custom status code and body\. In the trace map, the function node is green because it handled the error\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/tracemap-apig-404.png)

## Choosing an API type<a name="services-apigateway-apitypes"></a>

API Gateway supports three types of APIs that invoke Lambda functions:
+ **HTTP API** – A lightweight, low\-latency RESTful API\.
+ **REST API** – A customizable, feature\-rich RESTful API\.
+ **WebSocket API** – A web API that maintains persistent connections with clients for full\-duplex communication\.

HTTP APIs and REST APIs are both RESTful APIs that process HTTP requests and return responses\. HTTP APIs are newer and are built with the API Gateway version 2 API\. The following features are new for HTTP APIs:

**HTTP API features**
+ **Automatic deployments** – When you modify routes or integrations, changes deploy automatically to stages that have automatic deployment enabled\.
+ **Default stage** – You can create a default stage \(`$default`\) to serve requests at the root path of your API's URL\. For named stages, you must include the stage name at the beginning of the path\.
+ **CORS configuration** – You can configure your API to add CORS headers to outgoing responses, instead of adding them manually in your function code\.

REST APIs are the classic RESTful APIs that API Gateway has supported since launch\. REST APIs currently have more customization, integration, and management features\.

**REST API features**
+ **Integration types** – REST APIs support custom Lambda integrations\. With a custom integration, you can send just the body of the request to the function, or apply a transform template to the request body before sending it to the function\.
+ **Access control** – REST APIs support more options for authentication and authorization\.
+ **Monitoring and tracing** – REST APIs support AWS X\-Ray tracing and additional logging options\.

For a detailed comparison, see [Choosing between HTTP APIs and REST APIs](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-vs-rest.html) in the *API Gateway Developer Guide*\.

WebSocket APIs also use the API Gateway version 2 API and support a similar feature set\. Use a WebSocket API for applications that benefit from a persistent connection between the client and API\. WebSocket APIs provide full\-duplex communication, which means that both the client and the API can send messages continuously without waiting for a response\.

HTTP APIs support a simplified event format \(version 2\.0\)\. The following example shows an event from an HTTP API\.

**Example [event\-v2\.json](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig/event-v2.json) – API Gateway proxy event \(HTTP API\)**  

```
{
    "version": "2.0",
    "routeKey": "ANY /nodejs-apig-function-1G3XMPLZXVXYI",
    "rawPath": "/default/nodejs-apig-function-1G3XMPLZXVXYI",
    "rawQueryString": "",
    "cookies": [
        "s_fid=7AABXMPL1AFD9BBF-0643XMPL09956DE2",
        "regStatus=pre-register"
    ],
    "headers": {
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "accept-encoding": "gzip, deflate, br",
        ...
    },
    "requestContext": {
        "accountId": "123456789012",
        "apiId": "r3pmxmplak",
        "domainName": "r3pmxmplak.execute-api.us-east-2.amazonaws.com",
        "domainPrefix": "r3pmxmplak",
        "http": {
            "method": "GET",
            "path": "/default/nodejs-apig-function-1G3XMPLZXVXYI",
            "protocol": "HTTP/1.1",
            "sourceIp": "205.255.255.176",
            "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"
        },
        "requestId": "JKJaXmPLvHcESHA=",
        "routeKey": "ANY /nodejs-apig-function-1G3XMPLZXVXYI",
        "stage": "default",
        "time": "10/Mar/2020:05:16:23 +0000",
        "timeEpoch": 1583817383220
    },
    "isBase64Encoded": true
}
```

For more information, see [AWS Lambda integrations](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-develop-integrations-lambda.html) in the API Gateway Developer Guide\.

## Sample applications<a name="services-apigateway-samples"></a>

The GitHub repository for this guide provides the following sample application for API Gateway\.
+ [API Gateway with Node\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig) – A function with an AWS SAM template that creates a REST API that has AWS X\-Ray tracing enabled\. It includes scripts for deploying, invoking the function, testing the API, and cleanup\.

Lambda also provides [blueprints](gettingstarted-features.md#gettingstarted-features-blueprints) and [templates](gettingstarted-features.md#gettingstarted-features-templates) that you can use to create an API Gateway application in the Lambda console\.