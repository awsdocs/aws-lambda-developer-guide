# Step 3: Test the Lambda Function \(Invoke Manually\)<a name="vpc-rds-invoke-lambda-function"></a>

In this step, you invoke the Lambda function manually using the `invoke` command\. When the Lambda function executes, it runs the SELECT query against the Employee table in the RDS MySQL instance and prints the results \(these results also go to the CloudWatch Logs\)\.

1. Invoke the Lambda function \(`ReadMySqlTable`\) using the AWS Lambda `invoke` command\. 

   ```
   $ aws lambda invoke \
   --function-name CreateTableAddRecordsAndRead  \
   --region us-east-1 \
   --profile adminuser \
   output.txt
   ```

1. Verify that the Lambda function executed successfully as follows:
   + Review the output\.txt file\.
   + Review the results in the AWS Lambda console\.
   + Verify the results in CloudWatch Logs\.