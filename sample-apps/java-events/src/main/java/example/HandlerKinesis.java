package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandleKinesis
public class HandlerKinesis implements RequestHandler<KinesisEvent, List<String>>{

  private static final Logger logger = LoggerFactory.getLogger(HandlerKinesis.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public List<String> handleRequest(KinesisEvent event, Context context)
  {
    logger.info("EVENT TYPE: " + event.getClass().toString());
    var dataRecords = new ArrayList<String>();
    for(KinesisEventRecord record : event.getRecords()) {
      dataRecords.add(gson.toJson(record.getKinesis().getData()));
    }
    return dataRecords;
  }
}