package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

// Handler value: example.HandlerInteger
public class HandlerInteger implements RequestHandler<Integer, Integer>{

  @Override
  public Integer handleRequest(Integer event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT: " + event);
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return context.getRemainingTimeInMillis();
  }
}