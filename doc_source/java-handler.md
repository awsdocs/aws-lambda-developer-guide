# AWS Lambda function handler in Java<a name="java-handler"></a>

The Lambda function *handler* is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

In the following example, a class named `Handler` defines a handler method named `handleRequest`\. The handler method takes an event and context object as input and returns a string\.

**Example [Handler\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/Handler.java)**  

```
package example;
import [com\.amazonaws\.services\.lambda\.runtime\.Context](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/Context.java)
import [com\.amazonaws\.services\.lambda\.runtime\.RequestHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestHandler.java)
import [com\.amazonaws\.services\.lambda\.runtime\.LambdaLogger](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/LambdaLogger.java)
...

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(Map<String,String> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("200 OK");
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return response;
  }
}
```

The [Lambda runtime](lambda-runtimes.md) receives an event as a JSON\-formatted string and converts it into an object\. It passes the event object to your function handler along with a context object that provides details about the invocation and the function\. You tell the runtime which method to invoke by setting the handler parameter on your function's configuration\.

**Handler formats**
+ `package.Class::method` – Full format\. For example: `example.Handler::handleRequest`\.
+ `package.Class` – Abbreviated format for classes that implement a [handler interface](#java-handler-interfaces)\. For example: `example.Handler`\.

You can add [initialization code](https://docs.aws.amazon.com/lambda/latest/operatorguide/static-initialization.html) outside of your handler method to reuse resources across multiple invocations\. When the runtime loads your handler, it runs static code and the class constructor\. Resources that are created during initialization stay in memory between invocations, and can be reused by the handler thousands of times\.

In the following example, the logger and the serializer are created when the function serves its first event\. Subsequent events served by the same function instance are much faster because those resources already exist\.

**Example [HandlerS3\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events/src/main/java/example/HandlerS3.java) – Initialization code**  

```
public class HandlerS3 implements RequestHandler<S3Event, String>{
    private static final Logger logger = LoggerFactory.getLogger(HandlerS3.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(S3Event event, Context context)
    {
      ...
    }
```

The GitHub repo for this guide provides easy\-to\-deploy sample applications that demonstrate a variety of handler types\. For details, see the [end of this topic](#java-handler-samples)\.

**Topics**
+ [Choosing input and output types](#java-handler-types)
+ [Handler interfaces](#java-handler-interfaces)
+ [Sample handler code](#java-handler-samples)

## Choosing input and output types<a name="java-handler-types"></a>

You specify the type of object that the event maps to in the handler method's signature\. In the preceding example, the Java runtime deserializes the event into a type that implements the `Map<String,String>` interface\. String\-to\-string maps work for flat events like the following:

**Example [Event\.json](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/event.json) – Weather data**  

```
{
  "temperatureK": 281,
  "windKmh": -3,
  "humidityPct": 0.55,
  "pressureHPa": 1020
}
```

However, the value of each field must be a string or number\. If the event includes a field that has an object as a value, the runtime can't deserialize it and returns an error\.

Choose an input type that works with the event data that your function processes\. You can use a basic type, a generic type, or a well\-defined type\.

**Input types**
+ `Integer`, `Long`, `Double`, etc\. – The event is a number with no additional formatting—for example, `3.5`\. The runtime converts the value into an object of the specified type\.
+ `String` – The event is a JSON string, including quotes—for example, `"My string."`\. The runtime converts the value \(without quotes\) into a `String` object\.
+ `Type`, `Map<String,Type>` etc\. – The event is a JSON object\. The runtime deserializes it into an object of the specified type or interface\.
+ `List<Integer>`, `List<String>`, `List<Object>`, etc\. – The event is a JSON array\. The runtime deserializes it into an object of the specified type or interface\.
+ `InputStream` – The event is any JSON type\. The runtime passes a byte stream of the document to the handler without modification\. You deserialize the input and write output to an output stream\.
+ Library type – For events sent by AWS services, use the types in the [aws\-lambda\-java\-events](java-package.md) library\.

If you define your own input type, it should be a deserializable, mutable plain old Java object \(POJO\), with a default constructor and properties for each field in the event\. Keys in the event that don't map to a property as well as properties that aren't included in the event are dropped without error\.

The output type can be an object or `void`\. The runtime serializes return values into text\. If the output is an object with fields, the runtime serializes it into a JSON document\. If it's a type that wraps a primitive value, the runtime returns a text representation of that value\.

## Handler interfaces<a name="java-handler-interfaces"></a>

The [aws\-lambda\-java\-core](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-core) library defines two interfaces for handler methods\. Use the provided interfaces to simplify handler configuration and validate the handler method signature at compile time\.

****
+ [com\.amazonaws\.services\.lambda\.runtime\.RequestHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestHandler.java)
+ [com\.amazonaws\.services\.lambda\.runtime\.RequestStreamHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestStreamHandler.java)

The `RequestHandler` interface is a generic type that takes two parameters: the input type and the output type\. Both types must be objects\. When you use this interface, the Java runtime deserializes the event into an object with the input type, and serializes the output into text\. Use this interface when the built\-in serialization works with your input and output types\.

**Example [Handler\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/Handler.java) – Handler interface**  

```
// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String>{
  @Override
  public String handleRequest(Map<String,String> event, Context context)
```

To use your own serialization, implement the `RequestStreamHandler` interface\. With this interface, Lambda passes your handler an input stream and output stream\. The handler reads bytes from the input stream, writes to the output stream, and returns void\.

The following example uses buffered reader and writer types to work with the input and output streams\. It uses the [Gson](https://github.com/google/gson) library for serialization and deserialization\.

**Example [HandlerStream\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerStream.java)**  

```
import [com\.amazonaws\.services\.lambda\.runtime\.Context](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/Context.java)
import [com\.amazonaws\.services\.lambda\.runtime\.RequestStreamHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestStreamHandler.java)
import [com\.amazonaws\.services\.lambda\.runtime\.LambdaLogger](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/LambdaLogger.java)
...
// Handler value: example.HandlerStream
public class HandlerStream implements RequestStreamHandler {
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
  {
    LambdaLogger logger = context.getLogger();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")));
    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
    try
    {
      HashMap event = gson.fromJson(reader, HashMap.class);
      logger.log("STREAM TYPE: " + inputStream.getClass().toString());
      logger.log("EVENT TYPE: " + event.getClass().toString());
      writer.write(gson.toJson(event));
      if (writer.checkError())
      {
        logger.log("WARNING: Writer encountered an error.");
      }
    }
    catch (IllegalStateException | JsonSyntaxException exception)
    {
      logger.log(exception.toString());
    }
    finally
    {
      reader.close();
      writer.close();
    }
  }
}
```

## Sample handler code<a name="java-handler-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of various handler types and interfaces\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](java-package.md) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.

The `java-events` and `s3-java` applications take an AWS service event as input and return a string\. The `java-basic` application includes several types of handlers:
+ [Handler\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/Handler.java) – Takes a `Map<String,String>` as input\.
+ [HandlerInteger\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerInteger.java) – Takes an `Integer` as input\.
+ [HandlerList\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerList.java) – Takes a `List<Integer>` as input\.
+ [HandlerStream\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerStream.java) – Takes an `InputStream` and `OutputStream` as input\.
+ [HandlerString\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerString.java) – Takes a `String` as input\.
+ [HandlerWeatherData\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerWeatherData.java) – Takes a custom type as input\.

To test different handler types, just change the handler value in the AWS SAM template\. For detailed instructions, see the sample application's readme file\.
