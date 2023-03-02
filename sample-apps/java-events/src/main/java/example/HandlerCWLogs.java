package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;

import java.util.Base64;
import java.util.Base64.Decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

// Handler value: example.HandlerCWLogs
public class HandlerCWLogs implements RequestHandler<CloudWatchLogsEvent, String>{

  @Override
  public String handleRequest(CloudWatchLogsEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    Decoder decoder = Base64.getDecoder();
    byte[] decodedEvent = decoder.decode(event.getAwsLogs().getData());
    StringBuilder output = new StringBuilder();
    try {
      GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(decodedEvent));
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      bufferedReader.lines().forEach( line -> {
        output.append(line);
      });
      // logger.info(output.toString());
    } catch(IOException e) {
        logger.log("ERROR: " + e.toString());
    }
    return output.toString();
  }
}
