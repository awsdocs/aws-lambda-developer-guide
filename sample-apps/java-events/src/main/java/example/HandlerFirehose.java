package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent.Record;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandlerFirehose
public class HandlerFirehose implements RequestHandler<KinesisFirehoseEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerFirehose.class);

  @Override
  public List<String> handleRequest(KinesisFirehoseEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var recordIds = new ArrayList<String>();
    for (Record record : event.getRecords()) {
      recordIds.add(record.getRecordId());
    }
    return recordIds;
  }
}