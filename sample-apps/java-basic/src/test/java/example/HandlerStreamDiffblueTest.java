package example;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.amazonaws.services.lambda.runtime.Context;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HandlerStreamDiffblueTest {
  /**
   * Method under test:
   * {@link HandlerStream#handleRequest(InputStream, OutputStream, Context)}
   */
  @Test
  void testHandleRequest() throws IOException {
    // Arrange
    HandlerStream handlerStream = new HandlerStream();
    DataInputStream inputStream = mock(DataInputStream.class);
    when(inputStream.read(Mockito.<byte[]>any(), anyInt(), anyInt())).thenReturn(0);
    doNothing().when(inputStream).close();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);

    // Act
    handlerStream.handleRequest(inputStream, outputStream, new TestContext());

    // Assert that nothing has changed
    verify(inputStream).read(Mockito.<byte[]>any(), anyInt(), anyInt());
    verify(inputStream).close();
  }

  /**
   * Method under test:
   * {@link HandlerStream#handleRequest(InputStream, OutputStream, Context)}
   */
  @Test
  void testHandleRequest2() throws IOException {
    // Arrange
    HandlerStream handlerStream = new HandlerStream();
    DataInputStream inputStream = mock(DataInputStream.class);
    when(inputStream.available()).thenReturn(1);
    when(inputStream.read(Mockito.<byte[]>any(), anyInt(), anyInt())).thenReturn(1);
    doNothing().when(inputStream).close();
    PipedOutputStream outputStream = new PipedOutputStream();

    // Act
    handlerStream.handleRequest(inputStream, outputStream, new TestContext());

    // Assert that nothing has changed
    verify(inputStream, atLeast(1)).read(Mockito.<byte[]>any(), anyInt(), anyInt());
    verify(inputStream, atLeast(1)).available();
    verify(inputStream).close();
  }
}
