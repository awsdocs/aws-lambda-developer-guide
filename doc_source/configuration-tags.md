# Using tags on Lambda functions<a name="configuration-tags"></a>

You can tag AWS Lambda functions to activate [attribute\-based access control \(ABAC\)](attribute-based-access-control.md) and to organize them by owner, project, or department\. Tags are free\-form key\-value pairs that are supported across AWS services for use in ABAC, filtering resources, and [adding detail to billing reports\.](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/cost-alloc-tags.html)

Tags apply at the function level, not to versions or aliases\. Tags are not part of the version\-specific configuration that Lambda creates a snapshot of when you publish a version\.

**Topics**
+ [Permissions required for working with tags](#permissions-required-for-working-with-tags-cli)
+ [Using tags with the Lambda console](#using-tags-with-the-console)
+ [Using tags with the AWS CLI](#configuration-tags-cli)
+ [Requirements for tags](#configuration-tags-restrictions)

## Permissions required for working with tags<a name="permissions-required-for-working-with-tags-cli"></a>

Grant appropriate permissions to the AWS Identity and Access Management \(IAM\) identity \(user, group, or role\) for the person working with the function:
+ **lambda:ListTags **– When a function has tags, grant this permission to anyone who needs to call `GetFunction` or `ListTags` on it\.
+ **lambda:TagResource** – Grant this permission to anyone who needs to call `CreateFunction` or `TagResource`\.

For more information, see [Identity\-based IAM policies for Lambda](access-control-identity-based.md)\.

## Using tags with the Lambda console<a name="using-tags-with-the-console"></a>

You can use the Lambda console to create functions that have tags, add tags to existing functions, and filter functions by tags that you add\.

**To add tags when you create a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose **Create function**\.

1. Choose **Author from scratch** or **Container image**\. 

1. Under **Basic information**, do the following:

   1. For **Function name**, enter the function name\. Function names are limited to 64 characters in length\.

   1. For **Runtime**, choose the language version to use for your function\.

   1. \(Optional\) For **Architecture**, choose the [instruction set architecture](foundation-arch.md) to use for your function\. The default architecture is x86\_64\. When you build the deployment package for your function, make sure that it is compatible with the instruction set architecture that you choose\.

1. Expand **Advanced settings**, and then select **Enable tags**\.

1. Choose **Add new tag**, and then enter a **Key** and an optional **Value**\. To add more tags, repeat this step\.

1. Choose **Create function**\.

**To add tags to an existing function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of a function\.

1. Choose **Configuration**, and then choose **Tags**\.

1. Under **Tags**, choose **Manage tags**\.

1. Choose **Add new tag**, and then enter a **Key** and an optional **Value**\. To add more tags, repeat this step\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-add.png)

1. Choose **Save**\.

**To filter functions with tags**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the search bar to see a list of function attributes and tag keys\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-key.png)

1. Choose a tag key to see a list of values that are in use in the current AWS Region\.

1. Choose a value to see functions with that value, or choose **\(all values\)** to see all functions that have a tag with that key\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/configuration-tags-value.png)

The search bar also supports searching for tag keys\. Enter `tag` to see only a list of tag keys, or enter the name of a key to find it in the list\.

## Using tags with the AWS CLI<a name="configuration-tags-cli"></a>

### Adding and removing tags<a name="creating-tags-when-you-create-a-function-cli"></a>

To create a new Lambda function with tags, use the create\-function command with the \-\-tags option\.

```
aws lambda create-function --function-name my-function
--handler index.js --runtime nodejs16.x \
--role arn:aws:iam::123456789012:role/lambda-role \
--tags Department=Marketing,CostCenter=1234ABCD
```

To add tags to an existing function, use the tag\-resource command\.

```
aws lambda tag-resource \
--resource arn:aws:lambda:us-east-2:123456789012:function:my-function \
--tags Department=Marketing,CostCenter=1234ABCD
```

To remove tags, use the untag\-resource command\.

```
aws lambda untag-resource --resource arn:aws:lambda:us-east-1:123456789012:function:my-function \
--tag-keys Department
```

### Viewing tags on a function<a name="viewing-tags-on-a-function-cli"></a>

If you want to view the tags that are applied to a specific Lambda function, you can use either of the following AWS CLI commands:
+ [ListTags](API_ListTags.md) – To view a list of the tags associated with this function, include your Lambda function ARN \(Amazon Resource Name\):

  ```
  aws lambda list-tags --resource arn:aws:lambda:us-east-1:123456789012:function:my-function
  ```
+ [GetFunction](API_GetFunction.md) – To view a list of the tags associated with this function, include your Lambda function name:

  ```
  aws lambda get-function --function-name my-function
  ```

#### Filtering functions by tag<a name="filtering-functions-by-tag-cli"></a>

You can use the AWS Resource Groups Tagging API [GetResources](https://docs.aws.amazon.com/resourcegroupstagging/latest/APIReference/API_GetResources.html) API operation to filter your resources by tags\. The `GetResources` operation receives up to 10 filters, with each filter containing a tag key and up to 10 tag values\. You provide `GetResources` with a `ResourceType` to filter by specific resource types\.

For more information about AWS Resource Groups, see [What are resource groups?](https://docs.aws.amazon.com/ARG/latest/userguide/resource-groups.html) in the *AWS Resource Groups and Tags User Guide*\. 

## Requirements for tags<a name="configuration-tags-restrictions"></a>

The following requirements apply to tags:
+ Maximum number of tags per resource: 50
+ Maximum key length: 128 Unicode characters in UTF\-8
+ Maximum value length: 256 Unicode characters in UTF\-8
+ Tag keys and values are case sensitive\.
+ Do not use the `aws:` prefix in your tag names or values because it is reserved for AWS use\. You can't edit or delete tag names or values with this prefix\. Tags with this prefix do not count against your tags per resource limit\.
+ If you plan to use your tagging schema across multiple services and resources, remember that other services may have restrictions on allowed characters\. Generally allowed characters are: letters, spaces, and numbers representable in UTF\-8, plus the following special characters: \+ \- = \. \_ : / @\.