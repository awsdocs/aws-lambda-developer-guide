# Using AWS Lambda with Alexa<a name="services-alexa"></a>

You can use Lambda functions to build services that give new skills to Alexa, the Voice assistant on Amazon Echo\. The Alexa Skills Kit provides the APIs, tools, and documentation to create these new skills, powered by your own services running as Lambda functions\. Amazon Echo users can access these new skills by asking Alexa questions or making requests\.

The Alexa Skills Kit is available on GitHub\.
+ [Alexa Skills Kit SDK for Java](https://github.com/alexa/alexa-skills-kit-sdk-for-java)
+ [Alexa Skills Kit SDK for Node\.js ](https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs)
+ [Alexa Skills Kit SDK for Python](https://github.com/alexa/alexa-skills-kit-sdk-for-python)

**Example Alexa smart home event**  

```
{
  "header": {
    "payloadVersion": "1",
    "namespace": "Control",
    "name": "SwitchOnOffRequest"
  },
  "payload": {
    "switchControlAction": "TURN_ON",
    "appliance": {
      "additionalApplianceDetails": {
        "key2": "value2",
        "key1": "value1"
      },
      "applianceId": "sampleId"
    },
    "accessToken": "sampleAccessToken"
  }
}
```

For more information, see [ Host a custom skill as an AWS Lambda Function](https://developer.amazon.com/docs/alexa/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html) in the *Build Skills with the Alexa Skills Kit* guide\.