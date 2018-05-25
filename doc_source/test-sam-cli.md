# Test Your Serverless Applications Locally Using SAM CLI \(Public Beta\)<a name="test-sam-cli"></a>

**Note**  
This feature is available as part of a public beta and is subject to change at any time\.

[AWS SAM](https://docs.aws.amazon.com/lambda/latest/dg/deploying-lambda-apps.html#Using the AWS Serverless Application Model (AWS SAM)) is a fast and easy way of deploying your serverless applications, allowing you to write simple templates to describe your functions and their event sources \(Amazon API Gateway, Amazon S3, Kinesis, and so on\)\. 

Based on AWS SAM, SAM CLI is a tool that provides an environment for you to develop, test, and analyze your serverless applications locally before uploading them to the Lambda runtime\. Whether you're developing on Linux, Mac, or Microsoft Windows, you can use SAM CLI to create a local testing environment that simulates the AWS runtime environment\. The SAM CLI also allows faster, iterative development of your Lambda function code\. For more information, see [Building a Simple Application Using SAM CLI](#sam-cli-simple-app)\.

SAM CLI works with [AWS SAM](https://docs.aws.amazon.com/lambda/latest/dg/deploying-lambda-apps.html#Using the AWS Serverless Application Model (AWS SAM)), allowing you to invoke functions defined in SAM templates, whether directly or through API Gateway endpoints\. By using SAM CLI features, you can analyze your serverless application's performance in your own testing environment and update accordingly\.

SAM CLI also offers the `sam-init` command, which, when run, provides a fully\-functional SAM application that you can use to further your understanding of the SAM application model or enhance your application to meet production needs\. For more information, see [Create a Simple App \(sam init\) ](serverless_app.md#serv-app-sam-init)\. 

 The following examples outline additional advantages of using SAM CLI with sample operation code\. For instance, you can do the following: 
+ Generate sample function payloads \(for example, an Amazon S3 event\)\.

  ```
  $ sam local generate-event s3 --bucket bucket-name  --key key-name
                  > event_file.json
  ```
+ Test a sample function payload locally with your Lambda functions\.

  ```
  $ sam local invoke function-name -e event_file.json
  ```
+ Spawn a local API Gateway to test HTTP request and response functionality\. By using the hot reloading feature, you can test and iterate your functions without having to restart or reload them to the AWS runtime\.

  ```
  $ sam local start-api
  ```

   SAM CLI will automatically find any functions within your SAM template that have API event sources defined, and mount them at the defined HTTP paths\. In the example below, the `Ratings` function would mount `ratings.py:handler() `at `/ratings` for `GET` requests\. 

  ```
  Ratings:
      Type: AWS::Serverless::Function
         Properties:
             Handler: ratings.handler
                  Runtime: python3.6
                  Events:
                  Api:
                  Type: Api
                  Properties:
                  Path: /ratings
                  Method: get
  ```

  By default, SAM CLI uses [ Proxy Integration](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html) and expects the response from your Lambda function to include one or more of the following: `statusCode`, `headers` and/or `body`\. For example: 

  ```
  // Example of a Proxy Integration response
  exports.handler = (event, context, callback) => {
      callback(null, {
          statusCode: 200,
          headers: { "x-custom-header" : "my custom header value" },
          body: "hello world"
      });
  }
  ```

  If your Lambda function does not return a valid [ Proxy Integration](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html) response, you will receive an `HTTP 500 (Internal Server Error)` response when accessing your function\. SAM CLI will also print the following error log message to help you diagnose the problem:

  ```
  ERROR: Function ExampleFunction returned an invalid response (must include one of: body, headers
                      or statusCode in the response object)
  ```
+ Validate that any runtime constraints, such as maximum memory use or timeout limits of your Lambda function invocations, are honored\.
+ Inspect AWS Lambda runtime logs, and also any customized logging output specified in your Lambda function code \(for example, `console.log`\)\. SAM CLI automatically displays this output\. The following shows an example\.

  ```
  START RequestId: 2137da9a-c79c-1d43-5716-406b4e6b5c0a Version: $LATEST
                  2017-05-18T13:18:57.852Z        2137da9a-c79c-1d43-5716-406b4e6b5c0a
                  Error: any error information
                  END RequestId: 2137da9a-c79c-1d43-5716-406b4e6b5c0a
                  REPORT RequestId: 2137da9a-c79c-1d43-5716-406b4e6b5c0a 
                  Duration: 12.78 ms      Billed Duration: 100 ms Memory Size: 128 MB
                  Max Memory Used: 29 MB
  ```
+ Honor security credentials that you've established by using the AWS CLI\. Doing so means your Lambda function can make remote calls to the AWS services that make up your serverless application\. If you have not installed the AWS CLI, see [Installing the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/)\.

  As with the AWS CLI and SDKs, SAM CLI looks for credentials in the following order:
  + Environment variables \(*AWS\_ACCESS\_KEY\_ID*, *AWS\_SECRET\_ACCESS\_KEY*\)
  + The AWS credentials file, located at `~/.aws/credentials` on Linux, MacOS, or Unix, or at `C:\Users\USERNAME \.aws\credentials` on Windows\)
  + Instance profile credentials, if running on an Amazon EC2 instance with an assigned instance role

## Supported Runtimes<a name="test-sam-cli-supported-runtimes"></a>

SAM CLI supports the following AWS runtimes:
+ node\.js 4\.3
+ node\.js 6\.10
+ node\.js 8\.10
+ python 2\.7
+ python 3\.6
+ java8
+ go 1\.x

If you have not already installed SAM CLI, see [Install SAM CLI](sam-cli-requirements.md)\.

## Getting Started Using SAM CLI<a name="sam-cli-what-is"></a>

SAM CLI consists of the following CLI operations:
+ **start\-api**: Creates a local HTTP server hosting all of your Lambda functions\. When accessed by using a browser or the CLI, this operation launches a Docker container locally to invoke your function\. It reads the `CodeUri` property of the `AWS::Serverless::Function` resource to find the path in your file system containing the Lambda function code\. This path can be the project's root directory for interpreted languages like Node\.js or Python, a build directory that stores your compiled artifacts, or for Java, a `.jar` file\.

  If you use an interpreted language, local changes are made available within the same Docker container\. This approach means you can reinvoke your Lambda function with no need for redeployment\. For compiled languages or projects requiring complex packing support, we recommend that you run your own build solution and point AWS SAM to the directory that contains the build dependency files needed\. 
+ **invoke**: Invokes a local Lambda function once and terminates after invocation completes\.

  ```
  # Invoking function with event file
  $ sam local invoke "Ratings" -e event.json
  
  # Invoking function with event via stdin
  $ echo '{"message": "Hey, are you there?" }' | sam local invoke "Ratings"
  
  # For more options
  $ sam local invoke --help
  ```
+ **generate\-event**: Generates mock serverless events\. Using these, you can develop and test locally on functions that respond to asynchronous events such as those from Amazon S3, Kinesis, and DynamoDB\. The following displays the command options available to the `generate-event` operation\.

  ```
  sam local generate-event
  NAME:
     sam local generate-event - Generates Lambda events (e.g. for S3/Kinesis etc) that can be piped to 'sam local invoke'
  
  USAGE:
     sam local generate-event command [command options] [arguments...]
  
  COMMANDS:
       s3        Generates a sample Amazon S3 event
       sns       Generates a sample Amazon SNS event
       kinesis   Generates a sample Amazon Kinesis event
       dynamodb  Generates a sample Amazon DynamoDB event
       api       Generates a sample Amazon API Gateway event
       schedule  Generates a sample scheduled event
  
  OPTIONS:
     --help, -h  show help
  ```
+ **validate:** Validates your template against the official [AWS Serverless Application Model specification](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md)\. The following is an example\. 

  ```
  $ sam validate
  ERROR: Resource "HelloWorld", property "Runtime": Invalid value node. 
  Valid values are "nodejs4.3", "nodejs6.10", "nodejs8.10", "java8", "python2.7",
  "python3.6"(line: 11; col: 6)
  
  # Let's fix that error...
  $ sed -i 's/node/nodejs6.10/g' template.yaml
  
  $ sam validate
  Valid!
  ```
+ **package** and **deploy**: `sam package` and `sam deploy` implicitly call AWS CloudFormation's [package](http://docs.aws.amazon.com/cli/latest/reference/cloudformation/package.html) and [deploy](http://docs.aws.amazon.com/cli/latest/reference/cloudformation/deploy.html) commands\. For more information on packaging and deployment of SAM applications, see [Packaging and Deployment](serverless-deploy-wt.md#serverless-deploy)\.

  The following demonstrates how to use the `package` and `deploy` commands in SAM CLI\.

  ```
  # Package SAM template
  $ sam package --template-file sam.yaml --s3-bucket mybucket --output-template-file packaged.yaml
  
  # Deploy packaged SAM template
  $ sam deploy --template-file ./packaged.yaml --stack-name mystack --capabilities CAPABILITY_IAM
  ```

### Building a Simple Application Using SAM CLI<a name="sam-cli-simple-app"></a>

Suppose you want to build a simple RESTful API operation that creates, reads, updates, and deletes a list of products\. You begin by creating the following directory structure:

*dir*/products\.js

*dir*/template\.yaml

The *template\.yaml* file is the AWS SAM template that describes a single Lambda function that handles all the API requests\. 

**Note**  
By default, the `start-api` and `invoke` commands search your working directory for the *template\.yaml* file\. If you reference a *template\.yaml* file that is in a different directory, add the `-t` or `--template` parameter to these operations and pass an absolute or relative path to that file\.

 Copy and paste the following into the *template\.yaml* file\.

```
AWSTemplateFormatVersion : '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Description: My first serverless application.

Resources:

  Products:
    Type: AWS::Serverless::Function
    Properties:
      Handler: products.handler
      Runtime: nodejs6.10
      Events:
        ListProducts:
          Type: Api
          Properties:
            Path: /products
            Method: get
        CreateProduct:
          Type: Api
          Properties:
            Path: /products
            Method: post
        Product:
          Type: Api
          Properties:
            Path: /products/{product}
            Method: any
```

The preceding example configures the following RESTful API endpoints:
+ Create a new product with a `PUT` request to /products\.
+ List all products with a `GET` request to /products\.
+ Read, update, or delete a product with `GET`, `PUT` or `DELETE` request to /products/\{product\}\.

Next, copy and paste the following code into the *products\.js* file\.

```
'use strict';

exports.handler = (event, context, callback) => {

    let id =  (event.pathParameters || {}).product || false;
    switch(event.httpMethod){

        case "GET":

            if(id) {
                callback(null, {body: "This is a READ operation on product ID " + id});
                return;  
            } 

            callback(null, {body: "This is a LIST operation, return all products"});
            break;

        case "POST":            
            callback(null, {body: "This is a CREATE operation"}); 
            break;

        case "PUT": 
            callback(null, {body: "This is an UPDATE operation on product ID " + id});
            break;

        case "DELETE": 
            callback(null, {body:"This is a DELETE operation on product ID " + id});
            break;

        default:
            // Send HTTP 501: Not Implemented
            console.log("Error: unsupported HTTP method (" + event.httpMethod + ")");
            callback(null, { statusCode: 501 })

    }

}
```

Start a local copy of your API operations by calling the `start-api` command\.

```
$ sam local start-api

2017/05/18 14:03:01 Successfully parsed template.yaml (AWS::Serverless-2016-10-31)
2017/05/18 14:03:01 Found 1 AWS::Serverless::Function
2017/05/18 14:03:01 Mounting products.handler (nodejs6.10) at /products [POST]
2017/05/18 14:03:01 Mounting products.handler (nodejs6.10) at /products/{product} [OPTIONS GET HEAD POST PUT DELETE TRACE CONNECT]
2017/05/18 14:03:01 Mounting products.handler (nodejs6.10) at /products [GET]
2017/05/18 14:03:01 Listening on http://localhost:3000

You can now browse to the above endpoints to invoke your functions.
You do not need to restart/reload while working on your functions,
changes will be reflected instantly/automatically. You only need to restart
if you update your AWS SAM template.
```

You can then test your API endpoint locally using either a browser or the CLI\.

```
$ curl http://localhost:3000/products 
"This is a LIST operation, return all products"

$ curl -XDELETE http://localhost:3000/products/1
"This is a DELETE operation on product ID 1"
```

#### Local Logging<a name="sam-cli-local-logging"></a>

Using the `invoke` and `start-api` commands, you can pipe logs from your Lambda function's invocation into a file\. This approach is useful if you run automated tests against SAM CLI and want to capture logs for analysis\. The following is an example\.

```
$ sam local invoke --log-file ./output.log
```

#### Using an Environment Variables File<a name="sam-cli-env_vars_file"></a>

If your Lambda function uses [Environment Variables](env_variables.md), SAM CLI provides an `--env-vars` argument for both the `invoke` and `start-api` commands\. With this argument, you can use a JSON file that contains values for environment variables defined in your function\. The JSON file's structure should be similar to the following\.

```
{
  "MyFunction1": {
    "TABLE_NAME": "localtable",
    "BUCKET_NAME": "testBucket"
  },
  "MyFunction2": {
    "TABLE_NAME": "localtable",
    "STAGE": "dev"
  },
}
```

You then access the JSON file using the following command:

```
$ sam local start-api --env-vars env.json
```

#### Using a Shell Environment<a name="sam-cli-shell"></a>

Variables defined in your shell environment are passed to the Docker container if they map to a variable in your Lambda function\. Shell variables are globally accessible to functions\. For example, suppose you have two functions, *MyFunction1* and *MyFunction2*, which have a variable called *TABLE\_NAME\.* In this case, the value for *TABLE\_NAME* provided through your shell's environment is available to both functions\.

The following command sets the value of *TABLE\_NAME* to *myTable* for both functions\.

```
$ TABLE_NAME=mytable sam local start-api
```

**Note**  
For greater flexibility, you can use a combination of shell variables and an external JSON file that holds environment variables\. If a variable is defined in both places, the one from the external file overrides the shell version\. Following is the order of priority, highest to lowest:   
Environment variable file
Shell environment
Hard\-coded values contained in the SAM template

### Debugging With SAM CLI<a name="sam-cli-debugging"></a>

Both `sam local invoke` and `sam local start-api` support local debugging of your functions\. To run SAM CLI with debugging support enabled, specify `--debug-port` or `-d` on the command line\.

```
# Invoke a function locally in debug mode on port 5858 
$ sam local invoke -d 5858 function logical id

# Start local API Gateway in debug mode on port 5858
$ sam local start-api -d 5858
```

**Note**  
If you use `sam local start-api`, the local API Gateway exposes all of your Lambda functions\. But because you can specify only one debug port, you can only debug one function at a time\.

For compiled languages or projects requiring complex packaging support, we recommend that you run your own build solution and point AWS SAM to the directory that contains the build dependency files needed\. You can use one of the following IDEs or another of your choosing: 
+ [AWS Cloud9](https://docs.aws.amazon.com/cloud9/latest/user-guide)
+ Eclipse
+ Visual Studio Code

#### Debugging Functions Written in Python<a name="sam-cli-debugging-python"></a>

Unlike Node\.js, \.NET or Java, Python requires you to enable remote debugging in your Lambda function code\. If you enable debugging \(using the `--debug-port` or `-d` options mentioned above\) for a function that uses one of the Python runtimes \(2\.7 or 3\.6\), SAM CLI maps through that port from your host machine to the Lambda container\. To enable remote debugging, use a Python package such as [remote\-pdb](https://pypi.python.org/pypi/remote-pdb)\. 

**Important**  
When configuring the host, the debugger listens in on your code, so make sure to use `0.0.0.0` and not `127.0.0.1`\.