package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerSNS
public class HandlerSNS implements RequestHandler<SNSEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerSNS.class);

  @Override
  public List<String> handleRequest(SNSEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var messagesFound = new ArrayList<String>();
    for (SNSRecord record : event.getRecords()) {
      SNS message = record.getSNS();
      messagesFound.add(message.getMessage());
    }
    return messagesFound;
  }
}