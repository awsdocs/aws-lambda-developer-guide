package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// Handler value: example.HandlerInteger
public class HandlerInteger implements RequestHandler<Integer, Integer>{

  @Override
  /*
   * Takes an Integer as input, adds 1, and returns it.
   */
  public Integer handleRequest(Integer event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event + 1;
  }
}