package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String> {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    private static final Region region = Region.US_EAST_1;
    private static final LambdaClient awsLambda = LambdaClient.builder()
        .region(region)
        .build();

    @Override
    public String handleRequest(Map<String,String> event, Context context) {
        
        // log execution details
        logger.info("ENVIRONMENT VARIABLES: " + System.getenv());
        logger.info("EVENT: " + event);

        // process event
        GetAccountSettingsResponse response;
        try {
            response = awsLambda.getAccountSettings();
            System.out.println("Total code size for your account is " + response.accountLimit().totalCodeSize() + " bytes");
        } catch(LambdaException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "200 OK";
    }
}
