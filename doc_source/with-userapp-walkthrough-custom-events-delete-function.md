# Step 5: Delete the Lambda Function and IAM Role \(AWS CLI\)<a name="with-userapp-walkthrough-custom-events-delete-function"></a>

Execute the following `delete-function` command to delete the `helloworld` function\. 

```
$ aws lambda delete-function \
 --function-name helloworld \
 --region region \
--profile adminuser
```

## Delete the IAM Role<a name="with-userapp-walkthrough-custom-events-cleanup"></a>

After you delete the Lambda function you can also delete the IAM role you created in the IAM console\. For information about deleting a role, see [Deleting Roles or Instance Profiles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_manage_delete.html) in the *IAM User Guide*\. 