package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerWeatherData
public class HandlerWeatherData implements RequestHandler<WeatherData, WeatherData>{

  private static final Logger logger = LoggerFactory.getLogger(WeatherData.class);

  @Override
  /*
   * Takes in a WeatherData event object and updates its attributes with dummy values.
   * Returns the updated WeatherData object.
   */
  public WeatherData handleRequest(WeatherData event, Context context)
  {
    logger.info("EVENT: " + event);
    logger.info("EVENT TYPE: " + event.getClass().toString());

    event.setHumidityPct(50.5);
    event.setPressureHPa(1005);
    event.setWindKmh(28);
    // Assumes temperature of event is already set
    event.setTemperatureK(event.getTemperatureK() + 2);
    return event;
  }
}