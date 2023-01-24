# Cors<a name="API_Cors"></a>

The [cross\-origin resource sharing \(CORS\)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) settings for your Lambda function URL\. Use CORS to grant access to your function URL from any origin\. You can also use CORS to control access for specific HTTP headers and methods in requests to your function URL\.

## Contents<a name="API_Cors_Contents"></a>

 ** AllowCredentials **   <a name="SSS-Type-Cors-AllowCredentials"></a>
Whether to allow cookies or other credentials in requests to your function URL\. The default is `false`\.  
Type: Boolean  
Required: No

 ** AllowHeaders **   <a name="SSS-Type-Cors-AllowHeaders"></a>
The HTTP headers that origins can include in requests to your function URL\. For example: `Date`, `Keep-Alive`, `X-Custom-Header`\.  
Type: Array of strings  
Array Members: Maximum number of 100 items\.  
Length Constraints: Maximum length of 1024\.  
Pattern: `.*`   
Required: No

 ** AllowMethods **   <a name="SSS-Type-Cors-AllowMethods"></a>
The HTTP methods that are allowed when calling your function URL\. For example: `GET`, `POST`, `DELETE`, or the wildcard character \(`*`\)\.  
Type: Array of strings  
Array Members: Maximum number of 6 items\.  
Length Constraints: Maximum length of 6\.  
Pattern: `.*`   
Required: No

 ** AllowOrigins **   <a name="SSS-Type-Cors-AllowOrigins"></a>
The origins that can access your function URL\. You can list any number of specific origins, separated by a comma\. For example: `https://www.example.com`, `http://localhost:60905`\.  
Alternatively, you can grant access to all origins using the wildcard character \(`*`\)\.  
Type: Array of strings  
Array Members: Maximum number of 100 items\.  
Length Constraints: Minimum length of 1\. Maximum length of 253\.  
Pattern: `.*`   
Required: No

 ** ExposeHeaders **   <a name="SSS-Type-Cors-ExposeHeaders"></a>
The HTTP headers in your function response that you want to expose to origins that call your function URL\. For example: `Date`, `Keep-Alive`, `X-Custom-Header`\.  
Type: Array of strings  
Array Members: Maximum number of 100 items\.  
Length Constraints: Maximum length of 1024\.  
Pattern: `.*`   
Required: No

 ** MaxAge **   <a name="SSS-Type-Cors-MaxAge"></a>
The maximum amount of time, in seconds, that web browsers can cache results of a preflight request\. By default, this is set to `0`, which means that the browser doesn't cache results\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 86400\.  
Required: No

## See Also<a name="API_Cors_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/Cors) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/Cors) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/Cors) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/Cors) 