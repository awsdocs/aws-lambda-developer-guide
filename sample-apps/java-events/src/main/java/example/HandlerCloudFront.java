package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent.CF;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent.Record;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerCloudFront
public class HandlerCloudFront implements RequestHandler<CloudFrontEvent, List<String>>{

  @Override
  public List<String> handleRequest(CloudFrontEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var urisFound = new ArrayList<String>();
    for (Record record : event.getRecords()) {
      CF cfBody = record.getCf();
      urisFound.add(cfBody.getRequest().getUri());
    }
    return urisFound;
  }
}