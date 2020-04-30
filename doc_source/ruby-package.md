# AWS Lambda deployment package in Ruby<a name="ruby-package"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Note**  
To keep your deployment package size small, package your function's dependencies in layers\. Layers let you manage your dependencies independently, can be used by multiple functions, and can be shared with other accounts\. For details, see [AWS Lambda layers](configuration-layers.md)\.

**Topics**
+ [Updating a function with no dependencies](#ruby-package-codeonly)
+ [Updating a function with additional dependencies](#ruby-package-dependencies)

## Updating a function with no dependencies<a name="ruby-package-codeonly"></a>

To update a function by using the Lambda API, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) operation\. Create an archive that contains your function code, and upload it using the AWS CLI\.

**To update a Ruby function with no dependencies**

1. Create a ZIP archive\.

   ```
   ~/my-function$ zip function.zip function.rb
   ```

1. Use the `update-function-code` command to upload the package\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

## Updating a function with additional dependencies<a name="ruby-package-dependencies"></a>

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
     adding: vendor/bundle/ruby/2.7.0/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/build_info/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/cache/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/cache/aws-eventstream-1.0.1.gem (deflated 36%)
   ...
   ```

1. Update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 300,
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```