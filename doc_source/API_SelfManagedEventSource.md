# SelfManagedEventSource<a name="API_SelfManagedEventSource"></a>

The self\-managed Apache Kafka cluster for your event source\.

## Contents<a name="API_SelfManagedEventSource_Contents"></a>

 ** Endpoints **   <a name="SSS-Type-SelfManagedEventSource-Endpoints"></a>
The list of bootstrap servers for your Kafka brokers in the following format: `"KAFKA_BOOTSTRAP_SERVERS": ["abc.xyz.com:xxxx","abc2.xyz.com:xxxx"]`\.  
Type: String to array of strings map  
Map Entries: Maximum number of 2 items\.  
Valid Keys:` KAFKA_BOOTSTRAP_SERVERS`   
Array Members: Minimum number of 1 item\. Maximum number of 10 items\.  
Length Constraints: Minimum length of 1\. Maximum length of 300\.  
Pattern: `^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9]):[0-9]{1,5}`   
Required: No

## See Also<a name="API_SelfManagedEventSource_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/SelfManagedEventSource) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/SelfManagedEventSource) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/SelfManagedEventSource) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/SelfManagedEventSource) 