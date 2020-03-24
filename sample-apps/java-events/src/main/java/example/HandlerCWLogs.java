package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent.AWSLogs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Base64;
import java.util.Base64.Decoder;

import java.util.zip.Inflater;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

// Handler value: example.HandlerCWLogs
public class HandlerCWLogs implements RequestHandler<CloudWatchLogsEvent, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(CloudWatchLogsEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("200 OK");
    Decoder decoder = Base64.getDecoder();
    byte[] decodedEvent = decoder.decode(event.getAwsLogs().getData());
    StringBuilder output = new StringBuilder();
    try {
      GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(decodedEvent));
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      bufferedReader.lines().forEach( line -> {
        logger.log(line);
        output.append(line);
      });
      logger.log(output.toString());
    } catch(IOException e) {
        logger.log("ERROR: " + e.toString());
    }
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}
