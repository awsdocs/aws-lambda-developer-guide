package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.event.S3EventNotification.RequestParametersEntity;
import com.amazonaws.services.s3.event.S3EventNotification.ResponseElementsEntity;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.event.S3EventNotification.UserIdentityEntity;
import com.amazonaws.services.s3.event.S3EventNotification.GlacierEventDataEntity;
import com.amazonaws.services.s3.event.S3EventNotification.S3BucketEntity;
import com.amazonaws.services.s3.event.S3EventNotification.S3ObjectEntity;
import com.amazonaws.services.s3.event.S3EventNotification.UserIdentityEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.lang.Long;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;

class InvokeTest {
  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

  public InvokeTest() {
    AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
    builder.withSamplingStrategy(new NoSamplingStrategy());
    AWSXRay.setGlobalRecorder(builder.build());
  }

  @Test
  void invokeTest() throws IOException {
    AWSXRay.beginSegment("java-s3-test");
    String bucket = new String(Files.readAllLines(Paths.get("bucket-name.txt")).get(0));
    S3EventNotificationRecord record = new S3EventNotificationRecord("us-east-2",
       "ObjectCreated:Put",
       "aws:s3",
       "2020-03-08T00:30:12.456Z",
       "2.1",
       new RequestParametersEntity("174.255.255.156"),
       new ResponseElementsEntity("nBbLJPAHhdvxmplPvtCgTrWCqf/KtonyV93l9rcoMLeIWJxpS9x9P8u01+Tj0OdbAoGs+VGvEvWl/Sg1NW5uEsVO25Laq7L", "AF2D7AB6002E898D"),
       new S3Entity("682bbb7a-xmpl-48ca-94b1-7f77c4d6dbf0",
        new S3BucketEntity(bucket,
          new UserIdentityEntity("A3XMPLFAF2AI3E"),
          "arn:aws:s3:::" + bucket),
        new S3ObjectEntity("inbound/sample-java-s3.png",
          new Long(21476),
          "d132690b6c65b6d1629721dcfb49b883",
          "",
          "005E64A65DF093B26D"),
        "1.0"),
       new UserIdentityEntity("AWS:AIDAINPONIXMPLT3IKHL2"));
    ArrayList<S3EventNotificationRecord> records = new ArrayList<S3EventNotificationRecord>();
    records.add(record);
    S3Event event = new S3Event(records);
    
    Context context = new TestContext();
    String requestId = context.getAwsRequestId();
    Handler handler = new Handler();
    String result = handler.handleRequest(event, context);
    assertTrue(result.contains("200 OK"));
    AWSXRay.endSegment();
  }

}
