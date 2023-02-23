package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent.CF;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent.Record;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerCloudFront
public class HandlerCloudFront implements RequestHandler<CloudFrontEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerCloudFront.class);

  @Override
  public List<String> handleRequest(CloudFrontEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var urisFound = new ArrayList<String>();
    for (Record record : event.getRecords()) {
      CF cfBody = record.getCf();
      urisFound.add(cfBody.getRequest().getUri());
    }
    return urisFound;
  }
}