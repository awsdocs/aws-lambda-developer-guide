# AWS Lambda function handler in TypeScript<a name="typescript-handler"></a>

The AWS Lambda function *handler* is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

The Node\.js runtime passes three arguments to the handler method:
+ The `event` object: Contains information from the invoker\.
+ The [context object](nodejs-context.md): Contains information about the invocation, function, and execution environment\.
+ The third argument, `callback`, is a function that you can call in non\-async handlers to send a response\. For async handlers, you return a response, error, or promise to the runtime instead of using `callback`\.

## Non\-async handlers<a name="non-async-typescript"></a>

For non\-async handlers, the runtime passes the event object, the [context object](nodejs-context.md), and the callback function to the handler method\. The response object in the callback function must be compatible with `JSON.stringify`\.

**Example TypeScript function – synchronous**  

```
import { Context, APIGatewayProxyCallback, APIGatewayEvent } from 'aws-lambda';

export const lambdaHandler = (event: APIGatewayEvent, context: Context, callback: APIGatewayProxyCallback): void => {
    console.log(`Event: ${JSON.stringify(event, null, 2)}`);
    console.log(`Context: ${JSON.stringify(context, null, 2)}`);
    callback(null, {
        statusCode: 200,
        body: JSON.stringify({
            message: 'hello world',
        }),
    });
};
```

## Async handlers<a name="async-typescript"></a>

For async handlers, you can use `return` and `throw` to send a response or error, respectively\. Functions must use the `async` keyword to use these methods to return a response or error\.

If your code performs an asynchronous task, return a promise to make sure that it finishes running\. When you resolve or reject the promise, Lambda sends the response or error to the invoker\.

**Example TypeScript function – asynchronous**  

```
import { Context, APIGatewayProxyResult, APIGatewayEvent } from 'aws-lambda';

export const lambdaHandler = async (event: APIGatewayEvent, context: Context): Promise<APIGatewayProxyResult> => {
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

## Using types for the event object<a name="event-types"></a>

We recommend that you don’t use the [any](https://www.typescriptlang.org/docs/handbook/declaration-files/do-s-and-don-ts.html#any) type for the handler arguments and return type because you lose the ability to check types\. Instead, generate an event using the [sam local generate\-event](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-local-generate-event.html) AWS Serverless Application Model CLI command, or use an open\-source definition from the [@types/aws\-lambda package](https://www.npmjs.com/package/@types/aws-lambda)\.

**Generating an event using the sam local generate\-event command**

1. Generate an Amazon Simple Storage Service \(Amazon S3\) proxy event\.

   ```
   sam local generate-event s3 put >> S3PutEvent.json
   ```

1. Use the [quicktype utility](https://quicktype.io/typescript) to generate type definitions from the **S3PutEvent\.json** file\.

   ```
   npm install -g quicktype
   quicktype S3PutEvent.json -o S3PutEvent.ts
   ```

1. Use the generated types in your code\.

   ```
   import { S3PutEvent } from './S3PutEvent';
   
   export const lambdaHandler = async (event: S3PutEvent): Promise<void> => {
     event.Records.map((record) => console.log(record.s3.object.key));
   };
   ```

**Generating an event using an open\-source definition from the @types/aws\-lambda package**

1. Add the [@types/aws\-lambda](https://www.npmjs.com/package/@types/aws-lambda) package as a development dependency\.

   ```
   npm install -D @types/aws-lambda
   ```

1. Use the types in your code\.

   ```
   import { S3Event } from "aws-lambda";
   
   export const lambdaHandler = async (event: S3Event): Promise<void> => {
     event.Records.map((record) => console.log(record.s3.object.key));
   };
   ```