# State machine application patterns<a name="stepfunctions-patterns"></a>

In Step Functions, you orchestrate your resources using state machines, which are defined using a JSON\-based, structured language called [Amazon States Language](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-amazon-states-language.html)\.

**Topics**
+ [State machine components](#statemachine-components)
+ [State machine application patterns](#stepfunctions-application-patterns)
+ [Applying patterns to state machines](#stepfunctions-patterns-state-machines)
+ [Example branching application pattern](#statemachine-example)

## State machine components<a name="statemachine-components"></a>

State machines contain elements called [states](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-states.html) that make up your workflow\. The logic of each state determines which state comes next, what data to pass along, and when to terminate the workflow\. A state is referred to by its name, which can be any string, but which must be unique within the scope of the entire state machine\.

To create a state machine that uses Lambda, you need the following components:

1. An AWS Identity and Access Management \(IAM\) role for Lambda with one or more permissions policies \(such as [AWSLambdaRole](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/service-role/AWSLambdaRole) service permissions\)\.

1. One or more Lambda functions \(with the IAM role attached\) for your specific runtime\.

1. A state machine authored in Amazon States Language\.

## State machine application patterns<a name="stepfunctions-application-patterns"></a>

You can create complex orchestrations for state machines using application patterns such as:
+ **Catch and retry** – Handle errors using sophisticated catch\-and\-retry functionality\.
+ **Branching** – Design your workflow to choose different branches based on Lambda function output\.
+ **Chaining** – Connect functions into a series of steps, with the output of one step providing the input to the next step\. 
+ **Parallelism** – Run functions in parallel, or use dynamic parallelism to invoke a function for every member of any array\. 

## Applying patterns to state machines<a name="stepfunctions-patterns-state-machines"></a>

The following shows how you can apply these application patterns to a state machine within an Amazon States Language definition\.

**Catch and Retry**  
A `Catch` field and a `Retry` field add catch\-and\-retry logic to a state machine\. [Catch](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-error-handling.html#error-handling-fallback-states) \(`"Type": "Catch"`\) is an array of objects that define a fallback state\. [Retry](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-error-handling.html#error-handling-retrying-after-an-error) \(`"Type": "Retry"`\) is an array of objects that define a retry policy if the state encounters runtime errors\.

**Branching**  
A `Choice` state adds branching logic to a state machine\. [Choice](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-choice-state.html) \(`"Type": "Choice"`\) is an array of rules that determine which state the state machine transitions to next\.

**Chaining**  
A "Chaining" pattern describes multiple Lambda functions connected together in a state machine\. You can use chaining to create reusable workflow invocations from a [Task](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-nested-workflows.html) \(`"Type": "Task"`\) state of a state machine\.

**Parallelism**  
A `Parallel` state adds parallelism logic to a state machine\. You can use a [Parallel](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-parallel-state.html) state \(`"Type": "Parallel"`\) to create parallel branches of invocation in your state machine\.

**Dynamic parallelism**  
A `Map` state adds dynamic "for\-each" loop logic to a state machine\. You can use a [Map](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-map-state.html) state \(`"Type": "Map"`\) to run a set of steps for each element of an input array in a state machine\. While the `Parallel` state invokes multiple branches of steps using the same input, a `Map` state invokes the same steps for multiple entries of the array\.

In addition to application patterns, Step Functions supports various [service integration patterns](https://docs.aws.amazon.com/step-functions/latest/dg/connect-to-resource.html), including the ability to pause a workflow for human approval, or to call a legacy system or other third party\.

## Example branching application pattern<a name="statemachine-example"></a>

In the following example, the `WhichCoat` state machine defined in the Amazon States Language \(ASL\) definition shows a branching application pattern with a [Choice](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-choice-state.html) state \(`"Type": "Choice"`\)\. If the condition of one of the three `Choice` states is met, the Lambda function is invoked as a [Task](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-task-state.html):

1. The `WearHeavyCoat` state invokes the `wear_heavy_coat` Lambda function and returns a message\.

1. The `WearLightJacket` state invokes the `wear_light_jacket` Lambda function and returns a message\.

1. The `None` state invokes the `no_jacket` Lambda function and returns a message\.

The `WhichCoat` state machine has the following structure:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-stepfunctions-wearcoat.png)

**Example Amazon States Language definition**  
The following Amazon States Language definition of the `WhichCoat` state machine uses a `Variable` [context object](https://docs.aws.amazon.com/step-functions/latest/dg/input-output-contextobject.html) called `Weather`\. If one of the three conditions in `StringEquals` is met, the Lambda function defined in the [`Resource` field's Amazon Resource Name \(ARN\)](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-task-state.html#amazon-states-language-task-state-specifying-resource-arns) is invoked\.  

```
{
        "Comment":"Coat Indicator State Machine",   
        "StartAt":"WhichCoat",   
        "States":{
            "WhichCoat":{
                "Type":"Choice",         
                "Choices":[
                {
                    "Variable":"$.Weather",               
                    "StringEquals":"FREEZING",                 
                    "Next":"WearHeavyCoat"            
                },            
                {
                    "Variable":"$.Weather",               
                    "StringEquals":"COOL",                 
                    "Next":"WearLightJacket"            
                },            
                {
                    "Variable":"$.Weather",               
                    "StringEquals":"WARM",                 
                    "Next":"None"            
                }
                ]
            },      
            "WearHeavyCoat":{
                "Type":"Task",         
                "Resource":"arn:aws:lambda:us-west-2:01234567890:function:wear_heavy_coat",         
                "End":true      
            },      
            "WearLightJacket":{
                "Type":"Task",         
                "Resource":"arn:aws:lambda:us-west-2:01234567890:function:wear_light_jacket",         
                "End":true      
            },      
            "None":{
                "Type":"Task",         
                "Resource":"arn:aws:lambda:us-west-2:01234567890:function:no_coat",         
                "End":true      
            }
        }
    }
```


**Example Python function**  
The following Lambda function in Python \(`wear_heavy_coat`\) can be invoked for the state machine defined in the previous example\. If the `WhichCoat` state machine equals a string value of `FREEZING`, the `wear_heavy_coat` function is invoked from Lambda, and the user receives the message that corresponds with the function: "You should wear a heavy coat today\."  

```
from __future__ import print_function

import datetime

        
def wear_heavy_coat(message, context):
    print(message)
        
    response = {}
    response['Weather'] = message['Weather']
    response['Timestamp'] = datetime.datetime.now().strftime("%Y-%m-%d %H-%M-%S")
    response['Message'] = 'You should wear a heavy coat today.'    
            
    return response
```

**Example invocation data**  
The following input data runs the `WearHeavyCoat` state that invokes the `wear_heavy_coat` Lambda function, when the `Weather` variable is equal to a string value of `FREEZING`\.  

```
{
    "Weather":"FREEZING"
}
```

For more information, see [Creating a Step Functions State Machine That Uses Lambda](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-creating-lambda-state-machine.html) in the *AWS Step Functions Developer Guide*\.