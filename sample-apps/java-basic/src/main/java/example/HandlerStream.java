package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

// Handler value: example.HandlerStream
public class HandlerStream implements RequestStreamHandler {

  @Override
  /*
   * Takes an InputStream and an OutputStream. Reads from the InputStream,
   * and copies all characters to the OutputStream.
   */
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
  {
    LambdaLogger logger = context.getLogger();
    int nextChar;
    try {
      while ((nextChar = inputStream.read()) != -1) {
        outputStream.write(nextChar);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      inputStream.close();
      String finalString = outputStream.toString();
      logger.log("Final string result: " + finalString);
      outputStream.close();
    }
  }
}