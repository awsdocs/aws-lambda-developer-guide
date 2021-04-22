# Deploy Ruby Lambda functions with container images<a name="ruby-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Ruby function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

## AWS base images for Ruby<a name="ruby-image-base"></a>

AWS provides the following base images for Ruby:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
| 2, 2\.7 | Ruby 2\.7 | Amazon Linux 2 | [Dockerfile for Ruby 2\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/ruby2.7/Dockerfile.ruby2.7) | 
| 2\.5 | Ruby 2\.5 | Amazon Linux 2018\.03 | [Dockerfile for Ruby 2\.5 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/ruby2.5/Dockerfile.ruby2.5) | 

Docker Hub repository: amazon/aws\-lambda\-ruby

Amazon ECR repository: gallery\.ecr\.aws/lambda/ruby

## Using a Ruby base image<a name="ruby-image-instructions"></a>

For instructions on how to use a Ruby base image, choose the **usage** tab on [AWS Lambda base images for Ruby](https://gallery.ecr.aws/lambda/ruby) in the *Amazon ECR repository*\. 

The instructions are also available on [AWS Lambda base images for Ruby](https://hub.docker.com/r/amazon/aws-lambda-ruby) in the *Docker Hub repository*\.

## Ruby runtime interface clients<a name="ruby-image-clients"></a>

Install the runtime interface client for Ruby using the RubyGems\.org package manager:

```
gem install aws_lambda_ric
```

For package details, see [Lambda RIC](https://rubygems.org/gems/aws_lambda_ric) on [RubyGems\.org](https://rubygems.org/pages/about)\.

You can also download the [Ruby runtime interface client](https://github.com/aws/aws-lambda-ruby-runtime-interface-client) from GitHub\.