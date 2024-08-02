package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

class InvokeTest {
  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

  @Test
  void testHandler() {
    logger.info("Invoke TEST - Handler");
    var event = new HashMap<String,String>();
    Context context = new TestContext();
    Handler handler = new Handler();
    assertNull(handler.handleRequest(event, context));
  }

  @Test
  void testHandlerDivide() {
    logger.info("Invoke TEST - HandlerDivide");
    var event = List.of(20, 5);
    Context context = new TestContext();
    HandlerDivide handler = new HandlerDivide();
    assertEquals(4, handler.handleRequest(event, context));
  }

  @Test
  void testHandlerInteger() {
    logger.info("Invoke TEST - HandlerInteger");
    Integer event = 1;
    Context context = new TestContext();
    HandlerInteger handler = new HandlerInteger();
    assertEquals(2, handler.handleRequest(event, context));
  }

  @Test
  void testHandlerList() {
    logger.info("Invoke TEST - HandlerList");
    var event = List.of(1, 2, 3, 4);
    Context context = new TestContext();
    HandlerList handler = new HandlerList();
    assertEquals(10, handler.handleRequest(event, context));
  }

  @Test
    public void testHandlerStream() throws IOException {
        HandlerStream orderProcessor = new HandlerStream();
        String input = """
                {
                    "orderId": "123",
                    "items": [
                        {
                            "name": "widgets",
                            "quantity": 10
                        }
                    ]
                }
                """;
        Context context = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        orderProcessor.handleRequest(inputStream, outputStream, context);

        assertEquals("{\"orderId\":\"123\"}", outputStream.toString());
    }

  @Test
  void testHandlerString() {
    logger.info("Invoke TEST - HandlerString");
    String event = "HeLlO wOrLd";
    Context context = new TestContext();
    HandlerString handler = new HandlerString();
    assertEquals("hello world", handler.handleRequest(event, context));
  }

  @Test
  void testHandlerWeatherData() {
    logger.info("Invoke TEST - HandlerWeatherData");
    WeatherData inputData = new WeatherData();
    inputData.setTemperatureK(298);
    Context context = new TestContext();
    HandlerWeatherData handler = new HandlerWeatherData();
    WeatherData outputData = handler.handleRequest(inputData, context);
    assertNotNull(outputData.getHumidityPct());
    assertNotNull(outputData.getPressureHPa());
    assertNotNull(outputData.getWindKmh());
    assertEquals(300, outputData.getTemperatureK());
  }
}
