package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent.DatasetRecord;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerCognito
public class HandlerCognito implements RequestHandler<CognitoEvent, List<String>>{

  @Override
  public List<String> handleRequest(CognitoEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var operationsFound = new ArrayList<String>();
    for (DatasetRecord record : event.getDatasetRecords().values()) {
      operationsFound.add(record.getOp());
    }
    return operationsFound;
  }
}