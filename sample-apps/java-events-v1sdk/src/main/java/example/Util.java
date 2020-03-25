package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
  private static final Logger logger = LoggerFactory.getLogger(Util.class);

  public static void logEnvironment(Object event, Context context, Gson gson)
  {
    // log execution details
    logger.info("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.info("CONTEXT: " + gson.toJson(context));
    // log event details
    logger.info("EVENT: " + gson.toJson(event));
    logger.info("EVENT TYPE: " + event.getClass().toString());
  }
}