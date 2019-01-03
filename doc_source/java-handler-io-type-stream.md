# Example: Using Stream for Handler Input/Output \(Java\)<a name="java-handler-io-type-stream"></a>

If you do not want to use POJOs or if Lambda's serialization approach does not meet your needs, you can use the byte stream implementation\. In this case, you can use the `InputStream` and `OutputStream` as the input and output types for the handler\. An example handler function is shown:

```
public void handler(InputStream inputStream, OutputStream outputStream, Context context) throws IOException{
   ...
}
```

Note that in this case the handler function uses parameters for both the request and response streams\. 

The following is a Lambda function example that implements the handler that uses `InputStream` and `OutputStream` types for the `input` and `output` parameters\. 

**Note**  
The input payload must be valid JSON but the output stream does not carry such a restriction\. Any bytes are supported\.

```
package example;

import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context; 

public class Hello implements RequestStreamHandler{
    public void handler(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        int letter;
        while((letter = inputStream.read()) != -1)
        {
            outputStream.write(Character.toUpperCase(letter));
        }
    }
}
```

You can do the following to test the code:
+ Using the preceding code, create a deployment package\.
+ Upload the deployment package to AWS Lambda and create your Lambda function\. You can do this using the console or AWS CLI\.
+ You can manually invoke the code by providing sample input\. For example:

  ```
  test
  ```

Follow instructions provided in the Getting Started\. For more information, see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\.
+ When you create the Lambda function, specify `example.Hello::handler` \(*package*\.*class*::*method*\) as the handler value\.