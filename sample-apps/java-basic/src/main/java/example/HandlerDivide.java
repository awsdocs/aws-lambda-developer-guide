package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;

// Handler value: example.HandlerDivide
public class HandlerDivide implements RequestHandler<List<Integer>, Integer>{
  /*
   * Takes a list of two integers and divides them.
   */
  @Override
  public Integer handleRequest(List<Integer> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    if ( event.size() != 2 )
    {
      throw new InputLengthException("Input must be an array that contains 2 numbers.");
    }
    int numerator = event.get(0);
    int denominator = event.get(1);
    logger.log("EVENT: Numerator is " + event.get(0).toString() +
      "; Denominator is " + event.get(1).toString());
    return numerator/denominator;
  }
}