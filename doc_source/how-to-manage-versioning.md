# Managing Versioning Using the AWS Management Console, the AWS CLI, or Lambda API Operations<a name="how-to-manage-versioning"></a>

You can manage Lambda function versioning programmatically using AWS SDKs \(or make the AWS Lambda API calls directly, if you need to\), using AWS Command Line Interface \(AWS CLI\), or the AWS Lambda console\. 

AWS Lambda provides the following APIs to manage versioning and aliases: 

[PublishVersion](API_PublishVersion.md)

[ListVersionsByFunction](API_ListVersionsByFunction.md)

[CreateAlias](API_CreateAlias.md)

[UpdateAlias](API_UpdateAlias.md)

[DeleteAlias](API_DeleteAlias.md)

[GetAlias](API_GetAlias.md)

[ListAliases](API_ListAliases.md)

In addition to these APIs, existing relevant APIs also support versioning related operations\.

For an example of how you can use the AWS CLI, see [Tutorial: Using AWS Lambda Aliases](versioning-aliases-walkthrough1.md)\.

This section explains how you can use the AWS Lambda console to manage versioning\. In the AWS Lambda console, choose a function and then choose **Qualifiers**\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/versioning-console-10.png)

The expanded **Qualifiers** menu displays a **Versions** and **Aliases** tab, as shown in the following screen shot\. In the **Versions** pane, you can see a list of versions for the selected function\. If you have not previously published a version for the selected function, the **Versions** pane lists only the `$LATEST` version, as shown following\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/versioning-console-20.png)

Choose the **Aliases** tab to see a list of aliases for the function\. Initially, you won't have any aliases, as shown following\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/versioning-console-30.png)

Now you can publish a version or create aliases for the selected Lambda function by using the **Actions** menu\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/versioning-console-40.png)

To learn about versioning and aliases, see [AWS Lambda Function Versioning and Aliases](versioning-aliases.md)\.