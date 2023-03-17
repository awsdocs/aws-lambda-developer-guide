package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent.CodeCommit;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent.Record;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent.Reference;

import java.util.ArrayList;
import java.util.List;

// Handler value: example.HandlerCodeCommit
public class HandlerCodeCommit implements RequestHandler<CodeCommitEvent, List<String>>{

  @Override
  public List<String> handleRequest(CodeCommitEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT TYPE: " + event.getClass().toString());
    var commitsFound = new ArrayList<String>();
    for (Record record : event.getRecords()) {
      CodeCommit commit = record.getCodeCommit();
      for (Reference reference : commit.getReferences()) {
        commitsFound.add(reference.getCommit());
      }
    }
    return commitsFound;
  }
}