package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerSQS
public class HandlerSQS implements RequestHandler<SQSEvent, Void>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerSQS.class);
  @Override
  public Void handleRequest(SQSEvent event, Context context)
  {
    for(SQSMessage msg : event.getRecords()){
      logger.info(msg.getBody());
    }
    return null;
  }
}
