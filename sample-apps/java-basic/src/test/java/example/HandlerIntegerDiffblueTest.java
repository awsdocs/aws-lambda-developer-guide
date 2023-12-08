package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

class HandlerIntegerDiffblueTest {
  /**
   * Method under test: {@link HandlerInteger#handleRequest(Integer, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    HandlerInteger handlerInteger = new HandlerInteger();

    // Act and Assert
    assertEquals(2, handlerInteger.handleRequest(1, new TestContext()).intValue());
  }
}
