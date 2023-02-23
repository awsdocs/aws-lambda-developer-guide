package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerDynamoDB
public class HandlerDynamoDB implements RequestHandler<DynamodbEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerDynamoDB.class);

  @Override
  public List<String> handleRequest(DynamodbEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var operationsFound = new ArrayList<String>();
    for (DynamodbStreamRecord record : event.getRecords()) {
      operationsFound.add(record.getEventName());
    }
    return operationsFound;
  }
}