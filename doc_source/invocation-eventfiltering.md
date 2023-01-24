# Lambda event filtering<a name="invocation-eventfiltering"></a>

You can use event filtering to control which events Lambda sends to your function for processing\. For example, you can define filter criteria so that you process only the records from a Kinesis stream that have the status code `ERROR`\.

You can define up to five different filters for a single event source\. If an event satisfies any one of these five filters, Lambda sends the event to your function\. Otherwise, Lambda discards the event\. An event either satisfies the filter criteria or it doesn't\. If you're using batching windows, Lambda applies your filter criteria to each new event to determine whether to add it to the current batch\.

**Topics**
+ [Event filtering basics](#filtering-basics)
+ [Filter rule syntax](#filtering-syntax)
+ [Filtering examples](#filtering-examples)
+ [Attaching filter criteria to an event source mapping \(console\)](#filtering-console)
+ [Attaching filter criteria to an event source mapping \(AWS CLI\)](#filtering-cli)
+ [Properly filtering Amazon SQS messages](#filtering-sqs)
+ [Properly filtering Kinesis and DynamoDB messages](#filtering-streams)
+ [Properly filtering Amazon Managed Streaming for Apache Kafka, self\-managed Apache Kafka, and Amazon MQ messages](#filtering-poller)

## Event filtering basics<a name="filtering-basics"></a>

A filter criteria \(`FilterCriteria`\) object is a structure that consists of a list of filters \(`Filters`\)\. Each filter \(`Filter`\) is a structure that defines an event filtering pattern \(`Pattern`\)\. A `Pattern` is a string representation of a JSON filter rule\. A `FilterCriteria` object looks like the following example:

```
{
   "Filters": [
        {
            "Pattern": "{ \"Metadata1\": [ rule1 ], \"data\": { \"Data1\": [ rule2 ] }}"
        }
    ]
}
```

For added clarity, here is the value of the filter's `Pattern` expanded in plain JSON:

```
{
    "Metadata1": [ pattern1 ],
    "data": {
        "Data1": [ pattern2 ]
    }
}
```

There are three main parts to a `FilterCriteria` object: metadata properties, data properties, and filter patterns\. For example, suppose you receive a Kinesis event from your event source that looks like the following:

```
"kinesis": {
    "partitionKey": "1",
    "sequenceNumber": "49590338271490256608559692538361571095921575989136588898",
    "data": {
        "City": "Seattle",
        "State": "WA",
        "Temperature": "46",
        "Month": "December"
    },
    "approximateArrivalTimestamp": 1545084650.987
}
```
+ **Metadata properties** are the fields of the event object\. In the example `FilterCriteria`, `Metadata1` refers to a metadata property\. In the Kinesis event example, `Metadata1` could refer to a field such as `partitionKey`\.
+ **Data properties** are the fields of the event body\. In the example `FilterCriteria`, `Data1` refers to a data property\. In the Kinesis event example, `Data1` could refer to fields such as `City` and `Temperature`\.
**Note**  
To filter on data properties, make sure to contain them in `FilterCriteria` within the proper key\. This key depends on the event source\. For Kinesis event sources, the data key is `data`\. For Amazon SQS event sources, the data key is `body`\. For DynamoDB event sources, the data key is `dynamodb`\.
+ **Filter rules** define the filter that you want to apply to a specific property\. In the example `FilterCriteria`, `rule1` applies to `Metadata1`, and `rule2` applies to `Data1`\. The syntax of your filter rule depends on the comparison operator that you use\. For more information, see [Filter rule syntax](#filtering-syntax)\.

When you create a `FilterCriteria` object, specify only the metadata properties and data properties that you want the filter to match on\. For Lambda to consider the event a match, the event must contain all the field names included in a filter\. Lambda ignores the fields that aren't included in a filter\.

### Duplicate keys<a name="filtering-duplicate-keys"></a>

If a pattern contains duplicate keys with different values, Lambda uses only the last value for that key\. For example, here's a JSON filter pattern that contains multiple values for `"duplicateKey"`:

```
{
  "Metadata1": [ "pattern1" ],
  "data": {
      "duplicateKey" : ["hello"],
      "uniqueKey" : ["test123"],
      "duplicateKey" : ["world"] 
  }
}
```

In this scenario, Lambda interprets the filter rule as follows:

```
{
  "Metadata1": [ "pattern1" ],
  "data": {
      "uniqueKey" : ["test123"],
      "duplicateKey" : ["world"] 
  }
}
```

## Filter rule syntax<a name="filtering-syntax"></a>

For filter rules, Lambda supports the same set of syntax and rules as Amazon EventBridge\. For more information, see [Amazon EventBridge event patterns](https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-event-patterns.html) in the *Amazon EventBridge User Guide*\.

The following is a summary of all the comparison operators available for Lambda event filtering\.


| Comparison operator | Example | Rule syntax | 
| --- | --- | --- | 
|  Null  |  UserID is null  |  "UserID": \[ null \]  | 
|  Empty  |  LastName is empty  |  "LastName": \[""\]  | 
|  Equals  |  Name is "Alice"  |  "Name": \[ "Alice" \]  | 
|  And  |  Location is "New York" and Day is "Monday"  |  "Location": \[ "New York" \], "Day": \["Monday"\]  | 
|  Or  |  PaymentType is "Credit" or "Debit"  |  "PaymentType": \[ "Credit", "Debit"\]  | 
|  Not  |  Weather is anything but "Raining"  |  "Weather": \[ \{ "anything\-but": \[ "Raining" \] \} \]  | 
|  Numeric \(equals\)  |  Price is 100  |  "Price": \[ \{ "numeric": \[ "=", 100 \] \} \]  | 
|  Numeric \(range\)  |  Price is more than 10, and less than or equal to 20  |  "Price": \[ \{ "numeric": \[ ">", 10, "<=", 20 \] \} \]  | 
|  Exists  |  ProductName exists  |  "ProductName": \[ \{ "exists": true \} \]  | 
|  Does not exist  |  ProductName does not exist  |  "ProductName": \[ \{ "exists": false \} \]  | 
|  Begins with  |  Region is in the US  |  "Region": \[ \{"prefix": "us\-" \} \]  | 

**Note**  
Like EventBridge, for strings, Lambda uses exact character\-by\-character matching without case\-folding or any other string normalization\. For numbers, Lambda also uses string representation\. For example, 300, 300\.0, and 3\.0e2 are not considered equal\.

## Filtering examples<a name="filtering-examples"></a>

Suppose you have a Kinesis event source, and you want your function to handle only events with a specific `partitionKey` \(a metadata property\)\. In addition, you want to process only events with the `Location` field \(a data property\) equal to "Los Angeles"\. In this case, your `FilterCriteria` object would look like this:

```
{
   "Filters": [
        {
            "Pattern": "{ \"partitionKey\": [ \"1\" ], \"data\": { \"Location\": [ \"Los Angeles\" ] }}"
        }
    ]
}
```

For added clarity, here is the value of the filter's `Pattern` expanded in plain JSON\.

```
{
    "partitionKey": [ "1" ],
    "data": {
        "Location": [ "Los Angeles" ]
    }
}
```

The previous example uses the **Equals** comparison operator for both `partitionKey` and `Location`\.

As another example, suppose that you want to handle only events where the `Temperature` data property is greater than 50 but less than or equal to 60\. In this case, your `FilterCriteria` object would look like this:

```
{
   "Filters": [
        {
            "Pattern": "{ \"data\": { \"Temperature\": [ {\"numeric\": [ \">\", 50, \"<=\", 60 ] }]}"
        }
    ]
}
```

For added clarity, here is the value of the filter's `Pattern` expanded in plain JSON\.

```
{
    "data": {
        "Temperature": [ {"numeric": [ ">", 50, "<=", 60 ] } ]
    }
}
```

The previous example uses the **Numeric \(range\)** comparison operator for `Temperature`\.

### Multi\-level filtering<a name="multi-level-filtering"></a>

You can also use event filtering to handle multi\-level JSON filtering\. For example, suppose you receive a DynamoDB stream event with a data object that looks like the following:

```
"dynamodb": {
    "Keys": {
        "Id": {
            "N": "101"
        }
    },
    "NewImage": {
        "Message": {
            "S": "New item!"
        },
        "Id": {
            "N": "101"
        }
    },
    "SequenceNumber": "111",
    "SizeBytes": 26,
    "StreamViewType": "NEW_AND_OLD_IMAGES"
}
```

Suppose you only wanted to handle events where the Key ID value, `N`, is 101\. In this case, your `FilterCriteria` object would look like this:

```
{
   "Filters": [
        {
            "Pattern": "{ \"dynamodb\": { \"Keys\": { \"Id\": { \"N\": [ "101" ] } } } }"
        }
    ]
}
```

For added clarity, here is the value of the filter's `Pattern` expanded in plain JSON\.

```
{
    "dynamodb": {
        "Keys": {
            "Id": {
                "N": [ "101" ]
            }
        }
    }
}
```

The previous example uses the **Equals** comparison operator for `N`, which is nested multiple layers within the `dynamodb` data field\.

## Attaching filter criteria to an event source mapping \(console\)<a name="filtering-console"></a>

Follow these steps to create a new event source mapping with filter criteria using the Lambda console\.

**To create a new event source mapping with filter criteria \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of a function to create an event source mapping for\.

1. Under **Function overview**, choose **Add trigger**\.

1. For **Trigger configuration**, choose a trigger type that supports event filtering\. These include **SQS**, **DynamoDB**, **Kinesis**, **MSK**, and **MQ**\.

1. Expand **Additional settings**\.

1. Under **Filter criteria**, choose **Add**, and then define and enter your filters\. For example, you can enter the following:

   ```
   { "a" : [ 1, 2 ] }
   ```

   This instructs Lambda to process only the records where field `a` is equal to 1 or 2\.

1. Choose **Add**\.

When you enter filter criteria using the console, you provide only the filter pattern\. In step 6 of the preceding instructions, `{ "a" : [ 1, 2 ] }` corresponds to the following `FilterCriteria`:

```
{
   "Filters": [
      {
          "Pattern": "{ \"a\" : [ 1, 2 ] }"
      }
   ]
}
```

After creating your event source mapping in the console, you can see the formatted `FilterCriteria` in the trigger details\. Note that when entering filters using the console, you don't need to provide the `Pattern` key or escape quotes\.

**Note**  
By default, you can have five different filters per event source\. You can [request a quota increase](https://docs.aws.amazon.com/servicequotas/latest/userguide/request-quota-increase.html) for up to 10 filters per event source\. The Lambda console lets you add up to 10 filters depending on the current quota for your account\. If you attempt to add more filters than your current quota allows, Lambda throws an error when you try to create the event source\.

## Attaching filter criteria to an event source mapping \(AWS CLI\)<a name="filtering-cli"></a>

Suppose you want an event source mapping to have the following `FilterCriteria`:

```
{
   "Filters": [
      {
          "Pattern": "{ \"a\" : [ 1, 2 ] }"
      }
   ]
}
```

To create a new event source mapping with these filter criteria using the AWS Command Line Interface \(AWS CLI\), run the following command:

```
aws lambda create-event-source-mapping \
    --function-name my-function \
    --event-source-arn arn:aws:sqs:us-east-2:123456789012:my-queue \
    --filter-criteria "{\"Filters\": [{\"Pattern\": \"{ \"a\" : [ 1, 2 ]}\"}]}"
```

This [CreateEventSourceMapping](API_CreateEventSourceMapping.md) command creates a new Amazon SQS event source mapping for function `my-function` with the specified `FilterCriteria`\.

To add these filter criteria to an existing event source mapping, run the following command:

```
aws lambda update-event-source-mapping \
    --uuid "a1b2c3d4-5678-90ab-cdef-11111EXAMPLE" \
    --filter-criteria "{\"Filters\": [{\"Pattern\": \"{ \"a\" : [ 1, 2 ]}\"}]}"
```

Note that to update an event source mapping, you need its UUID\. You can get the UUID from a [ListEventSourceMappings](API_ListEventSourceMappings.md) call\. Lambda also returns the UUID in the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API response\.

To remove filter criteria from an event source, you can run the following [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) command with an empty `FilterCriteria` object:

```
aws lambda update-event-source-mapping \
    --uuid "a1b2c3d4-5678-90ab-cdef-11111EXAMPLE" \
    --filter-criteria "{}"
```

## Properly filtering Amazon SQS messages<a name="filtering-sqs"></a>

If an Amazon SQS message doesn't satisfy your filter criteria, Lambda automatically removes the message from the queue\. You don't have to manually delete these messages in Amazon SQS\.

For Amazon SQS, the message `body` can be any string\. However, this can be problematic if your `FilterCriteria` expect `body` to be in a valid JSON format\. The reverse scenario is also true—if the incoming message `body` is in JSON format but your filter criteria expects `body` to be a plain string, this can lead to unintended behavior\.

To avoid this issue, ensure that the format of `body` in your `FilterCriteria` matches the expected format of `body` in messages that you receive from your queue\. Before filtering your messages, Lambda automatically evaluates the format of the incoming message `body` and of your filter pattern for `body`\. If there is a mismatch, Lambda drops the message\. The following table summarizes this evaluation:


| Incoming message `body` format | Filter pattern `body` format | Resulting action | 
| --- | --- | --- | 
|  Plain string  |  Plain string  |  Lambda filters based on your filter criteria\.  | 
|  Plain string  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Plain string  |  Valid JSON  |  Lambda drops the message\.  | 
|  Valid JSON  |  Plain string  |  Lambda drops the message\.  | 
|  Valid JSON  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Valid JSON  |  Valid JSON  |  Lambda filters based on your filter criteria\.  | 

If you don't include `body` as part of your `FilterCriteria`, Lambda skips this check\.

## Properly filtering Kinesis and DynamoDB messages<a name="filtering-streams"></a>

Once your filter criteria processes an Kinesis or DynamoDB record, the streams iterator advances past this record\. If the record doesn't satisfy your filter criteria, you don't have to manually delete the record from your event source\. After the retention period, Kinesis and DynamoDB automatically delete these old records\. If you want records to be deleted sooner, see [Changing the Data Retention Period](https://docs.aws.amazon.com/kinesis/latest/dev/kinesis-extended-retention.html)\.

To properly filter events from Kinesis and DynamoDB sources, both the data field and your filter criteria for the data field must be in valid JSON format\. \(For Kinesis, the data field is `data`\. For DynamoDB, the data field is `dynamodb`\.\) If either field isn't in a valid JSON format, Lambda drops the message or throws an exception\. The following table summarizes the specific behavior:


| Incoming data format \(`data` or `dynamodb`\) | Filter pattern format for data properties | Resulting action | 
| --- | --- | --- | 
|  Valid JSON  |  Valid JSON  |  Lambda filters based on your filter criteria\.  | 
|  Valid JSON  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Valid JSON  |  Non\-JSON  |  Lambda throws an exception at the time of the event source mapping creation or update\. The filter pattern for data properties must be in a valid JSON format\.  | 
|  Non\-JSON  |  Valid JSON  |  Lambda drops the record\.  | 
|  Non\-JSON  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Non\-JSON  |  Non\-JSON  |  Lambda throws an exception at the time of the event source mapping creation or update\. The filter pattern for data properties must be in a valid JSON format\.  | 

## Properly filtering Amazon Managed Streaming for Apache Kafka, self\-managed Apache Kafka, and Amazon MQ messages<a name="filtering-poller"></a>

**Note**  
After you attach filter criteria to a Kafka or Amazon MQ event source mapping, it can take up to 15 minutes to apply your filtering rules to events\.

For [Amazon MQ sources](with-mq.md), the message field is `data`\. For Kafka sources \([Amazon MSK](with-msk.md) and [self\-managed Apache Kafka](with-kafka.md)\), there are two message fields: `key` and `value`\.

Lambda drops messages that don't match all fields included in the filter\. For Kafka, Lambda commits offsets for matched and unmatched messages after successfully invoking the function\. For Amazon MQ, Lambda acknowledges matched messages after successfully invoking the function and acknowledges unmatched messages when filtering them\.

Kafka and Amazon MQ messages must be UTF\-8 encoded strings, either plain strings or in JSON format\. That's because Lambda decodes Kafka and Amazon MQ byte arrays into UTF\-8 before applying filter criteria\. If your messages use another encoding, such as UTF\-16 or ASCII, or if the message format doesn't match the `FilterCriteria` format, Lambda processes metadata filters only\. The following table summarizes the specific behavior:


| Incoming message format \(`data` or `key` and `value`\) | Filter pattern format for message properties | Resulting action | 
| --- | --- | --- | 
|  Plain string  |  Plain string  |  Lambda filters based on your filter criteria\.  | 
|  Plain string  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Plain string  |  Valid JSON  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Valid JSON  |  Plain string  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Valid JSON  |  No filter pattern for data properties  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 
|  Valid JSON  |  Valid JSON  |  Lambda filters based on your filter criteria\.  | 
|  Non\-UTF\-8 encoded string  |  JSON, plain string, or no pattern  |  Lambda filters \(on the other metadata properties only\) based on your filter criteria\.  | 