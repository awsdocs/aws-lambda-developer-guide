package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

class InvokeTest {
  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

  @Test
  void invokeTest() {
    S3Event event = new S3Event();
    
    Context context = new TestContext();
    String requestId = context.getAwsRequestId();
    Handler handler = new Handler();
    // String result = handler.handleRequest(event, context);
    // assertTrue(result.contains("Ok"));
  }

}
