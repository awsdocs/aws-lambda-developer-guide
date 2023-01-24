# Deploy transpiled TypeScript code in Lambda with container images<a name="typescript-image"></a>

You can deploy your TypeScript code to an AWS Lambda function as a Node\.js [container image](images-create.md)\. AWS provides [base images](nodejs-image.md#nodejs-image-base) for Node\.js to help you build the container image\. These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.

If you use a community or private enterprise base image, you must [add the Node\.js runtime interface client \(RIC\)](nodejs-image.md#nodejs-image-clients) to the base image to make it compatible with Lambda\. For more information, see [Creating images from alternative base images](images-create.md#images-create-from-alt)\.

Lambda provides a runtime interface emulator \(RIE\) for you to test your function locally\. The base images for Lambda and base images for custom runtimes include the RIE\. For other base images, you can download the RIE for [testing your image locally](images-test.md)\.

## Using a Node\.js base image to build and package TypeScript function code<a name="base-image-typescript"></a>

**Prerequisites**

To complete the steps in this section, you must have the following:
+ [AWS Command Line Interface \(AWS CLI\) version 2](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
+ Docker

  The following instructions use Docker CLI commands to create the container image\. To install Docker, see [Get Docker](https://docs.docker.com/get-docker) on the Docker website\.
+ Node\.js 14\.x or later

**To create an image from an AWS base image for Lambda**

1. On your local machine, create a project directory for your new function\.

1. Create a new Node\.js project with `npm` or a package manager of your choice\.

   ```
   npm init
   ```

1. Add the [@types/aws\-lambda](https://www.npmjs.com/package/@types/aws-lambda) and [esbuild](https://esbuild.github.io/) packages as development dependencies\.

   ```
   npm install -D @types/aws-lambda esbuild
   ```

1. Add a [build script](https://esbuild.github.io/getting-started/#build-scripts) to the **package\.json** file\.

   ```
     "scripts": {
     "build": "esbuild index.ts --bundle --minify --sourcemap --platform=node --target=es2020 --outfile=dist/index.js"
   }
   ```

1. Create a new file called **index\.ts**\. Add the following sample code to the new file\. This is the code for the Lambda function\. The function returns a `hello world` message\.

   ```
   import { Context, APIGatewayProxyResult, APIGatewayEvent } from 'aws-lambda';
   
   export const handler = async (event: APIGatewayEvent, context: Context): Promise<APIGatewayProxyResult> => {
       console.log(`Event: ${JSON.stringify(event, null, 2)}`);
       console.log(`Context: ${JSON.stringify(context, null, 2)}`);
       return {
           statusCode: 200,
           body: JSON.stringify({
               message: 'hello world',
           }),
       };
   };
   ```

1. Create a new Dockerfile with the following configuration:
   + Set the `FROM` property to the URI of the base image\.
   + Set the `CMD` argument to specify the Lambda function handler\.  
**Example Dockerfile**  

   The following Dockerfile uses a multi\-stage build\. The first step transpiles the TypeScript code into JavaScript\. The second step produces a container image that contains only JavaScript files and production dependencies\.

   ```
   FROM public.ecr.aws/lambda/nodejs:16 as builder
   WORKDIR /usr/app
   COPY package.json index.ts  ./
   RUN npm install
   RUN npm run build
       
   
   FROM public.ecr.aws/lambda/nodejs:16
   WORKDIR ${LAMBDA_TASK_ROOT}
   COPY --from=builder /usr/app/dist/* ./
   CMD ["index.handler"]
   ```

1. Build your image\.

   ```
   docker build -t hello-world .
   ```

1. Authenticate the Docker CLI to your Amazon Elastic Container Registry \(Amazon ECR\) registry\.

   ```
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com
   ```

1. Create a repository in Amazon ECR using the `create-repository` command\.

   ```
   aws ecr create-repository --repository-name hello-world --image-scanning-configuration scanOnPush=true --image-tag-mutability MUTABLE
   ```

1. Tag your image to match your repository name, and deploy the image to Amazon ECR using the `docker push` command\. 

   ```
   docker tag  hello-world:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

1. [Create and test](gettingstarted-images.md#configuration-images-create) the Lambda function\.

To update the function code, you must create a new image version and store the image in the Amazon ECR repository\. For more information, see [Updating function code](gettingstarted-images.md#configuration-images-update)\.