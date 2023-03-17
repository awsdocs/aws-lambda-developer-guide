package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent.Record;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerFirehose
public class HandlerFirehose implements RequestHandler<KinesisFirehoseEvent, List<String>>{

  @Override
  public List<String> handleRequest(KinesisFirehoseEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var recordIds = new ArrayList<String>();
    for (Record record : event.getRecords()) {
      recordIds.add(record.getRecordId());
    }
    return recordIds;
  }
}