# Error Processing with CloudWatch Logs and X-Ray
This sample application triggers a Lambda function when CloudWatch Logs detects the word ERROR in a log stream. The function downloads the full log stream and the X-Ray trace for the request that caused the error. It stores both in an Amazon S3 bucket.

This project contains the following resources:
- processor - A Node.js function that retrieves logs and traces, and stores them in Amazon S3.
- random-error - A Node.js function that generates errors at random.
- template.yml - An AWS CloudFormation template that creates an application with the processor and random-error functions.
- 1-createbucket.sh, 2-deploy.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

![Architecture](/sample-apps/error-processor/images/sample-errorprocessor.png)

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [Error Processor Sample Application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/sample-errorprocessor.html) in the developer guide.

# Requirements
- [Node.js 10 with npm](https://nodejs.org/en/download/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/error-processor

Run `1-create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, create a file named `bucket-name.txt` that contains the name of your bucket.

    error-processor$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

# Deploy
Run `2-deploy.sh` to install dependencies and deploy the application.

    error-processor$ ./2-deploy.sh
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

    error-processor$ ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    {
        "StatusCode": 200,
        "FunctionError": "Handled",
        "ExecutedVersion": "$LATEST"
    }
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the random error function generating errors for some requests. It also shows the processor function calling X-Ray, CloudWatch Logs, and Amazon S3.

![Service Map](/sample-apps/error-processor/images/errorprocessor-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/error-processor/images/errorprocessor-trace.png)

Finally, view the logs and traces that the function stores in Amazon S3.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **error-processor**.

  ![Application](/sample-apps/error-processor/images/errorprocessor-application.png)

3. Choose **bucket**.
4. Choose **errors**.
5. Choose a request ID.
6. For each file, check the box next to the filename and then choose **Download**.

# Cleanup
To delete the application, run the cleanup script.

    error-processor$ ./4-cleanup.sh
