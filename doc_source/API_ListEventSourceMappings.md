# ListEventSourceMappings<a name="API_ListEventSourceMappings"></a>

Lists event source mappings\. Specify an `EventSourceArn` to show only event source mappings for a single event source\.

## Request Syntax<a name="API_ListEventSourceMappings_RequestSyntax"></a>

```
GET /2015-03-31/event-source-mappings/?EventSourceArn=EventSourceArn&FunctionName=FunctionName&Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListEventSourceMappings_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [EventSourceArn](#API_ListEventSourceMappings_RequestSyntax) **   <a name="SSS-ListEventSourceMappings-request-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
+  **Amazon Kinesis** \- The ARN of the data stream or a stream consumer\.
+  **Amazon DynamoDB Streams** \- The ARN of the stream\.
+  **Amazon Simple Queue Service** \- The ARN of the queue\.
+  **Amazon Managed Streaming for Apache Kafka** \- The ARN of the cluster\.
+  **Amazon MQ** \- The ARN of the broker\.
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionName](#API_ListEventSourceMappings_RequestSyntax) **   <a name="SSS-ListEventSourceMappings-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Version or Alias ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction:PROD`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it's limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Marker](#API_ListEventSourceMappings_RequestSyntax) **   <a name="SSS-ListEventSourceMappings-request-Marker"></a>
A pagination token returned by a previous call\.

 ** [MaxItems](#API_ListEventSourceMappings_RequestSyntax) **   <a name="SSS-ListEventSourceMappings-request-MaxItems"></a>
The maximum number of event source mappings to return\. Note that ListEventSourceMappings returns a maximum of 100 items in each response, even if you set the number higher\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListEventSourceMappings_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListEventSourceMappings_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "EventSourceMappings": [ 
      { 
         "AmazonManagedKafkaEventSourceConfig": { 
            "ConsumerGroupId": "string"
         },
         "BatchSize": number,
         "BisectBatchOnFunctionError": boolean,
         "DestinationConfig": { 
            "OnFailure": { 
               "Destination": "string"
            },
            "OnSuccess": { 
               "Destination": "string"
            }
         },
         "EventSourceArn": "string",
         "FilterCriteria": { 
            "Filters": [ 
               { 
                  "Pattern": "string"
               }
            ]
         },
         "FunctionArn": "string",
         "FunctionResponseTypes": [ "string" ],
         "LastModified": number,
         "LastProcessingResult": "string",
         "MaximumBatchingWindowInSeconds": number,
         "MaximumRecordAgeInSeconds": number,
         "MaximumRetryAttempts": number,
         "ParallelizationFactor": number,
         "Queues": [ "string" ],
         "SelfManagedEventSource": { 
            "Endpoints": { 
               "string" : [ "string" ]
            }
         },
         "SelfManagedKafkaEventSourceConfig": { 
            "ConsumerGroupId": "string"
         },
         "SourceAccessConfigurations": [ 
            { 
               "Type": "string",
               "URI": "string"
            }
         ],
         "StartingPosition": "string",
         "StartingPositionTimestamp": number,
         "State": "string",
         "StateTransitionReason": "string",
         "Topics": [ "string" ],
         "TumblingWindowInSeconds": number,
         "UUID": "string"
      }
   ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListEventSourceMappings_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [EventSourceMappings](#API_ListEventSourceMappings_ResponseSyntax) **   <a name="SSS-ListEventSourceMappings-response-EventSourceMappings"></a>
A list of event source mappings\.  
Type: Array of [EventSourceMappingConfiguration](API_EventSourceMappingConfiguration.md) objects

 ** [NextMarker](#API_ListEventSourceMappings_ResponseSyntax) **   <a name="SSS-ListEventSourceMappings-response-NextMarker"></a>
A pagination token that's returned when the response doesn't contain all event source mappings\.  
Type: String

## Errors<a name="API_ListEventSourceMappings_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_ListEventSourceMappings_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListEventSourceMappings) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListEventSourceMappings) 