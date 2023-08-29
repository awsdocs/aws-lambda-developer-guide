# Blank function with layer (C#)

![Architecture](/sample-apps/blank-csharp/images/sample-blank-csharp.png)

The project source includes function code and supporting resources:

- `src/blank-csharp` - A C# .NET Core function.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [Managing Spot Instance Requests](https://docs.aws.amazon.com/lambda/latest/dg/services-ec2-tutorial.html) in the developer guide.

# Requirements
- [.NET Core SDK 6.0](https://dotnet.microsoft.com/download/dotnet-core/6.0)
- [AWS extensions for .NET CLI](https://github.com/aws/aws-extensions-for-dotnet-cli). Specifically, ensure that you have [Amazon.Lambda.Tools](https://github.com/aws/aws-extensions-for-dotnet-cli#aws-lambda-amazonlambdatools) installed.
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
    $ cd aws-lambda-developer-guide/sample-apps/blank-csharp

To create a new bucket for deployment artifacts, run `1-create-bucket-and-role.sh`.

    blank-csharp$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-d7aec9f2022ef2b4
    make_bucket: lambda-artifacts-d7aec9f2022ef2b4-dotnet-layer
    {
        "Role": {
            "Path": "/",
            "RoleName": "blank-csharp-role",
            "RoleId": "AROA6HOIFXAKKWARP5RSC",
            "Arn": "arn:aws:iam::978061735956:role/blank-csharp-role",
            "CreateDate": "2023-08-22T18:12:29+00:00",
            "AssumeRolePolicyDocument": {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Allow",
                        "Action": [
                            "sts:AssumeRole"
                        ],
                        "Principal": {
                            "Service": [
                                "lambda.amazonaws.com"
                            ]
                        }
                    }
                ]
            }
        }
    }

To build a Lambda layer that contains the function's runtime dependencies, run `2-build-layer.sh`. This also uploads the layer to an S3 bucket created by the first script.

    blank-csharp$ ./2-build-layer.sh

# Deploy
To deploy the application, run `3-deploy.sh`.

    blank-csharp$ ./3-deploy.sh
    Amazon Lambda Tools for .NET Core applications (5.8.0)
    ...
    Created publish archive ...
    Creating new Lambda function blank-csharp
    New Lambda function created

This script uses the .NET Amazon Lambda Tools to deploy the Lambda function. It uses the default settings from the `src/aws-lambda-tools-defaults.json` file.

To invoke the function, run `4-invoke.sh`.

    blank-csharp$ ./4-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    {"FunctionCount":13,"TotalCodeSize":598094248}

Let the script invoke the function a few times and then press `CRTL+C` to exit.

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the function managing spot instances in Amazon EC2.

![Service Map](/sample-apps/blank-csharp-with-layer/images/blank-csharp-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/blank-csharp-with-layer/images/blank-csharp-trace.png)

# Cleanup
To delete the application, run the cleanup script.

    blank-csharp$ ./5-cleanup.sh
