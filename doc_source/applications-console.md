# Managing applications in the AWS Lambda console<a name="applications-console"></a>

The AWS Lambda console helps you monitor and manage your [Lambda applications](deploying-lambda-apps.md)\. The **Applications** menu lists AWS CloudFormation stacks with Lambda functions\. The menu includes stacks that you launch in AWS CloudFormation by using the AWS CloudFormation console, the AWS Serverless Application Repository, the AWS CLI, or the AWS SAM CLI\.

**To view a Lambda application**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose an application\.  
![\[A monitoring widget.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/applications-page.png)

The overview shows the following information about your application\.
+ **AWS CloudFormation template** or **SAM template** – The template that defines your application\.
+ **Resources** – The AWS resources that are defined in your application's template\. To manage your application's Lambda functions, choose a function name from the list\.

## Monitoring applications<a name="applications-console-monitoring"></a>

The **Monitoring** tab shows an Amazon CloudWatch dashboard with aggregate metrics for the resources in your application\.

**To monitor a Lambda application**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose **Monitoring**\.

By default, the Lambda console shows a basic dashboard\. You can customize this page by defining custom dashboards in your application template\. When your template includes one or more dashboards, the page shows your dashboards instead of the default dashboard\. You can switch between dashboards with the drop\-down menu on the top right of the page\.

## Custom monitoring dashboards<a name="applications-console-dashboards"></a>

Customize your application monitoring page by adding one or more Amazon CloudWatch dashboards to your application template with the [AWS::CloudWatch::Dashboard](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-cw-dashboard.html) resource type\. The following example creates a dashboard with a single widget that graphs the number of invocations of a function named `my-function`\.

**Example function dashboard template**  

```
Resources:
  MyDashboard:
    Type: AWS::CloudWatch::Dashboard
    Properties:
      DashboardName: my-dashboard
      DashboardBody: |
        {
            "widgets": [
                {
                    "type": "metric",
                    "width": 12,
                    "height": 6,
                    "properties": {
                        "metrics": [
                            [
                                "AWS/Lambda",
                                "Invocations",
                                "FunctionName",
                                "my-function",
                                {
                                    "stat": "Sum",
                                    "label": "MyFunction"
                                }
                            ],
                            [
                                {
                                    "expression": "SUM(METRICS())",
                                    "label": "Total Invocations"
                                }
                            ]
                        ],
                        "region": "us-east-1",
                        "title": "Invocations",
                        "view": "timeSeries",
                        "stacked": false
                    }
                }
            ]
        }
```

You can get the definition for any of the widgets in the default monitoring dashboard from the CloudWatch console\.

**To view a widget definition**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose an application that has the standard dashboard\.

1. Choose **Monitoring**\.

1. On any widget, choose **View in metrics** from the drop\-down menu\.  
![\[A monitoring widget.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/applications-monitoring-widget.png)

1. Choose **Source**\.

For more information about authoring CloudWatch dashboards and widgets, see [Dashboard body structure and syntax](https://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/CloudWatch-Dashboard-Body-Structure.html) in the *Amazon CloudWatch API Reference*\.