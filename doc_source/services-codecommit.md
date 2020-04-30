# Using AWS Lambda with AWS CodeCommit<a name="services-codecommit"></a>

You can create a trigger for an AWS CodeCommit repository so that events in the repository will invoke a Lambda function\. For example, you can invoke a Lambda function when a branch or tag is created or when a push is made to an existing branch\.

**Example AWS CodeCommit message event**  

```
{
    "Records": [
        {
            "awsRegion": "us-east-2",
            "codecommit": {
                "references": [
                    {
                        "commit": "5e493c6f3067653f3d04eca608b4901eb227078",
                        "ref": "refs/heads/master"
                    }
                ]
            },
            "eventId": "31ade2c7-f889-47c5-a937-1cf99e2790e9",
            "eventName": "ReferenceChanges",
            "eventPartNumber": 1,
            "eventSource": "aws:codecommit",
            "eventSourceARN": "arn:aws:codecommit:us-east-2:123456789012:lambda-pipeline-repo",
            "eventTime": "2019-03-12T20:58:25.400+0000",
            "eventTotalParts": 1,
            "eventTriggerConfigId": "0d17d6a4-efeb-46f3-b3ab-a63741badeb8",
            "eventTriggerName": "index.handler",
            "eventVersion": "1.0",
            "userIdentityARN": "arn:aws:iam::123456789012:user/intern"
        }
    ]
}
```

For more information, see [Manage triggers for an AWS CodeCommit repository](https://docs.aws.amazon.com/codecommit/latest/userguide/how-to-notify.html)\.