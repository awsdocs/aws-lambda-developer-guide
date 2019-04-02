# Instrumenting Java Code in AWS Lambda<a name="java-tracing"></a>

In Java, you can have Lambda emit subsegments to X\-Ray to show you information regarding downstream calls to other AWS services made by your function\. To take advantage of this capability, include the [AWS X\-Ray SDK for Java](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html) in your deployment package\. No code changes are needed\. As long as you are using an AWS SDK version 1\.11\.48 or later, there is no need to add any additional code lines for downstream calls from your function to be traced\. 

The AWS SDK will dynamically import the X\-Ray SDK to emit subsegments for downstream calls made by your function\. By using the X\-Ray SDK for Java, you can instrument your code in order to emit custom subsegments and or add annotations to your X\-Ray segments\. 

The following example uses the X\-Ray SDK for Java to instrument a Lambda function to emit a custom subsegment and send custom annotation to X\-Ray:

```
package uptime;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;

public class Hello {
    private static final Log logger = LogFactory.getLog(Hello.class);

    private static final AmazonDynamoDB dynamoClient;
    private static final HttpClient httpClient;

    static {
        dynamoClient = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        httpClient = HttpClientBuilder.create().build();
    }
    public void checkUptime(Context context) {
        AWSXRay.createSubsegment("makeRequest", (subsegment) -> {

            HttpGet request = new HttpGet("https://aws.amazon.com/");
            boolean is2xx = false;

            try {
                HttpResponse response = httpClient.execute(request);
                is2xx = (response.getStatusLine().getStatusCode() / 100) == 2;
                subsegment.putAnnotation("responseCode", response.getStatusLine().getStatusCode());
            } catch (IOException ioe) {
                logger.error(ioe);
            }
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("Timestamp", new AttributeValue().withN("" + Instant.now().getEpochSecond()));
            item.put("2xx", new AttributeValue().withBOOL(is2xx));
            dynamoClient.putItem("amazon-2xx", item);
        });
    }
}
```

Following is what a trace emitted by the code preceding looks like \(synchronous invocation\): 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Java.png)