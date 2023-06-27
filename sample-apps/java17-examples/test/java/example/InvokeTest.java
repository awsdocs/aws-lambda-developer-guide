package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent;
import com.amazonaws.services.lambda.runtime.events.ConfigEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvokeTest {

  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Test
  void testHandlerIntegerJava17() {
    logger.info("Invoke TEST - HandlerIntegerJava17");
    InputRecord input = new InputRecord(2, 4, "Hello World!");
    HandlerIntegerJava17 handler = new HandlerIntegerJava17();
    assertEquals(6, handler.handleRequest(input, context));
  }
}
