package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Handler value: example.HandlerInteger
public class HandlerInteger implements RequestHandler<Integer, Integer>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public Integer handleRequest(Integer event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    // return amount of time remaining before timeout
    return context.getRemainingTimeInMillis();
  }
}