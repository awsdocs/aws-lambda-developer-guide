package example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;

import java.util.Map;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;

class InvokeTest {

  public InvokeTest() {
      AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
      builder.withSamplingStrategy(new NoSamplingStrategy());
      AWSXRay.setGlobalRecorder(builder.build());
  }

  @ParameterizedTest
  @Event(value = "event.json", type = Map.class)
  void invokeTest(Map<String, String> event) {
      AWSXRay.beginSegment("blank-java-test");
      Context context = new TestContext();
      Handler handler = new Handler();
      String result = handler.handleRequest(event, context);
      assertTrue(result.contains("Total code size for your account"));
      AWSXRay.endSegment();
  }
}
