# Using tags on AWS Lambda functions<a name="configuration-tags"></a>

You can tag Lambda functions to organize them by owner, project or department\. Tags are freeform key\-value pairs that are supported across AWS services for use in filtering resources and adding detail to billing reports\.

**Topics**
+ [Using tags with the Lambda console](#using-tags-with-the-console)
+ [Using tags with the AWS Command Line Interface](#configuration-tags-cli)
+ [Requirements for tags](#configuration-tags-restrictions)

## Using tags with the Lambda console<a name="using-tags-with-the-console"></a>

You can use the console to add tags to existing functions and to filter functions by the tags that you add\.

### Adding tags to a function<a name="configuration-tags-config"></a>

**To add tags to a function \(console\)**

1. Grant appropriate permissions to the IAM identity \(user, group, or role\) for the person working with the function:
   + **lambda:ListTags**—When a function has tags, grant this permission to anyone who needs to view the function\.
   + **lambda:TagResource**—Grant this permission to anyone who needs to add tags to a function\.

   For more information, see [Identity\-based IAM policies for Lambda](access-control-identity-based.md)\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Tags**\.

1. Under **Tags**, choose **Manage tags**\.

1. Enter a key and value\. To add additional tags, choose **Add new tag**\.

   Make sure that any tags you use conform to the [tag requirements](#configuration-tags-restrictions)\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-add.png)

1. Choose **Save**\.

### Filtering functions by tag<a name="configuration-tags-filter"></a>

You can filter functions based on the presence or value of a tag with the Lambda console or with the AWS Resource Groups API\.

Tags apply at the function level, not to versions or aliases\. Tags are not part of the version\-specific configuration that is snapshotted when you publish a version\.

**To filter functions with tags \(console\)**

1. Make sure that you have the permissions you need:
   + lambda:ListTags grants permission to view functions that have tags\.
   + lambda:TagResource grants permission to add tags to a function\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Click within the search bar to see a list of function attributes and tag keys\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-key.png)

1. Choose a tag key to see a list of values that are in\-use in the current region\.

1. Choose a value to see functions with that value, or choose **\(all values\)** to see all functions that have a tag with that key\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-value.png)

The search bar also supports searching for tag keys\. Type `tag` to see just a list of tag keys, or start typing the name of a key to find it in the list\.

With AWS Billing and Cost Management, you can use tags to customize billing reports and create cost\-allocation reports\. For more information, see see [Monthly Cost Allocation Report](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/configurecostallocreport.html) and [Using Cost Allocation Tags](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/cost-alloc-tags.html) in the *AWS Billing and Cost Management User Guide*\.

## Using tags with the AWS Command Line Interface<a name="configuration-tags-cli"></a>

You can use the console to create functions that have tags, add tags to existing functions, and to filter functions by the tags that you add\.

### Permissions required for working with tags<a name="permissions-required-for-working-with-tags-cli"></a>

Grant appropriate permissions to the IAM identity \(user, group, or role\) for the person working with the function:
+ **lambda:ListTags**—When a function has tags, grant this permission to anyone who needs to call `GetFunction` or `ListTags` on it\.
+ **lambda:TagResource**—Grant this permission to anyone who needs to call `CreateFunction` or `TagResource`\.

For more information, see [Identity\-based IAM policies for Lambda](access-control-identity-based.md)\.

### Creating tags when you create a function<a name="creating-tags-when-you-create-a-function-cli"></a>

Make sure that any tags you use conform to the [tag requirements](#configuration-tags-restrictions)\.

When you create a new Lambda function, you can include tags with the `--tags` option\.

```
aws lambda create-function --function-name my-function
--handler index.js --runtime nodejs12.x \
--role arn:aws:iam::123456789012:role/lambda-role \
--tags Department=Marketing,CostCenter=1234ABCD
```

To add tags to an existing function, use the `tag-resource` command\. 

```
aws lambda tag-resource \
--resource arn:aws:lambda:us-east-2:123456789012:function:my-function \
--tags Department=Marketing,CostCenter=1234ABCD
```

To remove tags, use the `untag-resource` command\. 

```
aws lambda untag-resource --resource function arn \
--tag-keys Department
```

### Viewing tags on a function<a name="viewing-tags-on-a-function-cli"></a>

If you want to view the tags that are applied to a specific Lambda function, you can use either of the following Lambda API commands:
+ [ListTags](API_ListTags.md) – You supply your Lambda function ARN \(Amazon Resource Name\) to view a list of the tags associated with this function:

  ```
  aws lambda list-tags --resource function arn
  ```
+ [GetFunction](API_GetFunction.md) – You supply your Lambda function name to a view a list of the tags associated with this function:

  ```
  aws lambda get-function --function-name my-function
  ```

#### Filtering functions by tag<a name="filtering-functions-by-tag-cli"></a>

You can use the AWS Resource Groups Tagging API [GetResources](https://docs.aws.amazon.com/resourcegroupstagging/latest/APIReference/API_GetResources.html) action to filter your resources by tags\. The GetResources API receives up to 10 filters, with each filter containing a tag key and up to 10 tag values\. You provide GetResources with a ‘ResourceType’ to filter by specific resource types\.

For more information about the AWS Resource Groups service, see [What are resource groups?](https://docs.aws.amazon.com/ARG/latest/userguide/resource-groups.html) in the *AWS Resource Groups and Tags User Guide*\. 

## Requirements for tags<a name="configuration-tags-restrictions"></a>

The following requirements apply to tags:
+ Maximum number of tags per resource—50
+ Maximum key length—128 Unicode characters in UTF\-8
+ Maximum value length—256 Unicode characters in UTF\-8
+ Tag keys and values are case sensitive\.
+ Do not use the `aws:` prefix in your tag names or values because it is reserved for AWS use\. You can't edit or delete tag names or values with this prefix\. Tags with this prefix do not count against your tags per resource limit\.
+ If your tagging schema will be used across multiple services and resources, remember that other services may have restrictions on allowed characters\. Generally allowed characters are: letters, spaces, and numbers representable in UTF\-8, plus the following special characters: \+ \- = \. \_ : / @\.