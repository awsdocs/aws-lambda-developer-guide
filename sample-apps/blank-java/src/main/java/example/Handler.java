package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.StringBuilder;
import java.util.Map;

public class Handler implements RequestHandler<SQSEvent, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  // example.Handler::handleRequest
  @Override
  public String handleRequest(SQSEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    for(SQSMessage msg : event.getRecords()){
      logger.log(msg.getBody());
    }
    return context.getAwsRequestId();
  }
}