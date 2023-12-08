package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class InputLengthExceptionDiffblueTest {
  /**
   * Method under test: {@link InputLengthException#InputLengthException(String)}
   */
  @Test
  void testConstructor() {
    // Arrange and Act
    InputLengthException actualInputLengthException = new InputLengthException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualInputLengthException.getLocalizedMessage());
    assertEquals("An error occurred", actualInputLengthException.getMessage());
    assertNull(actualInputLengthException.getCause());
    assertEquals(0, actualInputLengthException.getSuppressed().length);
  }
}
