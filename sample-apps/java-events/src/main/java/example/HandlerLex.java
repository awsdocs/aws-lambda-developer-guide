package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.LexEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerLex
public class HandlerLex implements RequestHandler<LexEvent, String>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerLex.class);

  @Override
  public String handleRequest(LexEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    return event.getCurrentIntent().getName();
  }
}