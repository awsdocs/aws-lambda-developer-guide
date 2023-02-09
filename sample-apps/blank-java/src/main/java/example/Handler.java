package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String> {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    private static final LambdaClient lambdaClient = LambdaClient.builder()
        .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
        .build();

    @Override
    public String handleRequest(Map<String,String> event, Context context) {
        
        // log execution details
        logger.info("ENVIRONMENT VARIABLES: " + System.getenv());
        logger.info("EVENT: " + event);

        // process event
        GetAccountSettingsResponse response = null;
        try {
            response = lambdaClient.getAccountSettings();
        } catch(LambdaException e) {
            System.err.println(e.getMessage());
        }
        return response != null ? "Total code size for your account is " + response.accountLimit().totalCodeSize() + " bytes" : "Error";
    }
}
