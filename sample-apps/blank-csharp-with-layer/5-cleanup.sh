#!/bin/bash
set -eo pipefail
echo "Deleting function blank-csharp"
aws iam detach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
aws iam detach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/AWSLambda_ReadOnlyAccess
aws iam detach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess
aws iam delete-role --role-name blank-csharp-role
aws lambda delete-function --function-name blank-csharp

if [ -f bucket-name.txt ]; then
    ARTIFACT_BUCKET=$(cat bucket-name.txt)
    if [[ ! $ARTIFACT_BUCKET =~ lambda-artifacts-[a-z0-9]{16} ]] ; then
        echo "Bucket was not created by this application. Skipping."
    else
        while true; do
            read -p "Delete deployment artifacts and bucket ($ARTIFACT_BUCKET)? (y/n)" response
            case $response in
                [Yy]* ) aws s3 rb --force s3://$ARTIFACT_BUCKET; break;;
                [Nn]* ) break;;
                * ) echo "Response must start with y or n.";;
            esac
        done
    fi
    LAYER_BUCKET=$(cat bucket-name.txt)-dotnet-layer
    if [[ ! $LAYER_BUCKET =~ lambda-artifacts-[a-z0-9]{16}-dotnet-layer ]] ; then
        echo "Layer bucket was not created by this application. Skipping."
    else
        while true; do
            read -p "Delete deployment artifacts and bucket ($LAYER_BUCKET)? (y/n)" response
            case $response in
                [Yy]* ) aws s3 rb --force s3://$LAYER_BUCKET; break;;
                [Nn]* ) break;;
                * ) echo "Response must start with y or n.";;
            esac
        done
    fi
fi
rm bucket-name.txt

while true; do
    read -p "Delete function log group (/aws/lambda/blank-csharp)? (y/n)" response
    case $response in
        [Yy]* ) aws logs delete-log-group --log-group-name /aws/lambda/blank-csharp; break;;
        [Nn]* ) break;;
        * ) echo "Response must start with y or n.";;
    esac
done

rm -f out.yml out.json layer-arn.txt
rm -rf src/blank-csharp/bin src/blank-csharp/obj