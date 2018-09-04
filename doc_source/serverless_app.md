# Using the AWS Serverless Application Model \(AWS SAM\)<a name="serverless_app"></a>

The AWS Serverless Application Model \(AWS SAM\) is a model to define serverless applications\. AWS SAM is natively supported by AWS CloudFormation and defines simplified syntax for expressing serverless resources\. The specification currently covers APIs, Lambda functions and Amazon DynamoDB tables\. SAM is available under Apache 2\.0 for AWS partners and customers to adopt and extend within their own toolsets\. For details on the specification, see the [AWS Serverless Application Model](https://github.com/awslabs/serverless-application-model)\.

## Serverless Resources Within AWS SAM<a name="serverless_app_resources"></a>

An AWS CloudFormation template with serverless resources conforming to the AWS SAM model is referred to as a SAM file or template\.

The examples following illustrate how to leverage AWS SAM to declare common components of a serverless application\. Note that the `Handler` and `Runtime` parameter values should match the ones you used when you created the function in the previous section\. 

### Lambda function<a name="serverless_app_function"></a>

The following shows the notation you use to describe a Lambda function:

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:

    FunctionName:
        Type: AWS::Serverless::Function
        Properties:
           Handler: index.handler
           Runtime: runtime
           CodeUri: s3://bucketName/codepackage.zip
```

The `handler` value of the `Handler` property points to the module containing the code your Lambda function will execute when invoked\. The `index` value of the `Handler` property indicates the name of the file containing the code\. You can declare as many functions as your serverless application requires\. 

You can also declare environment variables, which are configuration settings you can set for your application\. The following shows an example of a serverless app with two Lambda functions and an environment variable that points to a DynamoDB table\. You can update environment variables without needing to modify, repackage, or redeploy your Lambda function code\. For more information, see [Environment Variables](env_variables.md)\. 

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  PutFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs6.10
      Policies: AWSLambdaDynamoDBExecutionRole
      CodeUri: s3://bucketName/codepackage.zip
      Environment:
        Variables:
          TABLE_NAME: !Ref DynamoDBTable
  DeleteFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs6.10
      Policies: AWSLambdaDynamoDBExecutionRole
      CodeUri: s3://bucketName/codepackage.zip
      Environment:
        Variables:
          TABLE_NAME: !Ref DynamoDBTable
      Events:
        Stream:
          Type: DynamoDB
          Properties:
            Stream: !GetAtt DynamoDBTable.StreamArn
            BatchSize: 100
            StartingPosition: TRIM_HORIZON

   DynamoDBTable:
     Type: AWS::DynamoDB::Table
     Properties: 
       AttributeDefinitions: 
         - AttributeName: id
           AttributeType: S
       KeySchema: 
         - AttributeName: id
           KeyType: HASH
       ProvisionedThroughput: 
         ReadCapacityUnits: 5
         WriteCapacityUnits: 5
       StreamSpecification:
         StreamViewType: streamview type
```

Note the notation at the top:

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
```

This is required in order to include objects defined by the AWS Serverless Application Model within an AWS CloudFormation template\.

### SimpleTable<a name="serverless_app_simple_table"></a>

`SimpleTable` is a resource that creates a DynamoDB table with a single\-attribute primary key\. You can use this simplified version if the data your serverless application is interacting with only needs to be accessed by a single\-valued key\. You could update the previous example to use a `SimpleTable`, as shown following:

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  TableName:                   
     Type: AWS::Serverless::SimpleTable
     Properties:
       PrimaryKey:
         Name: id
         Type: String
       ProvisionedThroughput:
         ReadCapacityUnits: 5
         WriteCapacityUnits: 5
```

### Events<a name="serverless_app_events"></a>

Events are AWS resources that trigger the Lambda function, such as an Amazon API Gateway endpoint or an Amazon SNS notification\. The `Events` property is an array, which allows you to set multiple events per function\. The following shows the notation you use to describe a Lambda function with a DynamoDB table as an event source:

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  FunctionName:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs6.10
      Events:
        Stream:
          Type: DynamoDB
          Properties:
            Stream: !GetAtt DynamoDBTable.StreamArn
            BatchSize: 100
            StartingPosition: TRIM_HORIZON   
  TableName:
    Type: AWS::DynamoDB::Table
    Properties: 
      AttributeDefinitions: 
        - AttributeName: id
          AttributeType: S
      KeySchema: 
        - AttributeName: id
          KeyType: HASH
      ProvisionedThroughput: 
        ReadCapacityUnits: 5
        WriteCapacityUnits: 5
```

As mentioned preceding, you can set multiple event sources that will trigger the Lambda function\. The example following shows a Lambda function that can be triggered by either an HTTP `PUT` or `POST` event\.

### API<a name="serverless_app_api"></a>

There are two ways to define an `API` using AWS SAM\. The following uses Swagger to configure the underlying Amazon API Gateway resources: 

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  Api: 
    Type: AWS::Serverless::Api
    Properties:
      StageName: prod
      DefinitionUri: swagger.yml
```

In the next example, the `AWS::Serverless::Api` resource type is implicity added from the union of `API` events defined on `AWS::Serverless::Function` resources\. 

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  GetFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.get
      Runtime: nodejs6.10
      CodeUri: s3://bucket/api_backend.zip
      Policies: AmazonDynamoDBReadOnlyAccess
      Environment:
        Variables:
          TABLE_NAME: !Ref Table
      Events:
        GetResource:
          Type: Api
          Properties:
            Path: /resource/{resourceId}
            Method: get
 
  PutFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.put
      Runtime: nodejs6.10
      CodeUri: s3://bucket/api_backend.zip
      Policies: AmazonDynamoDBFullAccess
      Environment:
        Variables:
          TABLE_NAME: !Ref Table
      Events:
        PutResource:
          Type: Api
          Properties:
            Path: /resource/{resourceId}
            Method: put
 
  DeleteFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.delete
      Runtime: nodejs6.10
      CodeUri: s3://bucket/api_backend.zip
      Policies: AmazonDynamoDBFullAccess
      Environment:
        Variables:
          TABLE_NAME: !Ref Table
      Events:
        DeleteResource:
          Type: Api
          Properties:
            Path: /resource/{resourceId}
            Method: delete
 
  Table:
    Type: AWS::Serverless::SimpleTable
```

In the example preceding, AWS CloudFormation will automatically generate an Amazon API Gateway `API` with the path `"/resource/{resourceId}"` and with the methods `GET`, `PUT` and `DELETE`\. 

### Permissions<a name="serverless_app_permissions"></a>

You can supply an Amazon Resource Name \(ARN\) for an AWS Identity and Access Management \(IAM\) role be used as this function's execution role, as shown following: 

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  FunctionName:
    Type: AWS::Serverless::Function
    Properties:
      Role:role arn
```

Alternatively, you could supply one or more managed policies to the Lambda function resource\. AWS CloudFormation will then create a new role with the managed policies plus the default Lambda basic execution policy\. 

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  FunctionName:
    Type: AWS::Serverless::Function
    Properties:
      Policies: AmazonDynamoDBFullAccess
```

If none of these are supplied, a default execution role is created with Lambda basic execution permissions\.

**Note**  
In addition to using the serverless resources, you can also use conventional AWS CloudFormation syntax for expressing resources in the same template\. Any resources not included in the current SAM model can still be created in the AWS CloudFormation template using AWS CloudFormation syntax\. In addition, you can use AWS CloudFormation syntax to express serverless resources as an alternative to using the SAM model\. For information about specifying a Lambda function using conventional CloudFormation syntax as part of your SAM template, see [AWS::Lambda::Function](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html) in the AWS CloudFormation User Guide\.

### <a name="w4ab1c19c17b4c16"></a>

For a list of complete serverless application examples, see [Examples of How to Use AWS Lambda](use-cases.md)\.

## Create a Simple App \(sam init\)<a name="serv-app-sam-init"></a>

To get started with a project in SAM, you can use the `sam init` command provided by the SAM CLI to get a fully deployable, boilerplate serverless application in any of the supported runtimes\. `sam init `provides a quick way for you to get started with creating a Lambda\-based application and augment into a production application by using other commands in the SAM CLI\. 

To use `sam init`, navigate to a directory where where you want the serverless application to be created\. Using the SAM CLI, run the following command \(using the runtime of your choice\. The following example uses Python for demonstration purposes\.\): 

```
$ sam init --runtime python
[+] Initializing project structure...
[SUCCESS] - Read sam-app/README.md for further instructions on how to proceed
[*] Project initialization is now complete
```

This will create a folder in the current directory titled *sam\-app*\. This folder will contain an AWS SAM template, along with your function code file and a README file that provides further guidance on how to proceed with your SAM application\. The SAM template defines the AWS Resources that your application will need to run in the AWS Cloud\.

The folder structure will include the following:
+ A `hello_world` directory that includes an app\.py file that contains sample code:

  ```
  import json
  
  import requests
  
  
  def lambda_handler(event, context):
      """Sample pure Lambda function
  
      Arguments:
          event LambdaEvent -- Lambda Event received from Invoke API
          context LambdaContext -- Lambda Context runtime methods and attributes
  
      Returns:
          dict -- {'statusCode': int, 'body': dict}
      """
  
      ip = requests.get('http://checkip.amazonaws.com/')
  
      return {
          "statusCode": 200,
          "body": json.dumps({
              'message': 'hello world',
              'location': ip.text.replace('\n', ''),
          })
      }
  ```
+ A sample `template` YAML file that describes your SAM application: 

  ```
  AWSTemplateFormatVersion: '2010-09-09'
  Transform: AWS::Serverless-2016-10-31
  Description: >
      sam-app
  
      Sample SAM Template for sam-app
      
  # More info about Globals: https://github.com/awslabs/serverless-application-model/blob/master/docs/globals.rst
  Globals:
      Function:
          Timeout: 3
  
  
  Resources:
  
      HelloWorldFunction:
          Type: AWS::Serverless::Function # More info about Function Resource: https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md#awsserverlessfunction
          Properties:
              CodeUri: hello_world/build/
              Handler: app.lambda_handler
              Runtime: python2.7
              Environment: # More info about Env Vars: https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md#environment-object
                  Variables:
                      PARAM1: VALUE
              Events:
                  HelloWorld:
                      Type: Api # More info about API Event Source: https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md#api
                      Properties:
                          Path: /hello
                          Method: get
  
  Outputs:
  
      HelloWorldApi:
        Description: "API Gateway endpoint URL for Prod stage for Hello World function"
        Value: !Sub "https://${ServerlessRestApi}.execute-api.${AWS::Region}.amazonaws.com/Prod/hello/"
  
      HelloWorldFunction:
        Description: "Hello World Lambda Function ARN"
        Value: !GetAtt HelloWorldFunction.Arn
  
      HelloWorldFunctionIamRole:
        Description: "Implicit IAM Role created for Hello World function"
        Value: !GetAtt HelloWorldFunctionRole.Arn
  ```

## Next Step<a name="serv-app-next-step"></a>

[Create Your Own Serverless Application](serverless-deploy-wt.md)