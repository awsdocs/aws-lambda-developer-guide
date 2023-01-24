# Creating and managing Lambda function URLs<a name="urls-configuration"></a>

A function URL is a dedicated HTTP\(S\) endpoint for your Lambda function\. You can create and configure a function URL through the Lambda console or the Lambda API\. When you create a function URL, Lambda automatically generates a unique URL endpoint for you\. Once you create a function URL, its URL endpoint never changes\. Function URL endpoints have the following format:

```
https://<url-id>.lambda-url.<region>.on.aws
```

**Topics**
+ [Creating a function URL \(console\)](#create-url-console)
+ [Creating a function URL \(AWS CLI\)](#create-url-cli)
+ [Adding a function URL to a CloudFormation template](#urls-cfn)
+ [Cross\-origin resource sharing \(CORS\)](#urls-cors)
+ [Throttling function URLs](#urls-throttling)
+ [Deactivating function URLs](#urls-deactivating)
+ [Deleting function URLs](#w831aac55c21c37)

## Creating a function URL \(console\)<a name="create-url-console"></a>

Follow these steps to create a function URL using the console\.

### To create a function URL for an existing function \(console\)<a name="create-url-existing-function"></a>

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function that you want to create the function URL for\.

1. Choose the **Configuration** tab, and then choose **Function URL**\.

1. Choose **Create function URL**\.

1. For **Auth type**, choose **AWS\_IAM** or **NONE**\. For more information about function URL authentication, see [Security and auth model](urls-auth.md)\.

1. \(Optional\) Select **Configure cross\-origin resource sharing \(CORS\)**, and then configure the CORS settings for your function URL\. For more information about CORS, see [Cross\-origin resource sharing \(CORS\)](#urls-cors)\.

1. Choose **Save**\.

This creates a function URL for the `$LATEST` unpublished version of your function\. The function URL appears in the **Function overview** section of the console\.

### To create a function URL for an existing alias \(console\)<a name="create-url-existing-alias"></a>

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function with the alias that you want to create the function URL for\.

1. Choose the **Aliases** tab, and then choose the name of the alias that you want to create the function URL for\.

1. Choose the **Configuration** tab, and then choose **Function URL**\.

1. Choose **Create function URL**\.

1. For **Auth type**, choose **AWS\_IAM** or **NONE**\. For more information about function URL authentication, see [Security and auth model](urls-auth.md)\.

1. \(Optional\) Select **Configure cross\-origin resource sharing \(CORS\)**, and then configure the CORS settings for your function URL\. For more information about CORS, see [Cross\-origin resource sharing \(CORS\)](#urls-cors)\.

1. Choose **Save**\.

This creates a function URL for your function alias\. The function URL appears in the console's **Function overview** section for your alias\.

### To create a new function with a function URL \(console\)<a name="create-url-new-function"></a>

**To create a new function with a function URL \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose **Create function**\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter a name for your function, such as **my\-function**\.

   1. For **Runtime**, choose the language runtime that you prefer, such as **Node\.js 14\.x**\.

   1. For **Architecture**, choose either **x86\_64** or **arm64**\.

   1. Expand **Permissions**, then choose whether to create a new execution role or use an existing one\.

1. Expand **Advanced settings**, and then select **Function URL**\.

1. For **Auth type**, choose **AWS\_IAM** or **NONE**\. For more information about function URL authentication, see [Security and auth model](urls-auth.md)\.

1. \(Optional\) Select **Configure cross\-origin resource sharing \(CORS\)**\. By selecting this option during function creation, your function URL allows requests from all origins by default\. You can edit the CORS settings for your function URL after creating the function\. For more information about CORS, see [Cross\-origin resource sharing \(CORS\)](#urls-cors)\.

1. Choose **Create function**\.

This creates a new function with a function URL for the `$LATEST` unpublished version of the function\. The function URL appears in the **Function overview** section of the console\.

## Creating a function URL \(AWS CLI\)<a name="create-url-cli"></a>

To create a function URL for an existing Lambda function using the AWS Command Line Interface \(AWS CLI\), run the following command:

```
aws lambda create-function-url-config \
    --function-name my-function \
    --qualifier prod \ // optional
    --auth-type AWS_IAM
    --cors-config {AllowOrigins="https://example.com"} // optional
```

This adds a function URL to the **prod** qualifier for the function **my\-function**\. For more information about these configuration parameters, see [CreateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateFunctionUrlConfig.html) in the API reference\.

**Note**  
To create a function URL via the AWS CLI, the function must already exist\.

## Adding a function URL to a CloudFormation template<a name="urls-cfn"></a>

To add an `AWS::Lambda::Url` resource to your AWS CloudFormation template, use the following syntax:

### JSON<a name="urls-cfn-json"></a>

```
{
  "Type" : "AWS::Lambda::Url",
  "Properties" : {
      "AuthType" : String,
      "Cors" : Cors,
      "Qualifier" : String,
      "TargetFunctionArn" : String
    }
}
```

### YAML<a name="urls-cfn-yaml"></a>

```
Type: AWS::Lambda::Url
Properties: 
  AuthType: String
  Cors: 
    Cors
  Qualifier: String
  TargetFunctionArn: String
```

### Parameters<a name="urls-cfn-params"></a>
+ \(Required\) `AuthType` – Defines the type of authentication for your function URL\. Possible values are either `AWS_IAM` or `NONE`\. To restrict access to authenticated IAM users only, set to `AWS_IAM`\. To bypass IAM authentication and allow any user to make requests to your function, set to `NONE`\.
+ \(Optional\) `Cors` – Defines the [CORS settings](#urls-cors) for your function URL\. To add `Cors` to your `AWS::Lambda::Url` resource in CloudFormation, use the following syntax\.

    
**Example AWS::Lambda::Url\.Cors \(JSON\)**  

  ```
  {
    "AllowCredentials" : Boolean,
    "AllowHeaders" : [ String, ... ],
    "AllowMethods" : [ String, ... ],
    "AllowOrigins" : [ String, ... ],
    "ExposeHeaders" : [ String, ... ],
    "MaxAge" : Integer
  }
  ```  
**Example AWS::Lambda::Url\.Cors \(YAML\)**  

  ```
    AllowCredentials: Boolean
    AllowHeaders: 
      - String
    AllowMethods: 
      - String
    AllowOrigins: 
      - String
    ExposeHeaders: 
      - String
    MaxAge: Integer
  ```
+ \(Optional\) `Qualifier` – The alias name\.
+ \(Required\) `TargetFunctionArn` – The name or Amazon Resource Name \(ARN\) of the Lambda function\. Valid name formats include the following:
  + **Function name** – `my-function`
  + **Function ARN** – `arn:aws:lambda:us-west-2:123456789012:function:my-function`
  + **Partial ARN** – `123456789012:function:my-function`

## Cross\-origin resource sharing \(CORS\)<a name="urls-cors"></a>

To define how different origins can access your function URL, use [cross\-origin resource sharing \(CORS\)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)\. We recommend configuring CORS if you intend to call your function URL from a different domain\. Lambda supports the following CORS headers for function URLs\.


| CORS header | CORS configuration property | Example values | 
| --- | --- | --- | 
|  [ Access\-Control\-Allow\-Origin](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Origin)  |  `AllowOrigins`  |  `*` \(allow all origins\) `https://www.example.com` `http://localhost:60905`  | 
|  [ Access\-Control\-Allow\-Methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Methods)  |  `AllowMethods`  |  `GET`, `POST`, `DELETE`, `*`  | 
|  [ Access\-Control\-Allow\-Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Headers)  |  `AllowHeaders`  |  `Date`, `Keep-Alive`, `X-Custom-Header`  | 
|  [ Access\-Control\-Expose\-Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Expose-Headers)  |  `ExposeHeaders`  |  `Date`, `Keep-Alive`, `X-Custom-Header`  | 
|  [ Access\-Control\-Allow\-Credentials](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Credentials)  |  `AllowCredentials`  |  `TRUE`  | 
|  [ Access\-Control\-Max\-Age](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Max-Age)  |  `MaxAge`  |  `5` \(default\), `300`  | 

When you configure CORS for a function URL using the Lambda console or the AWS CLI, Lambda automatically adds the CORS headers to all responses through the function URL\. Alternatively, you can manually add CORS headers to your function response\. If there are conflicting headers, the configured CORS headers on the function URL take precedence\.

## Throttling function URLs<a name="urls-throttling"></a>

Throttling limits the rate at which your function processes requests\. This is useful in many situations, such as preventing your function from overloading downstream resources, or handling a sudden surge in requests\.

You can throttle the rate of requests that your Lambda function processes through a function URL by configuring reserved concurrency\. Reserved concurrency limits the number of maximum concurrent invocations for your function\. Your function's maximum request rate per second \(RPS\) is equivalent to 10 times the configured reserved concurrency\. For example, if you configure your function with a reserved concurrency of 100, then the maximum RPS is 1,000\.

Whenever your function concurrency exceeds the reserved concurrency, your function URL returns an HTTP `429` status code\. If your function receives a request that exceeds the 10x RPS maximum based on your configured reserved concurrency, you also receive an HTTP `429` error\. For more information about reserved concurrency, see [Managing Lambda reserved concurrency](configuration-concurrency.md)\.

## Deactivating function URLs<a name="urls-deactivating"></a>

In an emergency, you might want to reject all traffic to your function URL\. To deactivate your function URL, set the reserved concurrency to zero\. This throttles all requests to your function URL, resulting in HTTP `429` status responses\. To reactivate your function URL, delete the reserved concurrency configuration, or set the configuration to an amount greater than zero\.

## Deleting function URLs<a name="w831aac55c21c37"></a>

When you delete a function URL, you can’t recover it\. Creating a new function URL will result in a different URL address\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function\.

1. Choose the **Configuration** tab, and then choose **Function URL**\.

1. Choose **Delete**\.

1. Enter the word *delete* into the field to confirm the deletion\.

1. Choose **Delete**\.

**Note**  
If you delete a function URL with auth type `NONE`, Lambda doesn't automatically delete the associated resource\-based policy\. If you want to delete this policy, you must manually do so\.