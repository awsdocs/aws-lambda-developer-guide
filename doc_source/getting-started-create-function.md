# Create a Lambda function with the console<a name="getting-started-create-function"></a>

In the following getting started exercises, you create Lambda functions using the console\.

In the first exercise, you create a function and use the default code that Lambda creates\. The Lambda console provides a [code editor](code-editor.md) for non\-compiled languages that lets you modify and test code quickly\.

In the next exercise, you create a function defined as a container image\. First, create a container image for your function code, and then use the Lambda console to create a function from the container image\.

**Topics**
+ [Create a Lambda function with default function code](#gettingstarted-zip-function)
+ [Create a function defined as a container image](#gettingstarted-images)

## Create a Lambda function with default function code<a name="gettingstarted-zip-function"></a>

In this getting started exercise, you create a Node\.js Lambda function using the Lambda console\. Lambda automatically creates default code for the function\. Next, you manually invoke the Lambda function using sample event data\. Lambda runs the function and returns results\. You then verify the results, including the logs that your Lambda function created and various Amazon CloudWatch metrics\.

**To create a Lambda function with the console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose **Create function**\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter **my\-function**\.

   1. For **Runtime**, confirm that **Node\.js 14\.x** is selected\. (Note that AWS Lambda natively supports Java, Go, PowerShell, Node.js, C#, Python, and Ruby code, and provides a Runtime API which allows you to use any additional programming languages to author your functions.)

1. Choose **Create function**\.

Lambda creates a Node\.js function and an [execution role](lambda-intro-execution-role.md) that grants the function permission to upload logs\. The Lambda function assumes the execution role when you invoke your function, and uses the execution role to create credentials for the AWS SDK and to read data from event sources\.

### Use the function overview<a name="get-started-designer"></a>

The **Function overview** shows a visualization of your function and its upstream and downstream resources\. You can use it to jump to trigger, destination, and layer configuration\.

![\[A Lambda function with an Amazon S3 trigger and an Amazon EventBridge destination.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-designer.png)

### Invoke the Lambda function<a name="get-started-invoke-manually"></a>

Invoke your Lambda function using the sample event data provided in the console\.

**To invoke a function**

1. In the console, open the file with the lambda function. For this Node\.js app, it's called index\.js.

1. Click on the orange **Test** button to create a new test event. Each user can create up to 10 test events per function\. Those test events are not available to other users\.

1. Enter an **Event name** and note the following sample event template:

   ```
   {
       "key1": "value1",
       "key2": "value2",
       "key3": "value3"
     }
   ```

1. Choose **Create**, and then choose **Test** again to run the function\. 

   Lambda runs your function on your behalf\. The function handler receives and then processes the sample event\.

1. Upon successful completion, view the results in the console\.
   + The **Execution results** tab shows the execution status as **succeeded**\. Here you will also see **Function Logs**\.

1. To make changes to your lambda function, edit the `index.js` file, and choose **Deploy**\. This will store those changes in S3. Then, choose **Test** again to run the function.
  

1. Choose the **Monitor** tab\. This page shows graphs for the metrics that Lambda sends to CloudWatch\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring functions in the AWS Lambda console](monitoring-functions-access-metrics.md)\.

### Clean up<a name="gettingstarted-cleanup"></a>

If you are done working with the example function, delete it\. You can also delete the log group that stores the function's logs, and the execution role that the console created\.

**To delete a Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Actions**, **Delete**\.

1. In the **Delete function** dialog box, choose **Delete**\.

**To delete the log group**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home#logs:) of the CloudWatch console\.

1. Select the function's log group \(`/aws/lambda/my-function`\)\.

1. Choose **Actions**, **Delete log group\(s\)**\.

1. In the **Delete log group\(s\)** dialog box, choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home?#/roles) of the AWS Identity and Access Management \(IAM\) console\.

1. Select the function's role \(`my-function-role-31exxmpl`\)\.

1. Choose **Delete role**\.

1. In the **Delete role** dialog box, choose **Yes, delete**\.

You can automate the creation and cleanup of functions, log groups, and roles with AWS CloudFormation and the AWS Command Line Interface \(AWS CLI\)\. For fully functional sample applications, see [Lambda sample applications](lambda-samples.md)\.

## Create a function defined as a container image<a name="gettingstarted-images"></a>

In this getting started exercise, you use the Docker CLI to create a container image and then use the Lambda console to create a function from the container image\.

**Topics**
+ [Prerequisites](#gettingstarted-images-prereq)
+ [Create the container image](#gettingstarted-images-package)
+ [Upload the image to the Amazon ECR repository](#gettingstarted-create-upload)
+ [Update the user permissions](#gettingstarted-images-permissions)
+ [Create a Lambda function defined as a container image](#gettingstarted-images-function)
+ [Invoke the Lambda function](#get-started-invoke-function)
+ [Clean up](#gettingstarted-image-cleanup)

### Prerequisites<a name="gettingstarted-images-prereq"></a>

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
this is a command
```

You should see the following output:

```
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

This exercise uses Docker CLI commands to create the container image\. To install the Docker CLI, see [Get Docker](https://docs.docker.com/get-docker) on the Docker Docs website\.

### Create the container image<a name="gettingstarted-images-package"></a>

AWS provides a set of base images in the Amazon Elastic Container Registry \(Amazon ECR\)\. In this getting started exercise, we use the Node\.js base image to create a container image\. For more information about base images, see [ AWS base images for Lambda](runtimes-images.md#runtimes-images-lp)\.

In the following commands, replace `123456789012` with your AWS account ID\.

**To create an image using the AWS Node\.js 12 base image**

1. On your local machine, create a project directory for your new function\.

1. Create a file named `app.js` in your project directory\. Add the following code to `app.js`:

   ```
   exports.handler = async (event) => {
       // TODO implement
       const response = {
           statusCode: 200,
           body: JSON.stringify('Hello from Lambda!'),
       };
       return response;
   };
   ```

1. Use a text editor to create a new file named `Dockerfile` in your project directory\. Add the following content to `Dockerfile`:

   ```
   FROM public.ecr.aws/lambda/nodejs:12
   
   # Copy function code and package.json
   COPY app.js package.json /var/task/
   
   # Install NPM dependencies for function
   RUN npm install
   
   # Set the CMD to your handler
   CMD [ "app.handler" ]
   ```

1. Create the `package.json` file\. From your project directory, run the `npm init` command\. Accept all of the default values:

   ```
   npm init
   ```

1. Build your Docker image\. From your project directory, run the following command:

   ```
   docker build -t hello-world .
   ```

1. \(Optional\) AWS base images include the Lambda runtime interface emulator, so you can test your function locally\. 

   1. Run your Docker image\. From your project directory, run the `docker run` command:

      ```
      docker run -p 9000:8080 hello-world:latest
      ```

   1. Test your Lambda function\. In a new terminal window, run a `curl` command to invoke your function:

      ```
      curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
      ```

### Upload the image to the Amazon ECR repository<a name="gettingstarted-create-upload"></a>

1. Authenticate the Docker CLI to your Amazon ECR registry\.

   ```
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com
   ```

1. Create a repository in Amazon ECR using the `create-repository` command\.

   ```
   aws ecr create-repository --repository-name hello-world --image-scanning-configuration scanOnPush=true --image-tag-mutability MUTABLE
   ```

1. Tag your image to match your repository name using the `docker tag` command\. 

   ```
   docker tag  hello-world:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

1. Deploy the image to Amazon ECR using the `docker push` command\. 

   ```
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

### Update the user permissions<a name="gettingstarted-images-permissions"></a>

Make sure that the permissions for the IAM user or role that creates the function contain the AWS managed policies `GetRepositoryPolicy` and `SetRepositoryPolicy`\. For information about access policies, see [ Access Management](https://docs.aws.amazon.com/IAM/latest/UserGuide/access.html) in the *IAM User Guide*

For example, use the IAM console to create a role with the following policy:

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
    "Sid": "VisualEditor0",
    "Effect": "Allow",
    "Action": ["ecr:SetRepositoryPolicy","ecr:GetRepositoryPolicy"],
    "Resource": "arn:aws:ecr:<region>:<account>:repository/<repo name>/"
    }
  ]
}
```

### Create a Lambda function defined as a container image<a name="gettingstarted-images-function"></a>

Use the Lambda console to create a function defined as a container image\.

**To create the function with the console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose **Create function**\.

1. Choose the **Container image** option\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter **my\-function**\.

   1. For **Container image URI**, enter the URI of the Amazon ECR image that you created previously\.

1. Choose **Create function**\.

Lambda creates your function and an [execution role](lambda-intro-execution-role.md) that grants the function permission to upload logs\. Lambda assumes the execution role when you invoke your function, and uses the execution role to create credentials for the AWS SDK and to read data from event sources\.

### Invoke the Lambda function<a name="get-started-invoke-function"></a>

Invoke your Lambda function using the sample event data provided in the console\.

**To invoke a function**

1. After selecting your function, choose the **Test** tab\.

1. In the **Test event** section, choose **New event**\. In **Template**, leave the default **hello\-world** option\. Enter an **Name** and note the following sample event template:

   ```
   {
       "key1": "value1",
       "key2": "value2",
       "key3": "value3"
     }
   ```

1. Choose **Create event**, and then choose **Invoke**\. Each user can create up to 10 test events per function\. Those test events are not available to other users\.

   Lambda runs your function on your behalf\. The function handler receives and then processes the sample event\.

1. Upon successful completion, view the results in the console\.
   + The **Execution result** shows the execution status as **succeeded**\. To view the function execution results, expand **Details**\. Note that the **logs** link opens the **Log groups** page in the CloudWatch console\.
   + The **Summary** section shows the key information reported in the **Log output** section \(the *REPORT* line in the execution log\)\.
   + The **Log output** section shows the log that Lambda generates for each invocation\. The function writes these logs to CloudWatch\. The Lambda console shows these logs for your convenience\. Choose **Click here** to add logs to the CloudWatch log group and open the **Log groups** page in the CloudWatch console\.

1. Run the function \(choose **Invoke**\) a few more times to gather some metrics that you can view in the next step\.

1. Near the top of the page, choose the **Monitoring** tab\. This page shows graphs for the metrics that Lambda sends to CloudWatch\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring functions in the AWS Lambda console](monitoring-functions-access-metrics.md)\.

### Clean up<a name="gettingstarted-image-cleanup"></a>

If you are finished with the container image, see [Deleting an image](https://docs.aws.amazon.com/AmazonECR/latest/userguide/delete_image.html) in the *Amazon Elastic Container Registry User Guide*

If you are done working with your function, delete it\. You can also delete the log group that stores the function's logs and the execution role that the console created\.

**To delete a Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Actions**, **Delete**\.

1. In the **Delete function** dialog box, choose **Delete**\.

**To delete the log group**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home#logs:) of the CloudWatch console\.

1. Select the function's log group \(`/aws/lambda/my-function`\)\.

1. Choose **Actions**, **Delete log group\(s\)**\.

1. In the **Delete log group\(s\)** dialog box, choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home?#/roles) of the IAM console\.

1. Select the function's role \(`my-function-role-31exxmpl`\)\.

1. Choose **Delete role**\.

1. In the **Delete role** dialog box, choose **Yes, delete**\.

You can automate the creation and cleanup of functions, log groups, and roles with AWS CloudFormation and the AWS CLI\. For fully functional sample applications, see [Lambda sample applications](lambda-samples.md)\.