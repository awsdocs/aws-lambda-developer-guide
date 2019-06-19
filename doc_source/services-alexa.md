# Using AWS Lambda with Alexa<a name="services-alexa"></a>

You can use Lambda functions to build services that give new skills to Alexa, the Voice assistant on Amazon Echo\. The Alexa Skills Kit provides the APIs, tools, and documentation to create these new skills, powered by your own services running as Lambda functions\. Amazon Echo users can access these new skills by asking Alexa questions or making requests\.

The Alexa Skills Kit is available on GitHub\.
+ [Alexa Skills Kit SDK for Node\.js ](https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs)
+ [Alexa Skills Kit SDK for Java](https://github.com/alexa/alexa-skills-kit-sdk-for-java)

**Example Alexa Smart Home Event**  

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

For more information, see [Getting Started with Alexa Skills Kit](https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/getting-started-guide)\.