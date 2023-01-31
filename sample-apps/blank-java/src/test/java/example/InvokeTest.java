package example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;

class InvokeTest {

  public InvokeTest() {
      AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
      builder.withSamplingStrategy(new NoSamplingStrategy());
      AWSXRay.setGlobalRecorder(builder.build());
  }

  @Test
  void invokeTest() {
      AWSXRay.beginSegment("blank-java-test");
      String path = "src/test/resources/event.json";
      String eventString = loadJsonFile(path);

      ObjectMapper mapper = new ObjectMapper();
      TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
      Map<String, String> event = new HashMap<>();
      try {
          event = mapper.readValue(eventString, typeRef);
      } catch (JsonMappingException e) {
          e.printStackTrace();
      } catch (JsonProcessingException e) {
          e.printStackTrace();
      }
      Context context = new TestContext();
      Handler handler = new Handler();
      String result = handler.handleRequest(event, context);
      assertTrue(result.contains("200 OK"));
      AWSXRay.endSegment();
  }

  private static String loadJsonFile(String path)
  {
      StringBuilder stringBuilder = new StringBuilder();
      try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
      {
          stream.forEach(s -> stringBuilder.append(s));
      }
      catch (IOException e)
      {
          e.printStackTrace();
      }
      return stringBuilder.toString();
  }
}
