# Using Stream Input and Output Types for Java Functions in AWS Lambda<a name="java-handler-io-type-stream"></a>

If you do not want to use POJOs or if Lambda's serialization approach does not meet your needs, you can use the byte stream implementation\. In this case, you can use the `InputStream` and `OutputStream` as the input and output types for the handler\.

The following is a Lambda function example that implements the handler that uses `InputStream` and `OutputStream` types for the input and output parameters\.

**Example Hello\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Hello implements RequestStreamHandler {
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        int letter;
        while ((letter = inputStream.read()) != -1) {
            outputStream.write(Character.toUpperCase(letter));
        }
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`

**Handler** - `example.Hello::handleRequest`

The event received processed by the function must be valid JSON but the output stream type is not restricted\. Any bytes are supported\.
