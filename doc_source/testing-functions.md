# Testing Lambda functions in the console<a name="testing-functions"></a>

You can test your Lambda function in the console by invoking your function with a test event\. A *test event* is a JSON input to your function\. If your function doesn't require input, the event can be an empty document `({})`\.

## Private test events<a name="creating-private-events"></a>

Private test events are available only to the event creator, and they require no additional permissions to use\. You can create and save up to 10 private test events per function\.

**To create a private test event**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function that you want to test\.

1. Choose the **Test** tab\.

1. Under **Test event**, do the following:

   1. Choose a **Template**\.

   1. Enter a **Name** for the test\.

   1. In the text entry box, enter the JSON test event\.

   1. Under **Event sharing settings**, choose **Private**\.

1. Choose **Save changes**\.

You can also create new test events on the **Code** tab\. From there, choose **Test**, **Configure test event**\.

## Shareable test events<a name="creating-shareable-events"></a>

Shareable test events are test events that you can share with other AWS Identity and Access Management \(IAM\) users in the same AWS account\. You can edit other users' shareable test events and invoke your function with them\.

Lambda saves shareable test events as schemas in an [Amazon EventBridge \(CloudWatch Events\) schema registry](https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-schema-registry.html) named `lambda-testevent-schemas`\. As Lambda utilizes this registry to store and call shareable test events you create, we recommend that you do not edit this registry or create a registry using the `lambda-testevent-schemas` name\.

To see, share, and edit shareable test events, you must have permissions for all of the following [EventBridge \(CloudWatch Events\) schema registry API operations](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/operations.html):
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname.html#CreateRegistry](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname.html#CreateRegistry)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#CreateSchema](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#CreateSchema)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#DeleteSchema](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#DeleteSchema)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname-version-schemaversion.html#DeleteSchemaVersion](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname-version-schemaversion.html#DeleteSchemaVersion)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname.html#DescribeRegistry](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname.html#DescribeRegistry)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#DescribeSchema](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#DescribeSchema)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-discover.html#GetDiscoveredSchema](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-discover.html#GetDiscoveredSchema)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname-versions.html#ListSchemaVersions](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname-versions.html#ListSchemaVersions)
+ [https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#UpdateSchema](https://docs.aws.amazon.com/eventbridge/latest/schema-reference/v1-registries-name-registryname-schemas-name-schemaname.html#UpdateSchema)

Note that saving edits made to a shareable test event overwrites that event\.

If you cannot create, edit, or see shareable test events, check that your account has the required permissions for these operations\. If you have the required permissions but still cannot access shareable test events, check for any [resource\-based policies](access-control-resource-based.md) that might limit access to the EventBridge \(CloudWatch Events\) registry\.

**To create a shareable test event**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function that you want to test\.

1. Choose the **Test** tab\.

1. Under **Test event**, do the following:

   1. Choose a **Template**\.

   1. Enter a **Name** for the test\.

   1. In the text entry box, enter the JSON test event\.

   1. Under **Event sharing settings**, choose **Shareable**\.

1. Choose **Save changes**\.

## Invoking functions with test events<a name="invoke-with-event"></a>

When you run a test event in the console, Lambda synchronously invokes your function with the test event\. The function runtime converts the JSON document into an object and passes it to your code's handler method for processing\.

**To test a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function that you want to test\.

1. Choose the **Test** tab\.

1. Under **Test event**, choose **Saved event**, and then choose the saved event that you want to use\.

1. Choose **Test**\.

1. To review the test results, under **Execution result**, expand **Details**\.

To invoke your function without saving your test event, choose **Test** before saving\. This creates an unsaved test event that Lambda preserves for the duration of the session\.

You can also access your saved and unsaved test events on the **Code** tab\. From there, choose **Test**, and then choose your test event\.

## Deleting shareable test event schemas<a name="deleting-test-schemas"></a>

When you delete shareable test events, Lambda removes them from the `lambda-testevent-schemas` registry\. If you remove the last shareable test event from the registry, Lambda deletes the registry\.

If you delete the function, Lambda does not delete any associated shareable test event schemas\. You must clean up these resources manually from the [EventBridge \(CloudWatch Events\) console](https://console.aws.amazon.com/events)\.