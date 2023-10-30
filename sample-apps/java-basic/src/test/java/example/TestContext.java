package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestContext implements Context{

  public TestContext() {}
  public String getAwsRequestId(){
    return "495b12a8-xmpl-4eca-8168-160484189f99";
  }
  public String getLogGroupName(){
    return "/aws/lambda/my-function";
  }
  public String getLogStreamName(){
    return "2020/02/26/[$LATEST]704f8dxmpla04097b9134246b8438f1a";
  }
  public String getFunctionName(){
    return "my-function";
  }
  public String getFunctionVersion(){
    return "$LATEST";
  }
  public String getInvokedFunctionArn(){
    return "arn:aws:lambda:us-east-2:123456789012:function:my-function";
  }
  public CognitoIdentity getIdentity(){
    return null;
  }
  public ClientContext getClientContext(){
    return null;
  }
  public int getRemainingTimeInMillis(){
    return 300000;
  }
  public int getMemoryLimitInMB(){
    return 512;
  }
  public LambdaLogger getLogger(){
    return new TestLogger();
  }

}