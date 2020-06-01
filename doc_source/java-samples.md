# Java sample applications for AWS Lambda<a name="java-samples"></a>

The GitHub repository for this guide provides sample applications that demonstrate the use of Java in AWS Lambda\. Each sample application includes scripts for easy deployment and cleanup, an AWS CloudFormation template, and supporting resources\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events) – A minimal Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that don't require the AWS SDK as a dependency, such as Amazon API Gateway\.
+ [java\-events\-v1sdk](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events-v1sdk) – A Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that require the AWS SDK as a dependency \(Amazon Simple Storage Service, Amazon DynamoDB, and Amazon Kinesis\)\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.

Use the `blank-java` sample app to learn the basics, or as a starting point for your own application\. It shows the use of Lambda's Java libraries, environment variables, the AWS SDK, and the AWS X\-Ray SDK\. It uses a Lambda layer to package its dependencies separately from the function code, which speeds up deployment times when you are iterating on your function code\. The project requires minimal setup and can be deployed from the command line in less than a minute\.

[https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java)

The other sample applications show other build configurations, handler interfaces, and use cases for services that integrate with Lambda\. The `java-basic` sample shows a function with minimal dependencies\. You can use this sample for cases where you don't need additional libraries like the AWS SDK, and can represent your function's input and output with standard Java types\. To try a different handler type, you can simply change the handler setting on the function\.

**Example [java\-basic/src/main/java/example/HandlerStream\.java](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/java-basic/src/main/java/example/HandlerStream.java) – Stream handler**  

```
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
    ...
```

The `java-events` and `java-events-v1sdk` samples show the use of the event types provided by the `aws-lambda-java-events` library\. These types represent the event documents that [AWS services](lambda-services.md) send to your function\. `java-events` includes handlers for types that don't require additional dependencies\. For event types like `DynamodbEvent` that require types from the AWS SDK for Java, `java-events-v1sdk` includes the SDK in its build configuration\.

**Example [java\-events\-v1sdk/src/main/java/example/HandlerDynamoDB\.java](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/java-events-v1sdk/src/main/java/example/HandlerDynamoDB.java) – DynamoDB records**  

```
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.dynamodbv2.model.Record;
...
// Handler value: example.HandlerDynamoDB
public class HandlerDynamoDB implements RequestHandler<DynamodbEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerDynamoDB.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(DynamodbEvent event, Context context)
  {
    String response = new String("200 OK");
    for (DynamodbStreamRecord record : event.getRecords()){
      logger.info(record.getEventID());
      logger.info(record.getEventName());
      logger.info(record.getDynamodb().toString());
    }
    ...
```

For more highlights, see the other topics in this chapter\.