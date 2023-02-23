package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent.DatasetRecord;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerCognito
public class HandlerCognito implements RequestHandler<CognitoEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerCognito.class);

  @Override
  public List<String> handleRequest(CognitoEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var operationsFound = new ArrayList<String>();
    for (DatasetRecord record : event.getDatasetRecords().values()) {
      operationsFound.add(record.getOp());
    }
    return operationsFound;
  }
}