package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.amazonaws.services.lambda.runtime.Context;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HandlerDivideDiffblueTest {
  /**
   * Method under test: {@link HandlerDivide#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    HandlerDivide handlerDivide = new HandlerDivide();
    ArrayList<Integer> event = new ArrayList<>();

    // Act and Assert
    assertThrows(InputLengthException.class, () -> handlerDivide.handleRequest(event, new TestContext()));
  }

  /**
   * Method under test: {@link HandlerDivide#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest2() {
    // Arrange
    HandlerDivide handlerDivide = new HandlerDivide();

    ArrayList<Integer> event = new ArrayList<>();
    event.add(1);
    event.add(1);

    // Act and Assert
    assertEquals(1, handlerDivide.handleRequest(event, new TestContext()).intValue());
  }

  /**
   * Method under test: {@link HandlerDivide#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest3() {
    // Arrange
    HandlerDivide handlerDivide = new HandlerDivide();

    ArrayList<Integer> event = new ArrayList<>();
    event.add(2);

    // Act and Assert
    assertThrows(InputLengthException.class, () -> handlerDivide.handleRequest(event, new TestContext()));
  }

  /**
   * Method under test: {@link HandlerDivide#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest4() {
    // Arrange
    HandlerDivide handlerDivide = new HandlerDivide();

    ArrayList<Integer> event = new ArrayList<>();
    event.add(1);
    event.add(0);

    // Act and Assert
    assertThrows(ArithmeticException.class, () -> handlerDivide.handleRequest(event, new TestContext()));
  }
}
