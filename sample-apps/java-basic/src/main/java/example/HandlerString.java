package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

// Handler value: example.HandlerString
public class HandlerString implements RequestHandler<String, Integer>{

  @Override
  public Integer handleRequest(String event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT: " + event);
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return context.getRemainingTimeInMillis();
  }
}