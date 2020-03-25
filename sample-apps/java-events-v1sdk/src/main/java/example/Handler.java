package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String>{
  private static final Logger logger = LoggerFactory.getLogger(Handler.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(S3Event event, Context context)
  {
    String response = new String("200 OK");
    S3EventNotificationRecord record = event.getRecords().get(0);
    String srcBucket = record.getS3().getBucket().getName();
    // Object key may have spaces or unicode non-ASCII characters.
    String srcKey = record.getS3().getObject().getUrlDecodedKey();
    logger.info("RECORD: " + record);
    logger.info("SOURCE BUCKET: " + srcBucket);
    logger.info("SOURCE KEY: " + srcKey);
    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}