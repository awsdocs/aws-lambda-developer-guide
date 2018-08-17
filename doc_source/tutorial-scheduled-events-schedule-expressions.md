# Schedule Expressions Using Rate or Cron<a name="tutorial-scheduled-events-schedule-expressions"></a>

**Rate expression**

```
rate(Value Unit)
```

Where:

*Value* can be a positive integer\.

*Unit* can be minute\(s\), hour\(s\), or day\(s\)\.

For example:


****  

| Example | Cron expression | 
| --- | --- | 
|  Invoke Lambda function every 5 minutes  |  <pre>rate(5 minutes)</pre>  | 
|  Invoke Lambda function every hour  |  <pre>rate(1 hour)</pre>  | 
|  Invoke Lambda function every seven days  |  <pre>rate(7 days)</pre>  | 

Note the following: 
+ Rate frequencies of less than one minute are not supported\.
+ For a singular value the unit must be singular \(for example, `rate(1 day)`\), otherwise plural \(for example, `rate(5 days)`\)\.

 

**Cron expression**

```
cron(Minutes Hours Day-of-month Month Day-of-week Year)
```

All fields are required and time zone is UTC only\. The following table describes these fields\.


****  

| Field | Values | Wildcards | 
| --- | --- | --- | 
|  Minutes  |  0\-59  |   , \- \* /   | 
|  Hours  |  0\-23  |   , \- \* /   | 
|  Day\-of\-month  |  1\-31  |   , \- \* ? / L W   | 
|  Month  |  1\-12 or JAN\-DEC  |   , \- \* /   | 
|  Day\-of\-week  |  1\-7 or SUN\-SAT  |   , \- \* ? / L \#   | 
|  Year  |  1970\-2199  |   , \- \* /   | 

The following table describes the wildcard characters\.


****  

| Character | Definition | Example | 
| --- | --- | --- | 
|  /  |  Specifies increments  |  0/15 in the minutes field directs execution to occur every 15 minutes\.  | 
|  L  |  Specifies "Last"  |  If used in Day\-of\-month field, specifies last day of the month\. If used in Day\-of\-week field, specifies last day of the week \(Saturday\)\.  | 
|  W  |  Specifies *Weekday*  |  When used with a date, such as `5/W`, specifies the closest weekday to 5th day of the month\. If the 5th falls on a Saturday, execution occurs on Friday\. If the 5th falls on a Sunday, execution occurs on Monday\.  | 
|  \#  |  Specifies the *nd* or *nth* day of the month  |  Specifying `3#2` means the second Tuesday of the month \(Tuesday is the third day of the 7\-day week\)\.  | 
|  \*  |  Specifies *All values*  |  If used in the Day\-of\-month field, it means all days in the month\.  | 
|  ?  |  No specified value  |  Used in conjunction with another specified value\. For example, if a specific date is specified, but you don't care what day of the week it falls on\.  | 
|  \-  |  Specifies ranges  |  `10-12` would mean 10, 11 and 12  | 
|  ,  |  Specifies additional values  |  `SUN,MON,TUE` means Sunday, Monday and Tuesday  | 
|  /  |  Specifies increments  |  `5/10` means 5, 15, 25, 35, etc\.  | 

The following table lists common examples of cron expressions\.


****  

| Example | Cron expression | 
| --- | --- | 
|  Invoke a Lambda function at 10:00am \(UTC\) everyday  |  <pre>cron(0 10 * * ? *)</pre>  | 
|  Invoke a Lambda function 12:15pm \(UTC\) everyday  |  <pre>cron(15 12 * * ? *)</pre>  | 
|  Invoke a Lambda function at 06:00pm \(UTC\) every Mon\-Fri  |  <pre>cron(0 18 ? * MON-FRI *)</pre>  | 
|  Invoke a Lambda function at 8:00am \(UTC\) every first day of the month  |  <pre>cron(0 8 1 * ? *)</pre>  | 
|  Invoke a Lambda function every 10 min Mon\-Fri  |  <pre>cron(0/10 * ? * MON-FRI *)</pre>  | 
|  Invoke a Lambda function every 5 minutes Mon\-Fri between 8:00am and 5:55pm \(UTC\)  |  <pre>cron(0/5 8-17 ? * MON-FRI *)</pre>  | 
|  Invoke a Lambda function at 9 a\.m\. \(UTC\) the first Monday of each month  |  <pre>cron(0 9 ? * 2#1 *)</pre>  | 

Note the following: 
+ The previous examples assume you are using the AWS CLI\. If you are using the Lambda console, do not include the `cron` prefix to your expression\.
+ Cron expressions that lead to rates faster than one minute are not supported\.
+ One of the day\-of\-month or day\-of\-week values must be a question mark \(`?`\)\.