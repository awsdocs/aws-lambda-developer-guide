# Sample Function Code<a name="with-on-demand-https-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js](#with-apigateway-example-deployment-pkg-nodejs)
+ [Python 3](#with-apigateway-example-deployment-pkg-python)
+ [Go](#with-apigateway-example-deployment-pkg-go)

## Node\.js<a name="with-apigateway-example-deployment-pkg-nodejs"></a>

The following example processes messages from API Gateway, and manages DynamoDB documents based on the request method\.

**Example index\.js**  

```
console.log('Loading function');

var AWS = require('aws-sdk');
var dynamo = new AWS.DynamoDB.DocumentClient();

/**
 * Provide an event that contains the following keys:
 *
 *   - operation: one of the operations in the switch statement below
 *   - tableName: required for operations that interact with DynamoDB
 *   - payload: a parameter to pass to the operation being performed
 */
exports.handler = function(event, context, callback) {
    //console.log('Received event:', JSON.stringify(event, null, 2));

    var operation = event.operation;

    if (event.tableName) {
        event.payload.TableName = event.tableName;
    }

    switch (operation) {
        case 'create':
            dynamo.put(event.payload, callback);
            break;
        case 'read':
            dynamo.get(event.payload, callback);
            break;
        case 'update':
            dynamo.update(event.payload, callback);
            break;
        case 'delete':
            dynamo.delete(event.payload, callback);
            break;
        case 'list':
            dynamo.scan(event.payload, callback);
            break;
        case 'echo':
            callback(null, "Success");
            break;
        case 'ping':
            callback(null, "pong");
            break;
        default:
            callback('Unknown operation: ${operation}');
    }
};
```

Zip up the sample code to create a deployment package\. For instructions, see [Creating a Deployment Package \(Node\.js\)](nodejs-create-deployment-pkg.md)\.

## Python 3<a name="with-apigateway-example-deployment-pkg-python"></a>

The following example processes messages from API Gateway, and manages DynamoDB documents based on the request method\.

**Example LambdaFunctionOverHttps\.py**  

```
from __future__ import print_function

import boto3
import json

print('Loading function')


def handler(event, context):
    '''Provide an event that contains the following keys:

      - operation: one of the operations in the operations dict below
      - tableName: required for operations that interact with DynamoDB
      - payload: a parameter to pass to the operation being performed
    '''
    #print("Received event: " + json.dumps(event, indent=2))

    operation = event['operation']

    if 'tableName' in event:
        dynamo = boto3.resource('dynamodb').Table(event['tableName'])

    operations = {
        'create': lambda x: dynamo.put_item(**x),
        'read': lambda x: dynamo.get_item(**x),
        'update': lambda x: dynamo.update_item(**x),
        'delete': lambda x: dynamo.delete_item(**x),
        'list': lambda x: dynamo.scan(**x),
        'echo': lambda x: x,
        'ping': lambda x: 'pong'
    }

    if operation in operations:
        return operations[operation](event.get('payload'))
    else:
        raise ValueError('Unrecognized operation "{}"'.format(operation))
```

Zip up the sample code to create a deployment package\. For instructions, see [Creating a Deployment Package \(Python\)](lambda-python-how-to-create-deployment-package.md)\.

## Go<a name="with-apigateway-example-deployment-pkg-go"></a>

The following example processes messages from API Gateway, and logs information about the request\.

**Example LambdaFunctionOverHttps\.go**  

```
import (
    "strings"
    "github.com/aws/aws-lambda-go/events"
)

func handleRequest(ctx context.Context, request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
    fmt.Printf("Processing request data for request %s.\n", request.RequestContext.RequestId)
    fmt.Printf("Body size = %d.\n", len(request.Body))

    fmt.Println("Headers:")
    for key, value := range request.Headers {
        fmt.Printf("    %s: %s\n", key, value)
    }

    return events.APIGatewayProxyResponse { Body: request.Body, StatusCode: 200 }, nil
}
```

Build the executable with `go build` and create a deployment package\. For instructions, see [Creating a Deployment Package \(Go\)](lambda-go-how-to-create-deployment-package.md)\.