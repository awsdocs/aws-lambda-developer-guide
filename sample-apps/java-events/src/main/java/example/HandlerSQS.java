package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerSQS
public class HandlerSQS implements RequestHandler<SQSEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerSQS.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(SQSEvent event, Context context)
  {
    String response = new String("200 OK");
    // log execution details
    logger.info("ENVIRONMENT VARIABLES: {}", gson.toJson(System.getenv()));
    logger.info("CONTEXT: {}", gson.toJson(context));
    logger.info("EVENT: {}", gson.toJson(event));
    // process event
    for(SQSMessage msg : event.getRecords()){
      logger.info(msg.getBody());
    }
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}