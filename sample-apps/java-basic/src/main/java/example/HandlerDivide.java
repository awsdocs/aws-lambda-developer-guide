package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

// Handler value: example.HandlerDivide
public class HandlerDivide implements RequestHandler<List<Integer>, Integer>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public Integer handleRequest(List<Integer> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    // process event
    if ( event.size() != 2 )
    {
      throw new InputLengthException("Input must be an array that contains 2 numbers.");
    }
    int numerator = event.get(0);
    int denominator = event.get(1);
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return numerator/denominator;
  }
}