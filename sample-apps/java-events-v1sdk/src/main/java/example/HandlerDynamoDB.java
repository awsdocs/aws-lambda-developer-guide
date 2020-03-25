package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import com.amazonaws.services.dynamodbv2.model.Record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerDynamoDB
public class HandlerDynamoDB implements RequestHandler<DynamodbEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerDynamoDB.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(DynamodbEvent event, Context context)
  {
    String response = new String("200 OK");
    for (DynamodbStreamRecord record : event.getRecords()){
      logger.info(record.getEventID());
      logger.info(record.getEventName());
      logger.info(record.getDynamodb().toString());
    }
    logger.info("Successfully processed " + event.getRecords().size() + " records.");
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}