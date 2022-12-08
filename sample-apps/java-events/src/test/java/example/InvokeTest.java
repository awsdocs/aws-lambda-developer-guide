package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvokeTest {
  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

  @Test
  void invokeTest() {
    logger.info("Invoke TEST");
    APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
    Context context = new TestContext();
    String requestId = context.getAwsRequestId();
    Handler handler = new Handler();
    APIGatewayV2HTTPResponse result = handler.handleRequest(event, context);
    assertEquals(200, result.getStatusCode());
  }

}
