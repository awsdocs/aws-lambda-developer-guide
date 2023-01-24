# Deploy Ruby Lambda functions with container images<a name="ruby-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Ruby function:
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
+ [AWS base images for Ruby](#ruby-image-base)
+ [Using a Ruby base image](#ruby-image-instructions)
+ [Ruby runtime interface clients](#ruby-image-clients)
+ [Create a Ruby image from an AWS base image](#ruby-image-create)
+ [Deploy the container image](#ruby-image-deploy)

## AWS base images for Ruby<a name="ruby-image-base"></a>

AWS provides the following base images for Ruby:


| Tags | Runtime | Operating system | Dockerfile | Deprecation | 
| --- | --- | --- | --- | --- | 
| 2\.7 | Ruby 2\.7 | Amazon Linux 2 | [Dockerfile for Ruby 2\.7 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/ruby2.7/Dockerfile.ruby2.7) |    | 

Amazon ECR repository: [gallery\.ecr\.aws/lambda/ruby](https://gallery.ecr.aws/lambda/ruby)

## Using a Ruby base image<a name="ruby-image-instructions"></a>

For instructions on how to use a Ruby base image, choose the **usage** tab on [AWS Lambda base images for Ruby](https://gallery.ecr.aws/lambda/ruby) in the *Amazon ECR repository*\.

## Ruby runtime interface clients<a name="ruby-image-clients"></a>

Install the runtime interface client for Ruby using the RubyGems\.org package manager:

```
gem install aws_lambda_ric
```

For package details, see [Lambda RIC](https://rubygems.org/gems/aws_lambda_ric) on [RubyGems\.org](https://rubygems.org/pages/about)\.

You can also download the [Ruby runtime interface client](https://github.com/aws/aws-lambda-ruby-runtime-interface-client) from GitHub\.

## Create a Ruby image from an AWS base image<a name="ruby-image-create"></a>

When you build a container image for Ruby using an AWS base image, you only need to copy the ruby app to the container and install any dependencies\. 

**To build and deploy a Ruby function with the `ruby:2.7` base image\.**

1. On your local machine, create a project directory for your new function\.

1. In your project directory, add a file named `app.rb` containing your function code\. The following example shows a simple Ruby handler\. 

   ```
   module LambdaFunction
     class Handler
       def self.process(event:,context:)
         "Hello from Ruby 2.7 container image!"
       end
     end
   end
   ```

1. Use a text editor to create a Dockerfile in your project directory\. The following example shows the Dockerfile for the handler that you created in the previous step\. Install any dependencies under the $\{LAMBDA\_TASK\_ROOT\} directory alongside the function handler to ensure that the Lambda runtime can locate them when the function is invoked\.

   ```
   FROM public.ecr.aws/lambda/ruby:2.7
   
   # Copy function code
   COPY app.rb ${LAMBDA_TASK_ROOT}
   
   # Copy dependency management file
   COPY Gemfile ${LAMBDA_TASK_ROOT}
   
   # Install dependencies under LAMBDA_TASK_ROOT
   ENV GEM_HOME=${LAMBDA_TASK_ROOT}
   RUN bundle install
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.LambdaFunction::Handler.process" ]
   ```

1. To create the container image, follow steps 4 through 7 in [Create an image from an AWS base image for Lambda](images-create.md#images-create-from-base)\.

## Deploy the container image<a name="ruby-image-deploy"></a>

For a new function, you deploy the Ruby image when you [create the function](gettingstarted-images.md#configuration-images-create)\. For an existing function, if you rebuild the container image, you need to redeploy the image by [updating the function code](gettingstarted-images.md#configuration-images-update)\.