# Programming Model<a name="programming-model-v2"></a>

You write code for your Lambda function in one of the languages AWS Lambda supports\. Regardless of the language you choose, there is a common pattern to writing code for a Lambda function that includes the following core concepts: 
+ **Handler** – Handler is the function AWS Lambda calls to start execution of your Lambda function\. You identify the handler when you create your Lambda function\. When a Lambda function is invoked, AWS Lambda starts executing your code by calling the handler function\. AWS Lambda passes any event data to this handler as the first parameter\. Your handler should process the incoming event data and may invoke any other functions/methods in your code\. 

   
+ **The context object and how it interacts with Lambda at runtime** – AWS Lambda also passes a  context  object to the handler function, as the second parameter\. Via this context object your code can interact with AWS Lambda\. For example, your code can find the execution time remaining before AWS Lambda terminates your Lambda function\. 

   

  In addition, for languages such as Node\.js, there is an asynchronous platform that uses callbacks\. AWS Lambda provides additional methods on this context object\. You use these context object methods to tell AWS Lambda to terminate your Lambda function and optionally return values to the caller\.

   
+ **Logging** – Your Lambda function can contain logging statements\. AWS Lambda writes these logs to CloudWatch Logs\. Specific language statements generate log entries, depending on the language you use to author your Lambda function code\. 

   
+ **Exceptions** – Your Lambda function needs to communicate the result of the function execution to AWS Lambda\. Depending on the language you author your Lambda function code, there are different ways to end a request successfully or to notify AWS Lambda an error occurred during execution\. If you invoke the function synchronously, then AWS Lambda forwards the result back to the client\.

**Note**  
 Your Lambda function code must be written in a stateless style, and have no affinity with the underlying compute infrastructure\. Your code should expect local file system access, child processes, and similar artifacts to be limited to the lifetime of the request\. Persistent state should be stored in Amazon S3, Amazon DynamoDB, or another cloud storage service\. Requiring functions to be stateless enables AWS Lambda to launch as many copies of a function as needed to scale to the incoming rate of events and requests\. These functions may not always run on the same compute instance from request to request, and a given instance of your Lambda function may be used more than once by AWS Lambda\. For more information, see [Best Practices for Working with AWS Lambda Functions](best-practices.md)

The following language specific topics provide detailed information:
+ [Programming Model\(Node\.js\)](programming-model.md)
+ [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)
+ [Programming Model for Authoring Lambda Functions in C\#](dotnet-programming-model.md)
+ [Programming Model for Authoring Lambda Functions in Python](python-programming-model.md)
+ [Programming Model for Authoring Lambda Functions in Go](go-programming-model.md)
+ [Programming Model for Authoring Lambda Functions in PowerShell](powershell-programming-model.md)