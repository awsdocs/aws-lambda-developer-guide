package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// Handler value: example.HandlerString
public class HandlerString implements RequestHandler<String, String>{

  @Override
  /*
   * Takes a String as input, and converts all characters to lowercase.
   */
  public String handleRequest(String event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event.toLowerCase();
  }
}