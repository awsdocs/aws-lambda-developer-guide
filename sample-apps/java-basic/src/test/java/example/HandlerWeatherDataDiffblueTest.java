package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

class HandlerWeatherDataDiffblueTest {
  /**
   * Method under test:
   * {@link HandlerWeatherData#handleRequest(WeatherData, Context)}
   */
  @Test
  void testHandleRequest() {
    // Arrange
    HandlerWeatherData handlerWeatherData = new HandlerWeatherData();

    WeatherData event = new WeatherData();
    event.setHumidityPct(10.0d);
    event.setPressureHPa(1);
    event.setTemperatureK(1);
    event.setWindKmh(1);

    // Act
    WeatherData actualHandleRequestResult = handlerWeatherData.handleRequest(event, new TestContext());

    // Assert
    assertEquals(1005, actualHandleRequestResult.getPressureHPa().intValue());
    assertEquals(28, actualHandleRequestResult.getWindKmh().intValue());
    assertEquals(3, actualHandleRequestResult.getTemperatureK().intValue());
    assertEquals(50.5d, actualHandleRequestResult.getHumidityPct().doubleValue());
    assertSame(event, actualHandleRequestResult);
  }
}
