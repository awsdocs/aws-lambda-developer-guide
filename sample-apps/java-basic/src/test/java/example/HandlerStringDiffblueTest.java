package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

class HandlerStringDiffblueTest {
  /**
   * Method under test: {@link HandlerString#handleRequest(String, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    HandlerString handlerString = new HandlerString();

    // Act and Assert
    assertEquals("event", handlerString.handleRequest("Event", new TestContext()));
  }
}
