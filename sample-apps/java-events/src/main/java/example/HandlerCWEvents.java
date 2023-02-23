package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerCWEvents
public class HandlerCWEvents implements RequestHandler<ScheduledEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerCWEvents.class);

  @Override
  public List<String> handleRequest(ScheduledEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var resourcesFound = new ArrayList<String>();
    for (String resource : event.getResources()) {
      resourcesFound.add(resource);
    }
    return resourcesFound;
  }
}