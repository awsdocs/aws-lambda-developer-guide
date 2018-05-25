# Step 3: Invoke the Lambda Function \(AWS CLI\)<a name="with-userapp-walkthrough-custom-events-invoke"></a>

In this section, you invoke your Lambda function manually using the invoke AWS CLI command\. 

```
$ aws lambda invoke \
--invocation-type RequestResponse \
--function-name helloworld \
--region region \
--log-type Tail \
--payload '{"key1":"value1", "key2":"value2", "key3":"value3"}' \
--profile adminuser \
outputfile.txt
```

If you want you can save the payload to a file \(for example, `input.txt`\) and provide the file name as a parameter\.

```
--payload file://input.txt \
```

The preceding `invoke` command specifies `RequestResponse` as the invocation type, which returns a response immediately in response to the function execution\. Alternatively, you can specify `Event` as the invocation type to invoke the function asynchronously\. 

By specifying the `--log-type` parameter, the command also requests the tail end of the log produced by the function\. The log data in the response is base64\-encoded as shown in the following example response:

```
{
     "LogResult": "base64-encoded-log",
     "StatusCode": 200 
}
```

On Linux and Mac, you can use the base64 command to decode the log\.

```
$ echo base64-encoded-log | base64 --decode
```

The following is a decoded version of an example log\.

```
START RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value1 = value1
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value2 = value2
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value3 = value3
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    result: "value1"
END RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801
REPORT RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801       
Duration: 13.35 ms      Billed Duration: 100 ms   Memory Size: 128 MB  
Max Memory Used: 9 MB
```

For more information, see [Invoke](API_Invoke.md)\. 

Because you invoked the function using the `RequestResponse` invocation type, the function executes and returns the object you passed to the `context.succeed()` in real time when it is called\. In this tutorial, you see the following text written to the `outputfile.txt` you specified in the CLI command:

```
"value1"
```

**Note**  
You are able to execute this function because you are using the same AWS account to create and invoke the Lambda function\. However, if you want to grant cross\-account permissions to another AWS account or grant permissions to another an AWS service to execute the function, you must add a permissions to the access permissions policy associated with the function\. The Amazon S3 tutorial, which uses Amazon S3 as the event source \(see [Tutorial: Using AWS Lambda with Amazon S3](with-s3-example.md)\), grants such permissions to Amazon S3 to invoke the function\.

You can monitor the activity of your Lambda function in the AWS Lambda console\. 
+ Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

  The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\.
+ For each graph, you can also choose the **logs** link to view the CloudWatch logs directly\.

## Next Step<a name="with-userapp-walkthrough-custom-events-invoke-next-step"></a>

 [Step 4: Try More CLI Commands \(AWS CLI\)](with-userapp-walkthrough-custom-events-try-more-api.md) 