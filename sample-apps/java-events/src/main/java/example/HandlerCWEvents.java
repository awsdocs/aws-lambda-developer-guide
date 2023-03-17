package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerCWEvents
public class HandlerCWEvents implements RequestHandler<ScheduledEvent, List<String>>{

  @Override
  public List<String> handleRequest(ScheduledEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var resourcesFound = new ArrayList<String>();
    for (String resource : event.getResources()) {
      resourcesFound.add(resource);
    }
    return resourcesFound;
  }
}