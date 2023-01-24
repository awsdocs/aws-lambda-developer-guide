# SourceAccessConfiguration<a name="API_SourceAccessConfiguration"></a>

To secure and define access to your event source, you can specify the authentication protocol, VPC components, or virtual host\.

## Contents<a name="API_SourceAccessConfiguration_Contents"></a>

 ** Type **   <a name="SSS-Type-SourceAccessConfiguration-Type"></a>
The type of authentication protocol, VPC components, or virtual host for your event source\. For example: `"Type":"SASL_SCRAM_512_AUTH"`\.  
+  `BASIC_AUTH` \- \(Amazon MQ\) The AWS Secrets Manager secret that stores your broker credentials\.
+  `BASIC_AUTH` \- \(Self\-managed Apache Kafka\) The Secrets Manager ARN of your secret key used for SASL/PLAIN authentication of your Apache Kafka brokers\.
+  `VPC_SUBNET` \- \(Self\-managed Apache Kafka\) The subnets associated with your VPC\. Lambda connects to these subnets to fetch data from your self\-managed Apache Kafka cluster\.
+  `VPC_SECURITY_GROUP` \- \(Self\-managed Apache Kafka\) The VPC security group used to manage access to your self\-managed Apache Kafka brokers\.
+  `SASL_SCRAM_256_AUTH` \- \(Self\-managed Apache Kafka\) The Secrets Manager ARN of your secret key used for SASL SCRAM\-256 authentication of your self\-managed Apache Kafka brokers\.
+  `SASL_SCRAM_512_AUTH` \- \(Amazon MSK, Self\-managed Apache Kafka\) The Secrets Manager ARN of your secret key used for SASL SCRAM\-512 authentication of your self\-managed Apache Kafka brokers\.
+  `VIRTUAL_HOST` \- \(RabbitMQ\) The name of the virtual host in your RabbitMQ broker\. Lambda uses this RabbitMQ host as the event source\. This property cannot be specified in an UpdateEventSourceMapping API call\.
+  `CLIENT_CERTIFICATE_TLS_AUTH` \- \(Amazon MSK, self\-managed Apache Kafka\) The Secrets Manager ARN of your secret key containing the certificate chain \(X\.509 PEM\), private key \(PKCS\#8 PEM\), and private key password \(optional\) used for mutual TLS authentication of your MSK/Apache Kafka brokers\.
+  `SERVER_ROOT_CA_CERTIFICATE` \- \(Self\-managed Apache Kafka\) The Secrets Manager ARN of your secret key containing the root CA certificate \(X\.509 PEM\) used for TLS encryption of your Apache Kafka brokers\. 
Type: String  
Valid Values:` BASIC_AUTH | VPC_SUBNET | VPC_SECURITY_GROUP | SASL_SCRAM_512_AUTH | SASL_SCRAM_256_AUTH | VIRTUAL_HOST | CLIENT_CERTIFICATE_TLS_AUTH | SERVER_ROOT_CA_CERTIFICATE`   
Required: No

 ** URI **   <a name="SSS-Type-SourceAccessConfiguration-URI"></a>
The value for your chosen configuration in `Type`\. For example: `"URI": "arn:aws:secretsmanager:us-east-1:01234567890:secret:MyBrokerSecretName"`\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 200\.  
Pattern: `[a-zA-Z0-9-\/*:_+=.@-]*`   
Required: No

## See Also<a name="API_SourceAccessConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/SourceAccessConfiguration) 