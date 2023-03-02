package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.LexEvent;

// Handler value: example.HandlerLex
public class HandlerLex implements RequestHandler<LexEvent, String>{

  @Override
  public String handleRequest(LexEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event.getCurrentIntent().getName();
  }
}