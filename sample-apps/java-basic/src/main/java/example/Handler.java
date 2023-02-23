package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, Void>{

  private static final Logger logger = LoggerFactory.getLogger(Handler.class);

  @Override
  public Void handleRequest(Map<String,String> event, Context context)
  {
    logger.info("ENVIRONMENT VARIABLES: " + System.getenv());
    logger.info("EVENT: " + event);
    logger.info("EVENT TYPE: " + event.getClass());
    return null;
  }
}