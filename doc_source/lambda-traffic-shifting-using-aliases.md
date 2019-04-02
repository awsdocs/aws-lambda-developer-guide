# Traffic Shifting Using Aliases<a name="lambda-traffic-shifting-using-aliases"></a>

By default, an alias points to a single Lambda function version\. When the alias is updated to point to a different function version, incoming request traffic in turn instantly points to the updated version\. This exposes that alias to any potential instabilities introduced by the new version\. To minimize this impact, you can implement the `routing-config` parameter of the Lambda alias that allows you to point to two different versions of the Lambda function and dictate what percentage of incoming traffic is sent to each version\.

For example, you can specify that only 2 percent of incoming traffic is routed to the new version while you analyze its readiness for a production environment, while the remaining 98 percent is routed to the original version\. As the new version matures, you can gradually update the ratio as necessary until you have determined the new version is stable\. You can then update the alias to route all traffic to the new version\. 

You can point an alias to a maximum of two Lambda function versions\. In addition: 
+ Both versions must have the same IAM execution role\.
+ Both versions must have the same [AWS Lambda Function Dead Letter Queues](dlq.md) configuration, or no DLQ configuration\.
+ When pointing an alias to more than one version, the alias cannot point to `$LATEST`\.

## Traffic Shifting Using an Alias \(CLI\)<a name="lambda-weighted-aliases-cli"></a>

To configure an alias to shift traffic between two function versions based on weights by using the [CreateAlias](API_CreateAlias.md) operation, you need to configure the `routing-config` parameter\. The example following points an alias to two different Lambda function versions, with version 2 receiving 2 percent of the invocation traffic and the remaining 98 percent invoking version 1\. 

```
aws lambda create-alias --name alias name --function-name function-name \ --function-version 1
--routing-config AdditionalVersionWeights={"2"=0.02}
```

You can update the percentage of incoming traffic to your new version \(version 2\) by using the [UpdateAlias](API_UpdateAlias.md) operation\. For example, you can boost the invocation traffic to your new version to 5 percent, as shown following\.

```
aws lambda update-alias --name alias name --function-name function-name \
--routing-config AdditionalVersionWeights={"2"=0.05}
```

To route all traffic to version 2, again use the `UpdateAlias` operation to change the `function-version` property to point to version 2\. In the same command, reset the routing configuration\.

```
aws lambda update-alias --name alias name --function-name function-name \ 
--function-version 2 --routing-config AdditionalVersionWeights={}
```

## Traffic Shifting Using an Alias \(Console\)<a name="lambda-traffic-shifting-aliases-console"></a>

You can configure traffic shifting with an alias by using the Lambda console as described below:

1. Open your Lambda function and verify that you have at least two previously published versions\. Otherwise, you can go to [Introduction to AWS Lambda Versioning](versioning-intro.md) to learn more about versioning, and publish your first function version\.

1. For **Actions**, choose **Create alias**\.

1. In the **Create a new alias** window, specify a value for **Name\***, optionally for **Description**, and for **Version\*** of the Lambda function that the alias will point to\. Here the version is `1`\.

1. Under **Additional version**, specify the following:

   1. Specify a second Lambda function version\.

   1. Type a weight value for the function\. *Weight* is the percentage of traffic that is assigned to that version when the alias is invoked\. The first version receives the residual weight\. For example, if you specify 10 percent to **Additional version**, the first version automatically is assigned 90 percent\.

1. Choose **Create**\.

## Determining Which Version Has Been Invoked<a name="lambda-traffic-shifting-analysis"></a>

When your alias is shifting traffic between two function versions, there are two ways to determine which Lambda function version has been invoked:

1. **CloudWatch Logs** – Lambda automatically emits a `START` log entry that contains the invoked version ID to CloudWatch Logs for every function invocation\. An example follows\.

   `19:44:37 START RequestId: request id Version: $version ` 

   Lambda uses the `Executed Version` dimension to filter the metric data by the executed version\. This only applies to alias invocations\. For more information, see [AWS Lambda CloudWatch Dimensions](monitoring-functions-metrics.md#lambda-cloudwatch-dimensions)\.

1. **Response payload \(synchronous invocations\)** – Responses to synchronous function invocations include an `x-amz-executed-version` header to indicate which function version has been invoked\.