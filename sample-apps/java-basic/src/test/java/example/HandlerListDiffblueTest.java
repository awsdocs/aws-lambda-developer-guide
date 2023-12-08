package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.amazonaws.services.lambda.runtime.Context;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HandlerListDiffblueTest {
  /**
   * Method under test: {@link HandlerList#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    HandlerList handlerList = new HandlerList();
    ArrayList<Integer> event = new ArrayList<>();

    // Act and Assert
    assertEquals(0, handlerList.handleRequest(event, new TestContext()).intValue());
  }

  /**
   * Method under test: {@link HandlerList#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest2() {
    // Arrange
    HandlerList handlerList = new HandlerList();

    ArrayList<Integer> event = new ArrayList<>();
    event.add(2);

    // Act and Assert
    assertEquals(2, handlerList.handleRequest(event, new TestContext()).intValue());
  }

  /**
   * Method under test: {@link HandlerList#handleRequest(List, Context)}
   */
  @Test
  void testHandleRequest3() {
    // Arrange
    HandlerList handlerList = new HandlerList();

    ArrayList<Integer> event = new ArrayList<>();
    event.add(1);
    event.add(2);

    // Act and Assert
    assertEquals(3, handlerList.handleRequest(event, new TestContext()).intValue());
  }
}
