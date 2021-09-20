# Troubleshoot container image issues in Lambda<a name="troubleshooting-images"></a>

## Container: Error occurs on runtime InvalidEntrypoint<a name="troubleshooting-images-entry"></a>

**Issue:** *You receive a Runtime\.ExitError error message, or an error message with `"errorType": "Runtime.InvalidEntrypoint"`\.*

Verify that the ENTRYPOINT to your container image includes the absolute path as the location\. Also verify that the image does not contain a symlink as the ENTRYPOINT\. 

## Lambda: System provisioning additional capacity<a name="troubleshooting-images-capacity"></a>

**Error:** *“Error: We currently do not have sufficient capacity in the region you requested\. Our system will be working on provisioning additional capacity\.*

Retry the function invocation\. If the retry fails, validate that the files required to run the function code can be read by any user\. Lambda defines a default Linux user with least\-privileged permissions\. You need to verify that your application code does not rely on files that are restricted by other Linux users for execution\. 

## CloudFormation: ENTRYPOINT is being overridden with a null or empty value<a name="troubleshooting-images-cfn"></a>

**Error:** *You are using an AWS CloudFormation template, and your container ENTRYPOINT is being overridden with a null or empty value\.*

Review the `ImageConfig` resource in the AWS CloudFormation template\. If you declare an `ImageConfig` resource in your template, you must provide non\-empty values for all three of the properties\.