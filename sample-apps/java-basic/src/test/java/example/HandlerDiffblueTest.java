package example;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.amazonaws.services.lambda.runtime.Context;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HandlerDiffblueTest {
  /**
   * Method under test: {@link Handler#handleRequest(Map, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    Handler handler = new Handler();
    HashMap<String, String> event = new HashMap<>();

    // Act and Assert
    assertNull(handler.handleRequest(event, new TestContext()));
  }
}
