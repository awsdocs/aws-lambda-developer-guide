# Error Processing with CloudWatch Logs and X-Ray
This sample application triggers a Lambda function when CloudWatch Logs detects the word ERROR in a log stream. The function downloads the full log stream, and the X-Ray trace for the request that caused the error, and stores both in an Amazon S3 bucket.

This project contains the following resources-
- cloudwatch-logs - A Node.js function that retrieves logs and traces, and stores them in Amazon S3.
- random-error - A Node.js function that generates errors at random.
- error-processor.yaml - A CloudFormation template that creates an application with the above functions.
- install.sh, deploy.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

# Requirements
- NPM
- Bash
- AWS CLI

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/error-processor

Run `create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, replace `MY_BUCKET` in `deploy.sh` with the name of an existing bucket.

    error-processor$ ./create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

Run `install.sh` to install the dependencies for each function, including the X-Ray SDK.

    error-processor$ ./install.sh
    added 16 packages from 18 contributors and audited 18 packages in 0.926s
    found 0 vulnerabilities

    added 17 packages from 19 contributors and audited 19 packages in 0.916s
    found 0 vulnerabilities

# Deploy
Run `deploy.sh` to deploy the application.

    error-processor$ ./deploy.sh
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2737254 / 2737254.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yaml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - lambda-error-processor

This script uses AWS CloudFormation to deploy the Lambda functions, an IAM role, and a CloudWatch Logs subscription that triggers the processor function. If the CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
To generate logs and errors, invoke the random error function.

    error-processor$ ./invoke.sh
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

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/error-processor/images/errorprocessor-servicemap.png)

The map shows two graphs, one for the function that generates errors, and one for the main function, which CloudWatch Logs invokes when an error is logged. The main function uses the AWS SDK to download the full log and X-Ray trace for the request that generated the error, and stores both in the output bucket. The X-Ray SDK instruments the AWS SDK client to add nodes for the calls to X-Ray, CloudWatch Logs, and Amazon S3.

Choose a node in the main function graph and then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/error-processor/images/errorprocessor-trace.png)

Finally, view the logs and traces that the function stores in Amazon S3.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **lambda-error-processor**.

  ![Application](/sample-apps/error-processor/images/errorprocessor-application.png)

3. Choose **bucket**.
4. Choose **errors**.
5. Choose a request ID.
6. For each file, check the box next to the filename and then choose **Download**.

# Cleanup
To delete the application, run the cleanup script.

    error-processor$ ./cleanup.sh