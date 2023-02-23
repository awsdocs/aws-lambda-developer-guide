package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ConfigEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerConfig
public class HandlerConfig implements RequestHandler<ConfigEvent, String>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerConfig.class);

  @Override
  public String handleRequest(ConfigEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    return event.getConfigRuleArn();
  }
}