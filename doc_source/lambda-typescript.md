# Building Lambda functions with TypeScript<a name="lambda-typescript"></a>

You can use the Node\.js runtime to run TypeScript code in AWS Lambda\. Because Node\.js doesn't run TypeScript code natively, you must first transpile your TypeScript code into JavaScript\. Then, use the JavaScript files to deploy your function code to Lambda\. Your code runs in an environment that includes the AWS SDK for JavaScript, with credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Node\.js runtimes\.


**Node\.js**  

| Name | Identifier | SDK | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | --- | 
|  Node\.js 16  |  `nodejs16.x`  |  2\.1083\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 14  |  `nodejs14.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 

## Setting up a TypeScript development environment<a name="typescript-dev"></a>

Use a local integrated development environment \(IDE\), text editor, or [AWS Cloud9](https://docs.aws.amazon.com/cloud9/latest/user-guide/sample-typescript.html) to write your TypeScript function code\. You can’t create TypeScript code on the Lambda console\.

To transpile your TypeScript code, set up a compiler such as [esbuild](https://esbuild.github.io/) or Microsoft's TypeScript compiler \(`tsc`\) , which is bundled with the [TypeScript distribution](https://www.typescriptlang.org/download)\. You can use the [AWS Serverless Application Model \(AWS SAM\)](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-getting-started.html) or the [AWS Cloud Development Kit \(AWS CDK\)](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html) to simplify building and deploying TypeScript code\. Both tools use esbuild to transpile TypeScript code into JavaScript\.

When using esbuild, consider the following:
+ There are several [TypeScript caveats](https://esbuild.github.io/content-types/#typescript-caveats)\.
+ You must configure your TypeScript transpilation settings to match the Node\.js runtime that you plan to use\. For more information, see [Target](https://esbuild.github.io/api/#target) in the esbuild documentation\. For an example of a **tsconfig\.json** file that demonstrates how to target a specific Node\.js version supported by Lambda, refer to the [TypeScript GitHub repository](https://github.com/tsconfig/bases/blob/main/bases/node14.json)\.
+ esbuild doesn’t perform type checks\. To check types, use the `tsc` compiler\. Run `tsc -noEmit` or add a `"noEmit"` parameter to your **tsconfig\.json** file, as shown in the following example\. This configures `tsc` to not emit JavaScript files\. After checking types, use esbuild to convert the TypeScript files into JavaScript\.

**Example tsconfig\.json**  

```
 {
  "compilerOptions": {
    "target": "es2020",
    "strict": true,
    "preserveConstEnums": true,
    "noEmit": true,
    "sourceMap": false,
    "module":"commonjs",
    "moduleResolution":"node",
    "esModuleInterop": true, 
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true, 
    "isolatedModules": true, 
  },
  "exclude": ["node_modules", "**/*.test.ts"]
}
```

**Topics**
+ [Setting up a TypeScript development environment](#typescript-dev)
+ [AWS Lambda function handler in TypeScript](typescript-handler.md)
+ [Deploy transpiled TypeScript code in Lambda with \.zip file archives](typescript-package.md)
+ [Deploy transpiled TypeScript code in Lambda with container images](typescript-image.md)
+ [AWS Lambda function errors in TypeScript](typescript-exceptions.md)

The following topics about the Node\.js runtime are also relevant for TypeScript:
+ [AWS Lambda context object in Node\.js](nodejs-context.md)
+ [AWS Lambda function logging in Node\.js](nodejs-logging.md)
+ [Instrumenting Node\.js code in AWS Lambda](nodejs-tracing.md)