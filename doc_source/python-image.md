# Deploy Python Lambda functions with container images<a name="python-image"></a>

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
| 3\.7 | Python 3\.7 | Amazon Linux 2 | [Dockerfile for Python 3\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.7/Dockerfile.python3.7) | 
| 3\.6 | Python 3\.6 | Amazon Linux 2018\.03 | [Dockerfile for Python 3\.6 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python3.6/Dockerfile.python3.6) | 
| 2, 2\.7 | Python 2\.7 | Amazon Linux 2018\.03 | [Dockerfile for Python 2\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/python2.7/Dockerfile.python2.7) | 

Docker Hub repository: amazon/aws\-lambda\-python

Amazon ECR repository: public\.ecr\.aws/lambda/python

## Python runtime interface clients<a name="python-image-clients"></a>

Install the runtime interface client for Python using the pip package manager:

```
pip install awslambdaric
```

For package details, see [Lambda RIC](https://pypi.org/project/awslambdaric) on the Python Package Index \(PyPI\) website\.

You can also download the [Python runtime interface client](https://github.com/aws/aws-lambda-python-runtime-interface-client/) from GitHub\.