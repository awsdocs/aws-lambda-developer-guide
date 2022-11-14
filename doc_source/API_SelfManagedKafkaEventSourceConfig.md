# SelfManagedKafkaEventSourceConfig<a name="API_SelfManagedKafkaEventSourceConfig"></a>

Specific configuration settings for a self\-managed Apache Kafka event source\.

## Contents<a name="API_SelfManagedKafkaEventSourceConfig_Contents"></a>

 ** ConsumerGroupId **   <a name="SSS-Type-SelfManagedKafkaEventSourceConfig-ConsumerGroupId"></a>
The identifier for the Kafka consumer group to join\. The consumer group ID must be unique among all your Kafka event sources\. After creating a Kafka event source mapping with the consumer group ID specified, you cannot update this value\. For more information, see [Customizable consumer group ID](https://docs.aws.amazon.com/lambda/latest/dg/with-msk.html#services-msk-consumer-group-id)\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 200\.  
Pattern: `[a-zA-Z0-9-\/*:_+=.@-]*`   
Required: No

## See Also<a name="API_SelfManagedKafkaEventSourceConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/SelfManagedKafkaEventSourceConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/SelfManagedKafkaEventSourceConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/SelfManagedKafkaEventSourceConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/SelfManagedKafkaEventSourceConfig) 