package example;

public class WeatherData {

  private Integer temperatureK;
  private Integer windKmh;
  private Double humidityPct;
  private Integer pressureHPa;

  public Integer getTemperatureK() {
    return temperatureK;
  }

  public void setTemperatureK(Integer temperatureK) {
    this.temperatureK = temperatureK;
  }

  public Integer getWindKmh() {
    return windKmh;
  }

  public void setWindKmh(Integer windKmh) {
    this.windKmh = windKmh;
  }

  public Double getHumidityPct() {
    return humidityPct;
  }

  public void setHumidityPct(Double humidityPct) {
    this.humidityPct = humidityPct;
  }

  public Integer getPressureHPa() {
    return pressureHPa;
  }

  public void setPressureHPa(Integer pressureHPa) {
    this.pressureHPa = pressureHPa;
  }

}