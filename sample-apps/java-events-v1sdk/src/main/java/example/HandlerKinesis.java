package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

import com.amazonaws.services.kinesis.model.Record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.HandleKinesis
public class HandlerKinesis implements RequestHandler<KinesisEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerKinesis.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(KinesisEvent event, Context context)
  {
    String response = new String("200 OK");
    for(KinesisEventRecord record : event.getRecords()) {
      logger.info(gson.toJson(record.getKinesis().getData()));
    }
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}