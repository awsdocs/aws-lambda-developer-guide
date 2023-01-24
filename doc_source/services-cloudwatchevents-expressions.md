# Schedule expressions using rate or cron<a name="services-cloudwatchevents-expressions"></a>

AWS Lambda supports standard rate and cron expressions for frequencies of up to once per minute\. EventBridge \(CloudWatch Events\) rate expressions have the following format\.

```
rate(Value Unit)
```

Where *Value* is a positive integer and *Unit* can be minute\(s\), hour\(s\), or day\(s\)\. For a singular value the unit must be singular \(for example, `rate(1 day)`\), otherwise plural \(for example, `rate(5 days)`\)\.


**Rate expression examples**  

| Frequency | Expression | 
| --- | --- | 
|  Every 5 minutes  |  `rate(5 minutes)`  | 
|  Every hour  |  `rate(1 hour)`  | 
|  Every seven days  |  `rate(7 days)`  | 

Cron expressions have the following format\.

```
cron(Minutes Hours Day-of-month Month Day-of-week Year)
```


**Cron expression examples**  

| Frequency | Expression | 
| --- | --- | 
|  10:15 AM \(UTC\) every day  |  `cron(15 10 * * ? *)`  | 
|  6:00 PM Monday through Friday  |  `cron(0 18 ? * MON-FRI *)`  | 
|  8:00 AM on the first day of the month  |  `cron(0 8 1 * ? *)`  | 
|  Every 10 min on weekdays  |  `cron(0/10 * ? * MON-FRI *)`  | 
|  Every 5 minutes between 8:00 AM and 5:55 PM weekdays  |  `cron(0/5 8-17 ? * MON-FRI *)`  | 
|  9:00 AM on the first Monday of each month  |  `cron(0 9 ? * 2#1 *)`  | 

Note the following:
+ If you are using the Lambda console, do not include the `cron` prefix in your expression\.
+ One of the day\-of\-month or day\-of\-week values must be a question mark \(`?`\)\.

For more information, see [Schedule expressions for rules](https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-schedule-expressions.html) in the *EventBridge User Guide*\.