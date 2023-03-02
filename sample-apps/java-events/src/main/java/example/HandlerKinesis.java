package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandleKinesis
public class HandlerKinesis implements RequestHandler<KinesisEvent, List<String>>{

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public List<String> handleRequest(KinesisEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var dataRecords = new ArrayList<String>();
    for(KinesisEventRecord record : event.getRecords()) {
      dataRecords.add(gson.toJson(record.getKinesis().getData()));
    }
    return dataRecords;
  }
}