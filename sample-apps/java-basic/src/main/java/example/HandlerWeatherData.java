package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// Handler value: example.HandlerWeatherData
public class HandlerWeatherData implements RequestHandler<WeatherData, WeatherData>{

  @Override
  /*
   * Takes in a WeatherData event object and updates its attributes with dummy values.
   * Returns the updated WeatherData object.
   */
  public WeatherData handleRequest(WeatherData event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());

    event.setHumidityPct(50.5);
    event.setPressureHPa(1005);
    event.setWindKmh(28);
    // Assumes temperature of event is already set
    event.setTemperatureK(event.getTemperatureK() + 2);
    return event;
  }
}