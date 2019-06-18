# Tagging Lambda Functions<a name="tagging"></a>

Lambda functions can span multiple applications across separate regions\. To simplify the process of tracking the frequency and cost of each function invocation, you can use tags\. Tags are key\-value pairs that you attach to AWS resources to better organize them\. They are particularly useful when you have many resources of the same type, which in the case of AWS Lambda, is a function\. By using tags, customers with hundreds of Lambda functions can easily access and analyze a specific set by filtering on those that contain the same tag\. Two of the key advantages of tagging your Lambda functions are:
+ **Grouping and Filtering:** By applying tags, you can use the Lambda console or CLI to isolate a list of Lambda functions contained within a specific application or billing department\. For more information, see [Filtering on Tagged Lambda Functions](#tag-filtering)\. 
+ **Cost allocation:** Because Lambda's support for tagging is integrated with AWS Billing, you can break down bills into dynamic categories and map functions to specific cost centers\. For example, if you tag all Lambda functions with a "Department" key, then all AWS Lambda costs can be broken down by department\. You can then provide an individual department value, such "Department 1" or "Department 2" to direct the function invocation cost to the appropriate cost center\. Cost allocation is surfaced via detailed billing reports, making it easier for you to categorize and track your AWS costs\. 

**Topics**
+ [Tagging Lambda Functions for Billing](#tagging-for-billing)
+ [Applying Tags to Lambda Functions Using the Console](#how-to-tag-console)
+ [Applying Tags to Lambda Functions Using the CLI](#how-to-tag-cli)
+ [Filtering on Tagged Lambda Functions](#tag-filtering)
+ [Tag Restrictions](#tag-restrictions)

## Tagging Lambda Functions for Billing<a name="tagging-for-billing"></a>

You can use tags to organize your AWS bill to reflect your own cost structure\. To do this, you can add tag keys whose values will be included in the cost allocation report\. For more information about setting up a cost allocation report that includes the tag keys you select to be included as line items in the report, see [The Monthly Cost Allocation Report](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/configurecostallocreport.html) in *About AWS Account Billing*\. 

To see the cost of your combined resources, you can organize your billing information based on functions that have the same tag key values\. For example, you can tag several Lambda functions with a specific application name, and then organize your billing information to see the total cost of that application across several services\. For more information, see [Using Cost Allocation Tags](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/cost-alloc-tags.html) in the *AWS Billing and Cost Management User Guide*\. 

In AWS Lambda the only resource that can be tagged is a function\. You cannot tag an alias or a specific function version\. Any invocation of a function's alias or version will be billed as an invocation of the original function\.

## Applying Tags to Lambda Functions Using the Console<a name="how-to-tag-console"></a>

You can add tags to your function under the **Tags** section in the **configuration** tab\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/TagConsole1.png)

To remove tags from an existing function, open the function, choose the **Tags** section and then choose the **Remove** button next to key\-value pair\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/TagConsole2.png)

## Applying Tags to Lambda Functions Using the CLI<a name="how-to-tag-cli"></a>

When you create a new Lambda function, you can include tags with the `--tags` option\.

```
$ aws lambda create-function --function-name my-function
--handler index.js --runtime nodejs8.10 \
--role role-arn \
--tags "DEPARTMENT=Department A"
```

To add tags to an existing function, use the `tag-resource` command\. 

```
$ aws lambda tag-resource \
--resource function arn \
--tags "DEPARTMENT=Department A"
```

To remove tags, use the `untag-resource` command\. 

```
$ aws lambda untag-resource --resource function arn \
--tagkeys DEPARTMENT
```

## Filtering on Tagged Lambda Functions<a name="tag-filtering"></a>

Once you have grouped your Lambda functions by using tags, you can leverage the filtering capabilities provided by the Lambda console or the AWS CLI to view them based on your specific requirements\.

### Filtering Lambda Functions Using the Console<a name="tag-filtering-console"></a>

The Lambda console contains a search field that allows you to filter the list of functions based on a specified set of function attributes, including **Tags**\. Suppose you have two functions named **MyFunction** and **MyFunction2** that have a **Tags** key called **Department**\. To view those functions, choose the search field and notice the automatic filtering that includes a list of the **Tags** keys: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Filtering.png)

Choose the **Department** key\. Lambda will return any function that contains that key\.

Now suppose that the key value of the **MyFunction** tag is "Department A" and the key value of MyFunction2 is "Department B"\. You can narrow your search by choosing the value of the **Department** key, in this case **Department A**, as shown below\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Filtering1.png)

This will return only **MyFunction\.**

You can further narrow your search by including the other accepted **Function attributes**, including **Description**, **Function name** or **Runtime**\. 

**Note**  
You are limited to a maximum of 50 tags per Lambda function\. If you delete the Lambda function, the associated tags will also be deleted\.

### Filtering Lambda Functions Using the CLI<a name="tag-filtering-cli"></a>

If you want to view the tags that are applied to a specific Lambda function, you can use either of the following Lambda API commands:
+ [ListTags](API_ListTags.md): You supply your Lambda function ARN \(Amazon Resource Name\) to view a list of the tags associated with this function:

  ```
  $ aws lambda list-tags --resource function arn
  ```
+ [GetFunction](API_GetFunction.md): You supply your Lambda function name to a view a list of the tags associated with this function:

  ```
  $ aws lambda get-function --function-name my-function
  ```

You can also use the AWS Tagging Service’s [GetResources](https://docs.aws.amazon.com/resourcegroupstagging/latest/APIReference/API_GetResources.html) API to filter your resources by tags\. The GetResources API receives up to 10 filters, with each filter containing a tag key and up to 10 tag values\. You provide GetResources with a ‘ResourceType’ to filter by specific resource types\. For more information about the AWS Tagging Service, see [Working with Resource Groups](https://docs.aws.amazon.com/awsconsolehelpdocs/latest/gsg/resource-groups.html)\. 

## Tag Restrictions<a name="tag-restrictions"></a>

The following restrictions apply to tags:
+ Maximum number of tags per resource—50
+ Maximum key length—128 Unicode characters in UTF\-8
+ Maximum value length—256 Unicode characters in UTF\-8
+ Tag keys and values are case sensitive\.
+ Do not use the `aws:` prefix in your tag names or values because it is reserved for AWS use\. You can't edit or delete tag names or values with this prefix\. Tags with this prefix do not count against your tags per resource limit\.
+ If your tagging schema will be used across multiple services and resources, remember that other services may have restrictions on allowed characters\. Generally allowed characters are: letters, spaces, and numbers representable in UTF\-8, plus the following special characters: \+ \- = \. \_ : / @\.