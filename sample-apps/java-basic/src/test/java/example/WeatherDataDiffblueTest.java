package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class WeatherDataDiffblueTest {
  /**
   * Methods under test:
   * 
   * <ul>
   *   <li>default or parameterless constructor of {@link WeatherData}
   *   <li>{@link WeatherData#setHumidityPct(Double)}
   *   <li>{@link WeatherData#setPressureHPa(Integer)}
   *   <li>{@link WeatherData#setTemperatureK(Integer)}
   *   <li>{@link WeatherData#setWindKmh(Integer)}
   *   <li>{@link WeatherData#getHumidityPct()}
   *   <li>{@link WeatherData#getPressureHPa()}
   *   <li>{@link WeatherData#getTemperatureK()}
   *   <li>{@link WeatherData#getWindKmh()}
   * </ul>
   */
  @Test
  void testConstructor() {
    // Arrange and Act
    WeatherData actualWeatherData = new WeatherData();
    actualWeatherData.setHumidityPct(10.0d);
    actualWeatherData.setPressureHPa(1);
    actualWeatherData.setTemperatureK(1);
    actualWeatherData.setWindKmh(1);
    Double actualHumidityPct = actualWeatherData.getHumidityPct();
    Integer actualPressureHPa = actualWeatherData.getPressureHPa();
    Integer actualTemperatureK = actualWeatherData.getTemperatureK();
    Integer actualWindKmh = actualWeatherData.getWindKmh();

    // Assert that nothing has changed
    assertEquals(1, actualPressureHPa.intValue());
    assertEquals(1, actualTemperatureK.intValue());
    assertEquals(1, actualWindKmh.intValue());
    assertEquals(10.0d, actualHumidityPct.doubleValue());
  }
}
