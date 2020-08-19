# Error processing with CloudWatch Logs and X-Ray

This sample application triggers a Lambda function when CloudWatch Logs detects the word ERROR in a log stream. The function downloads the full log stream and the X-Ray trace for the request that caused the error. It stores both in an Amazon S3 bucket.

![Architecture](/sample-apps/error-processor/images/sample-errorprocessor.png)

The project source includes function code and supporting resources:

- `processor` - A Node.js function that retrieves logs and traces, and stores them in Amazon S3.
- `random-error` - A Node.js function that generates errors at random.
- `template.yml` - An AWS CloudFormation template that creates an application with the processor and random-error functions.
- `1-createbucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [Error Processor Sample Application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/sample-errorprocessor.html) in the developer guide.

# Requirements
- [Node.js 10 with npm](https://nodejs.org/en/download/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

If you use the AWS CLI v2, add the following to your [configuration file](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html) (`~/.aws/config`):

```
cli_binary_format=raw-in-base64-out
```

This setting enables the AWS CLI v2 to load JSON events from a file, matching the v1 behavior.

# Setup
Download or clone this repository.

    $ git clone https://github.com/awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/error-processor

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    error-processor$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

To build a Lambda layer that contains the function's runtime dependencies, run `2-build-layer.sh`. Packaging dependencies in a layer reduces the size of the deployment package that you upload when you modify your code.

    blank-ruby$ ./2-build-layer.sh

# Deploy
To deploy the application, run `3-deploy.sh`.

    error-processor$ ./3-deploy.sh
    added 16 packages from 18 contributors and audited 18 packages in 1.979s
    found 0 vulnerabilities
    added 17 packages from 19 contributors and audited 19 packages in 1.782s
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2737254 / 2737254.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - error-processor

This script uses AWS CloudFormation to deploy the Lambda functions, an IAM role, and a CloudWatch Logs subscription that triggers the processor function. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
To generate logs and errors, invoke the random error function.  

    error-processor$ ./4-invoke.sh
    {
        "StatusCode": 200,
        "FunctionError": "Unhandled",
        "ExecutedVersion": "$LATEST"
    }
    {"errorType":"function.DoublesRolledError","errorMessage":"Doubles rolled: 10 & 10","trace":["function.DoublesRolledError: Doubles rolled: 10 & 10","    at Runtime.myFunction [as handler] (/var/task/index.js:36:17)","    at Runtime.handleOnce (/var/runtime/Runtime.js:66:25)"]}
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    {"errorType":"function.DoublesRolledError","errorMessage":"Doubles rolled: 2 & 2","trace":["function.DoublesRolledError: Doubles rolled: 2 & 2","    at Runtime.myFunction [as handler] (/var/task/index.js:36:17)","    at Runtime.handleOnce (/var/runtime/Runtime.js:66:25)"],"depth":8}
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    {"errorType":"function.MaxDepthError","errorMessage":"Maximum depth reached: 9","trace":["function.MaxDepthError: Maximum depth reached: 9","    at Runtime.myFunction [as handler] (/var/task/index.js:30:17)","    at Runtime.handleOnce (/var/runtime/Runtime.js:66:25)"],"depth":9}

Let the script invoke the function a few times and then press `CRTL+C` to exit.

This function generates errors for a configurable percentage of invocations. When an error does not occur, it invokes itself recursively until one does. The event (`event.json`) provides a base error rate and maximum recursion depth.

    {
      "max-depth": 9,
      "current-depth": 0,
      "error-rate": 0.07
    }

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the random error function generating errors for some requests. It also shows the processor function calling X-Ray, CloudWatch Logs, and Amazon S3.

![Service Map](/sample-apps/error-processor/images/errorprocessor-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.  The following example shows a trace for the processor function, which is invoked by CloudWatch Logs for each error.

![Trace - Processor](/sample-apps/error-processor/images/errorprocessor-trace.png)

Traces for the random error function include all recursive calls. The following example shows a request that resulted in 3 invocations. The third invocation resulted in an error that was relayed back through the other two (successful) invocations to the requester.

![Trace - Random Error](/sample-apps/error-processor/images/errorprocessor-trace-randomerror.png)

Click on the error icon next to the function segment for the final invocation to see details about the exception.

![Trace - Exception](/sample-apps/error-processor/images/errorprocessor-exception.png)

Finally, view the logs and traces that the function stores in Amazon S3.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **error-processor**.

  ![Application](/sample-apps/error-processor/images/errorprocessor-application.png)

3. Choose **bucket**.
4. Choose **errors**.
5. Choose a request ID.
6. For each file, check the box next to the filename and then choose **Download**.

# Configure the random error function

With the default configuration, the function runs up to 9 times before returning an error. When invoked continuously with the AWS CLI, it maintains a concurrency of 9, with approximately 100 invocations per minute, including when the function invokes itself. The effective error rate is close to 14%.

To generate a larger, more varied set of data, lower the error rate and increase the maximum depth. The following example shows a service map for the application running with an error rate of 0.5% and a maximum depth of 500.

![Service Map](/sample-apps/error-processor/images/errorprocessor-servicemap-traffic.png)

Lambda records traces for the first request each second and 5% of additional requests. 

# Cleanup
To delete the application, run the cleanup script.

    error-processor$ ./5-cleanup.sh
