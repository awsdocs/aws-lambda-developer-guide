package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerSNS
public class HandlerSNS implements RequestHandler<SNSEvent, List<String>>{

  @Override
  public List<String> handleRequest(SNSEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var messagesFound = new ArrayList<String>();
    for (SNSRecord record : event.getRecords()) {
      SNS message = record.getSNS();
      messagesFound.add(message.getMessage());
    }
    return messagesFound;
  }
}