# AWS Lambda function errors in TypeScript<a name="typescript-exceptions"></a>

If an exception occurs in TypeScript code that's transpiled into JavaScript, use source map files to determine where the error occurred\. Source map files allow debuggers to map compiled JavaScript files to the TypeScript source code\.

For example, the following code results in an error:

```
export const handler = async (event: unknown): Promise<unknown> => {
    throw new Error('Some exception');
};
```

AWS Lambda catches the error and generates a JSON document\. However, this JSON document refers to the compiled JavaScript file \(**app\.js**\), not the TypeScript source file\. 

```
{
  "errorType": "Error",
  "errorMessage": "Some exception",
  "stack": [
      "Error: Some exception",
      "    at Runtime.p [as handler] (/var/task/app.js:1:491)",
      "    at Runtime.handleOnce (/var/runtime/Runtime.js:66:25)"
  ]
}
```

**To get an error response that maps to your TypeScript source file**
**Note**  
The following steps aren't valid for Lambda@Edge functions because Lambda@Edge doesn't support environment variables\.

1. Generate a source map file with esbuild or another TypeScript compiler\. Example:

   ```
   esbuild app.ts —sourcemap —outfile=output.js
   ```

1. Add the source map to your deployment\.

1. Turn on source maps for the Node\.js runtime by adding `--enable-source-maps` to your `NODE_OPTIONS`\.

**Example for the AWS Serverless Application Model \(AWS SAM\)**  

```
Globals:
    Function:
      Environment:
        Variables:
          NODE_OPTIONS: '--enable-source-maps'
```
Make sure that the esbuild properties in your **template\.yaml** file include `Sourcemap: true`\. Example:  

```
Metadata: # Manage esbuild properties
  BuildMethod: esbuild
  BuildProperties:
    Minify: true
    Target: "es2020"
    Sourcemap: true
    EntryPoints: 
    - app.ts
```

**Example for the AWS Cloud Development Kit \(AWS CDK\)**  
To use a source map with an AWS CDK application, add the following code to the file that contains the [NodejsFunction construct](https://docs.aws.amazon.com/cdk/api/v2/docs/aws-cdk-lib.aws_lambda_nodejs-readme.html)\.  

```
const helloFunction = new NodejsFunction(this, 'function',{
  bundling: {
    minify: true,
  sourceMap: true
  },
  environment:{
    NODE_OPTIONS: '--enable-source-maps',
  }
});
```

When you use a source map in your code, you get an error response similar to the following\. This response shows that the error happened at line 2, column 11 in the **app\.ts** file\.

```
{
  "errorType": "Error",
  "errorMessage": "Some exception",
  "stack": [
      "Error: Some exception",
      "    at Runtime.p (/private/var/folders/3c/0d4wz7dn2y75bw_hxdwc0h6w0000gr/T/tmpfmxb4ziy/app.ts:2:11)",
      "    at Runtime.handleOnce (/var/runtime/Runtime.js:66:25)"
  ]
}
```