# Troubleshoot container image issues in Lambda<a name="troubleshooting-images"></a>

## Container: CodeArtifactUserException errors related to the code artifact\.<a name="troubleshooting-images-invoke"></a>

**Issue:** *CodeArtifactUserPendingException error message*

The CodeArtifact is pending optimization\. The function transitions to active state when Lambda completes the optimization\. HTTP response code 409\. 

**Issue:** *CodeArtifactUserDeletedException error message*

The CodeArtifact is scheduled to be deleted\. HTTP response code 409\.

**Issue:** *CodeArtifactUserFailedException error message*

Lambda failed to optimize the code\. You need to correct the code and upload it again\. HTTP response code 409\.

## Container: ManifestKeyCustomerException errors related to the code manifest key\.<a name="troubleshooting-images-keys"></a>

**Issue:** *KMSAccessDeniedException error message*

You do not have permissions to access the key to decrypt the manifest\. HTTP response code 502\.

**Issue:** *TooManyRequestsException error message*

The client is being throttled\. The current request rate exceeds the KMS subscription rate\. HTTP response code 429\.

**Issue:** *KMSNotFoundException error message*

Lambda cannot find the key to decrypt the manifest\. HTTP response code 502\.

**Issue:** *KMSDisabledException error message*

The key to decrypt the manifest is disabled\. HTTP response code 502\.

**Issue:** *KMSInvalidStateException error message*

The key is in a state \(such as pending deletion or unavailable\) such that Lambda cannot use the key to decrypt the manifest\. HTTP response code 502\.

## Container: Error occurs on runtime InvalidEntrypoint<a name="troubleshooting-images-entry"></a>

**Issue:** *You receive a Runtime\.ExitError error message, or an error message with `"errorType": "Runtime.InvalidEntrypoint"`\.*

Verify that the ENTRYPOINT to your container image includes the absolute path as the location\. Also verify that the image does not contain a symlink as the ENTRYPOINT\. 

## Lambda: System provisioning additional capacity<a name="troubleshooting-images-capacity"></a>

**Error:** *“Error: We currently do not have sufficient capacity in the region you requested\. Our system will be working on provisioning additional capacity\.*

Retry the function invocation\. If the retry fails, validate that the files required to run the function code can be read by any user\. Lambda defines a default Linux user with least\-privileged permissions\. You need to verify that your application code does not rely on files that are restricted by other Linux users for execution\. 

## CloudFormation: ENTRYPOINT is being overridden with a null or empty value<a name="troubleshooting-images-cfn"></a>

**Error:** *You are using an AWS CloudFormation template, and your container ENTRYPOINT is being overridden with a null or empty value\.*

Review the `ImageConfig` resource in the AWS CloudFormation template\. If you declare an `ImageConfig` resource in your template, you must provide non\-empty values for all three of the properties\.