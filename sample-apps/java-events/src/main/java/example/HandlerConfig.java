package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ConfigEvent;

// Handler value: example.HandlerConfig
public class HandlerConfig implements RequestHandler<ConfigEvent, String>{

  @Override
  public String handleRequest(ConfigEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event.getConfigRuleArn();
  }
}