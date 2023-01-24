# Overview of Amazon CodeWhisperer for AWS Lambda<a name="codewhisperer-overview"></a>

This overview contains a list of Amazon CodeWhisperer user actions and common ways for you to utilize CodeWhisperer in the AWS Lambda code editor\.

**Note**  
In the Lambda console, CodeWhisperer only supports functions using the Python and Node\.js runtimes\.

**Topics**
+ [User actions](#codewhisperer-actions)
+ [Use cases and examples](#codewhisperer-examples)

## User actions<a name="codewhisperer-actions"></a>

When you're using the Lambda code editor, you can use the following keyboard shortcuts to perform various CodeWhisperer user actions\.


**CodeWhisperer user actions and keyboard shortcuts**  

| Action | Keyboard shortcut | 
| --- | --- | 
|  Manually fetch a code suggestion  |  MacOS: `Option` \+ `C` Windows: `Alt` \+ `C`  | 
|  Accept a suggestion  |  `Tab`  | 
|  Reject a suggestion  |  `ESC`, `Backspace`, scroll in any direction, or keep typing and the recommendation automatically disappears\.  | 

## Use cases and examples<a name="codewhisperer-examples"></a>

Here are some common ways for you to utilize Amazon CodeWhisperer while authoring Lambda functions\.

### Single\-line code completion<a name="codewhisperer-single-line"></a>

When you start typing out single lines of code, CodeWhisperer makes suggestions based on your current and previous inputs\. In the image below, a user has begun to define a variable for an Amazon S3 client\. Based on this, CodeWhisperer then suggests a way to complete this line of code\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-single-line-completion-s3-client.png)

As another example, in the image below, a user has already written some code, and now wants to send a message to an Amazon SQS queue\. CodeWhisperer suggests a way to complete this final line of code\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-single-line-completion-sqs.png)

### Full function generation<a name="codewhisperer-full-function"></a>

CodeWhisperer can generate an entire function based on your function signature or code comments\. In the following image, a user has written a function signature for reading a file from Amazon S3\. Amazon CodeWhisperer then suggests a full implementation of the `read_from_s3` method\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-function-read-from-s3.png)

**Note**  
Sometimes, as in the previous example, CodeWhisperer includes `import` statements as part of its suggestions\. As a best practice, manually move these `import` statements to the top of your file\.

As another example, in the following image, a user has written a function signature\. CodeWhisperer then suggests a full implementation of the `quicksort` method\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-function-quicksort.png)

CodeWhisperer considers past code snippets when making suggestions\. In the following image, the user in the previous example has accepted the suggested implementation for `quicksort` above\. The user then writes another function signature for a generic `sort` method\. CodeWhisperer then suggests an implementation based on what has already been written\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-function-from-context-sort.png)

In the following image, a user has written a comment\. Based on this comment, CodeWhisperer then suggests a function signature\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-comment-binary-search.png)

In the following image, the user in the previous example has accepted the suggested function signature\. CodeWhisperer can then suggest a complete implementation of the `binary_search` function\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-function-binary-search.png)

### Line\-by\-line recommendations<a name="codewhisperer-line-by-line"></a>

Depending on your use case, CodeWhisperer may not be able to generate an entire function block in one recommendation\. However, CodeWhisperer can still provide line\-by\-line recommendations\. In the following image, the customer has written an initial comment indicating that they want to publish a message to an Amazon CloudWatch Logs group\. Given this context, CodeWhisperer is only able to suggest the client initialization code in its first recommendation, as shown in the following image\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-line-by-line-cwlogs-1.png)

However, if the user continues to request line\-by\-line recommendations, CodeWhisperer also continues to suggest lines of code based on what's already been written\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-line-by-line-cwlogs-2.png)

**Note**  
In the example above, `VPCFlowLogs` may not be the correct constant value\. As CodeWhisperer makes suggestions, remember to rename any constants as required\.

CodeWhisperer can eventually complete the entire code block as shown in the following image\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/whisper-line-by-line-cwlogs-3.png)