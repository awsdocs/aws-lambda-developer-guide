package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerDynamoDB
public class HandlerDynamoDB implements RequestHandler<DynamodbEvent, List<String>>{

  @Override
  public List<String> handleRequest(DynamodbEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var operationsFound = new ArrayList<String>();
    for (DynamodbStreamRecord record : event.getRecords()) {
      operationsFound.add(record.getEventName());
    }
    return operationsFound;
  }
}