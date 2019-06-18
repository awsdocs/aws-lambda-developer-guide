# Creating a Deployment Package<a name="deployment-package-v2"></a>

To create a Lambda function you first create a Lambda function deployment package, a \.zip or \.jar file consisting of your code and any dependencies\. When creating the zip, include only the code and its dependencies, not the containing folder\. You will then need to set the appropriate security permissions for the zip package\.
+ [AWS Lambda Deployment Package in Node\.js](nodejs-create-deployment-pkg.md)
+ [AWS Lambda Deployment Package in Python](lambda-python-how-to-create-deployment-package.md)
+ [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)
+ [AWS Lambda Deployment Package in Go](lambda-go-how-to-create-deployment-package.md)
+ [AWS Lambda Deployment Package in C\#](lambda-dotnet-how-to-create-deployment-package.md)
+ [AWS Lambda Deployment Package in PowerShell](lambda-powershell-how-to-create-deployment-package.md)

## Permissions Policies on Lambda Deployment Packages<a name="lambda-zip-package-permission-policies"></a>

Zip packages uploaded with incorrect permissions may cause execution failure\. AWS Lambda requires global read permissions on code files and any dependent libraries that comprise your deployment package\. To ensure permissions are not restricted to your user account, you can check using the following samples:
+ **Linux/Unix/OSX environments**: Use `zipinfo` as shown in the sample below:

  ```
  $ zipinfo test.zip
  Archive:  test.zip
  Zip file size: 473 bytes, number of entries: 2
  -r--------  3.0 unx        0 bx stor 17-Aug-10 09:37 exlib.py
  -r--------  3.0 unx      234 tx defN 17-Aug-10 09:37 index.py
  2 files, 234 bytes uncompressed, 163 bytes compressed:  30.3%
  ```

  The `-r--------` indicates that only the file owner has read permissions, which can cause Lambda function execution failures\. The following indicates what you would see if there are requisite global read permissions:

  ```
  $ zipinfo test.zip
  Archive:  test.zip
  Zip file size: 473 bytes, number of entries: 2
  -r--r--r--  3.0 unx        0 bx stor 17-Aug-10 09:37 exlib.py
  -r--r--r--  3.0 unx      234 tx defN 17-Aug-10 09:37 index.py
  2 files, 234 bytes uncompressed, 163 bytes compressed:  30.3%
  ```

  To fix this recursively, run the following command:

  ```
  $ chmod 644 $(find /tmp/package_contents -type f)
  $ chmod 755 $(find /tmp/package_contents -type d)
  ```
  + The first command changes all files in `/tmp/package_contents` to have read/write permissions to owners, read to group and global\.
  + The second command cascades the same permissions for directories\.