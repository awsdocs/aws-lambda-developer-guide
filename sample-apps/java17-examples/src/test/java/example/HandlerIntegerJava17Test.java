package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvokeTest {

  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

  @Test
  public void testHandlerIntegerJava17() {
    logger.info("Invoke TEST - HandlerIntegerJava17");
    IntegerRecord input = new IntegerRecord(2, 4, "Hello World!");
    Context context = new TestContext();
    HandlerIntegerJava17 handler = new HandlerIntegerJava17();
    assertEquals(6, handler.handleRequest(input, context));
  }
}
