package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerString
public class HandlerString implements RequestHandler<String, String>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerString.class);

  @Override
  /*
   * Takes a String as input, and converts all characters to lowercase.
   */
  public String handleRequest(String event, Context context)
  {
    logger.info("EVENT: " + event);
    logger.info("EVENT TYPE: " + event.getClass().toString());
    return event.toLowerCase();
  }
}