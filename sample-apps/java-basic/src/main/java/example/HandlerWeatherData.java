package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

// Handler value: example.HandlerWeatherData
public class HandlerWeatherData implements RequestHandler<WeatherData, WeatherData>{

  @Override
  public WeatherData handleRequest(WeatherData event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT: " + event);
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return event;
  }
}