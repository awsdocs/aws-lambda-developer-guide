# Deploy Python Lambda functions with container images<a name="python-image"></a>

**Note**  
End of support for the Python 2\.7 runtime starts on July 15, 2021\. For more information, see [Runtime support policy](runtime-support-policy.md)\.

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Python function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

## AWS base images for Python<a name="python-image-base"></a>

AWS provides the following base images for Python:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
| 3, 3\.8 | Python 3\.8 | Amazon Linux 2 | [Dockerfile for Python 3\.8 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.8/Dockerfile.python3.8) | 
| 3\.7 | Python 3\.7 | Amazon Linux 2018\.03 | [Dockerfile for Python 3\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.7/Dockerfile.python3.7) | 
| 3\.6 | Python 3\.6 | Amazon Linux 2018\.03 | [Dockerfile for Python 3\.6 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.6/Dockerfile.python3.6) | 
| 2, 2\.7 | Python 2\.7 | Amazon Linux 2018\.03 | [Dockerfile for Python 2\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python2.7/Dockerfile.python2.7) | 

Docker Hub repository: amazon/aws\-lambda\-python

Amazon ECR repository: gallery\.ecr\.aws/lambda/python

## Python runtime interface clients<a name="python-image-clients"></a>

Install the runtime interface client for Python using the pip package manager:

```
pip install awslambdaric
```

For package details, see [Lambda RIC](https://pypi.org/project/awslambdaric) on the Python Package Index \(PyPI\) website\.

You can also download the [Python runtime interface client](https://github.com/aws/aws-lambda-python-runtime-interface-client/) from GitHub\.

## Deploying Python with an AWS base image<a name="python-image-create"></a>

When you build a container image for Python using an AWS base image, you only need to copy the python app to the container and install any dependencies\. 

**To build and deploy a Python function with the `python:3.8` base image\.**

1. On your local machine, create a project directory for your new function\.

1. In your project directory, add a file named `app.py` containing your function code\. The following example shows a simple Python handler\. 

   ```
   import sys
   def handler(event, context):
       return 'Hello from AWS Lambda using Python' + sys.version + '!'
   ```

1. Use a text editor to create a Dockerfile in your project directory\. The following example shows the Dockerfile for the handler that you created in the previous step\.

   ```
   FROM public.ecr.aws/lambda/python:3.8
   
   COPY app.py   ./
   CMD ["app.handler"]
   ```

1. To create the container image, follow steps 4 through 7 in [Create an image from an AWS base image for Lambda](images-create.md#images-create-1.title)\.

## Create a Python image from an alternative base image<a name="python-image-create2"></a>

For an example of how to create a Python image from an Alpine base image, see [Container image support for Lambda](http://aws.amazon.com/blogs/aws/new-for-aws-lambda-container-image-support/) on the AWS Blog\.