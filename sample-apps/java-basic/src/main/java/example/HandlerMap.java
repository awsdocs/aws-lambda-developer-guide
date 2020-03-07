package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

// Handler value: example.HandlerMap
public class HandlerMap implements RequestHandler<Map<String,String>, Map<String,String> >{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public Map<String,String> handleRequest(Map<String,String> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event;
  }
}