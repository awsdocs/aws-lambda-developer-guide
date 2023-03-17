package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerSQS
public class HandlerSQS implements RequestHandler<SQSEvent, List<String>>{

  @Override
  public List<String> handleRequest(SQSEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var messagesFound = new ArrayList<String>();
    for(SQSMessage msg : event.getRecords()){
      messagesFound.add(msg.getBody());
    }
    return messagesFound;
  }
}
