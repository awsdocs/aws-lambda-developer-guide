package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Handler value: example.HandlerFirehose
public class HandlerFirehose implements RequestHandler<KinesisFirehoseEvent, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(KinesisFirehoseEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("200 OK");
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}