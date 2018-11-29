# AWS Lambda Deployment Package in Ruby<a name="ruby-package"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if your code uses libraries other than the AWS SDK\. Other libraries and dependencies need to be included in the deployment package\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Topics**
+ [Updating a Function with No Dependencies](#ruby-package-codeonly)
+ [Updating a Function with Additional Dependencies](#ruby-package-dependencies)

## Updating a Function with No Dependencies<a name="ruby-package-codeonly"></a>

To create or update a function with the Lambda API, create an archive that contains your function code and upload it with the AWS CLI\.

**To update a Ruby function with no dependencies**

1. Create a ZIP archive\.

   ```
   ~/my-function$ zip function.zip function.rb
   ```

1. Use the `update-function-code` command to upload the package\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name ruby25 --zip-file fileb://function.zip
   {
       "FunctionName": "ruby25",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:ruby25",
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 300,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-23T21:00:10.248+0000",
       "CodeSha256": "Qf0haXm3JtGTmcDbjMc1I2di6YFMi9niEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "d1e983e3-ca8e-434b-8dc1-7add83d72ebd"
   }
   ```

## Updating a Function with Additional Dependencies<a name="ruby-package-dependencies"></a>

If your function depends on libraries other than the AWS SDK for Ruby, install them to a local directory with [Bundler](https://bundler.io/), and include them in your deployment package\.

**To update a Ruby function with dependencies**

1. Install libraries in the vendor directory with the `bundle` command\.

   ```
   ~/my-function$ bundle install --path vendor/bundle
   Fetching gem metadata from https://rubygems.org/..............
   Resolving dependencies...
   Fetching aws-eventstream 1.0.1
   Installing aws-eventstream 1.0.1
   ...
   ```

   The `--path` installs the gems in the project directory instead of the system location, and sets this as the default path for future installations\. To later install gems globally, use the `--system` option\.

1. Create a ZIP archive\.

   ```
   package$ zip -r function.zip function.rb vendor
     adding: function.rb (deflated 37%)
     adding: vendor/ (stored 0%)
     adding: vendor/bundle/ (stored 0%)
     adding: vendor/bundle/ruby/ (stored 0%)
     adding: vendor/bundle/ruby/2.5.0/ (stored 0%)
     adding: vendor/bundle/ruby/2.5.0/build_info/ (stored 0%)
     adding: vendor/bundle/ruby/2.5.0/cache/ (stored 0%)
     adding: vendor/bundle/ruby/2.5.0/cache/aws-eventstream-1.0.1.gem (deflated 36%)
   ...
   ```

1. Update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name ruby25 --zip-file fileb://function.zip
   {
       "FunctionName": "ruby25",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:ruby25",
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 998918,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-20T20:51:35.871+0000",
       "CodeSha256": "fJ3TxYnFosnnpN483dz9/rTzcXrbOiuu4iOZx34nXZI=",
       "Version": "$LATEST",
       "VpcConfig": {
           "SubnetIds": [],
           "SecurityGroupIds": [],
           "VpcId": ""
       },
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "9ca7c45b-bcda-4e51-ab5f-7c42fa916e39"
   }
   ```