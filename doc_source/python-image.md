# Deploy Python Lambda functions with container images<a name="python-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Python function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients \(RIC\)

  If you use a community or private enterprise base image, you must add a [Runtime interface client](runtimes-images.md#runtimes-api-client) to the base image to make it compatible with Lambda\.
+ Open\-source runtime interface emulator \(RIE\)

   Lambda provides a runtime interface emulator for you to test your function locally\. The base images for Lambda and base images for custom runtimes include the RIE\. For other base images, you can download the RIE for [testing your image](images-test.md) locally\.

The workflow for a function defined as a container image includes these steps:

1. Build your container image using the resources listed in this topic\.

1. Upload the image to your [Amazon ECR container registry](images-create.md#images-upload)\.

1. [Create](gettingstarted-images.md#configuration-images-create) the Lambda function or [update the function code](gettingstarted-images.md#configuration-images-update) to deploy the image to an existing function\.

**Topics**
+ [AWS base images for Python](#python-image-base)
+ [Create a Python image from an AWS base image](#python-image-create)
+ [Create a Python image from an alternative base image](#python-image-create-alt)
+ [Python runtime interface clients](#python-image-clients)
+ [Deploy the container image](#python-image-deploy)

## AWS base images for Python<a name="python-image-base"></a>

AWS provides the following base images for Python:


| Tags | Runtime | Operating system | Dockerfile | Deprecation | 
| --- | --- | --- | --- | --- | 
| 3\.9 | Python 3\.9 | Amazon Linux 2 | [Dockerfile for Python 3\.9 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.9/Dockerfile.python3.9) |    | 
| 3\.8 | Python 3\.8 | Amazon Linux 2 | [Dockerfile for Python 3\.8 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.8/Dockerfile.python3.8) |    | 
| 3\.7 | Python 3\.7 | Amazon Linux | [Dockerfile for Python 3\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.7/Dockerfile.python3.7) |    | 

Amazon ECR repository: [gallery\.ecr\.aws/lambda/python](https://gallery.ecr.aws/lambda/python)

## Create a Python image from an AWS base image<a name="python-image-create"></a>

When you build a container image for Python using an AWS base image, you only need to copy the python app to the container and install any dependencies\.

If your function has dependencies, your local Python environment must match the version in the base image that you specify in the Dockerfile\.

**To build and deploy a Python function with the `python:3.8` base image\.**

1. On your local machine, create a project directory for your new function\.

1. In your project directory, add a file named `app.py` containing your function code\. The following example shows a simple Python handler\. 

   ```
   import sys
   def handler(event, context):
       return 'Hello from AWS Lambda using Python' + sys.version + '!'
   ```

1. In your project directory, add a file named `requirements.txt`\. List each required library as a separate line in this file\. Leave the file empty if there are no dependencies\.

1. Use a text editor to create a Dockerfile in your project directory\. The following example shows the Dockerfile for the handler that you created in the previous step\. Install any dependencies under the $\{LAMBDA\_TASK\_ROOT\} directory alongside the function handler to ensure that the Lambda runtime can locate them when the function is invoked\.

   ```
   FROM public.ecr.aws/lambda/python:3.8
   
   # Install the function's dependencies using file requirements.txt
   # from your project folder.
   
   COPY requirements.txt  .
   RUN  pip3 install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"
   
   # Copy function code
   COPY app.py ${LAMBDA_TASK_ROOT}
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.handler" ]
   ```

1. To create the container image, follow steps 4 through 7 in [Create an image from an AWS base image for Lambda](images-create.md#images-create-from-base)\.

## Create a Python image from an alternative base image<a name="python-image-create-alt"></a>

When you use an alternative base image, you need to install the [Python runtime interface client](#python-image-clients)

For an example of how to create a Python image from an Alpine base image, see [Container image support for Lambda](http://aws.amazon.com/blogs/aws/new-for-aws-lambda-container-image-support/) on the AWS Blog\.

## Python runtime interface clients<a name="python-image-clients"></a>

Install the [runtime interface client](runtimes-images.md#runtimes-api-client) for Python using the pip package manager:

```
pip install awslambdaric
```

For package details, see [Lambda RIC](https://pypi.org/project/awslambdaric) on the Python Package Index \(PyPI\) website\.

You can also download the [Python runtime interface client](https://github.com/aws/aws-lambda-python-runtime-interface-client/) from GitHub\.

## Deploy the container image<a name="python-image-deploy"></a>

For a new function, you deploy the Python image when you [create the function](gettingstarted-images.md#configuration-images-create)\. For an existing function, if you rebuild the container image, you need to redeploy the image by [updating the function code](gettingstarted-images.md#configuration-images-update)\.