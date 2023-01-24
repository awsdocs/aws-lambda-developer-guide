# Using AWS Lambda with Secrets Manager<a name="with-secrets-manager"></a>

Secrets Manager uses a Lambda function to [ rotate the secret](https://docs.aws.amazon.com/secretsmanager/latest/userguide/rotating-secrets.html) for a secured service or database\. You can customize the Lambda function to implement the service\-specific details of rotating a secret\.

Secrets Manager invokes the Lambda rotation function as a synchronous invocation\. The event parameter contains the following fields: 

```
{
  "Step" : "request.type",
  "SecretId" : "string",
  "ClientRequestToken" : "string"
}
```

For more information about using Lambda with Secrets Manager, see [Understanding Your Lambda rotation function](https://docs.aws.amazon.com/secretsmanager/latest/userguide/rotating-secrets-lambda-function-customizing.html)\. 