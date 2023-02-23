package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerInteger
public class HandlerInteger implements RequestHandler<Integer, Integer>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerInteger.class);

  @Override
  /*
   * Takes an Integer as input, adds 1, and returns it.
   */
  public Integer handleRequest(Integer event, Context context)
  {
    logger.info("EVENT: " + event);
    logger.info("EVENT TYPE: " + event.getClass().toString());
    return event + 1;
  }
}