#!/bin/bash
OUTPUT_BUCKET=$(aws cloudformation describe-stack-resource --stack-name lambda-error-processor --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
while true; do
    read -p "Delete logs, traces, and output bucket ($OUTPUT_BUCKET)?" response
    case $response in
        [Yy]* ) aws s3 rb --force s3://$OUTPUT_BUCKET; aws cloudformation delete-stack --stack-name lambda-error-processor; break;;
        [Nn]* ) aws cloudformation delete-stack --stack-name lambda-error-processor --retain-resources bucket; exit;;
        * ) echo "Response must start with y or n.";;
    esac
done
if [ -f bucket-name.txt ]; then
    ARTIFACT_BUCKET=$(cat bucket-name.txt)
    sed -i "s/$ARTIFACT_BUCKET/MY_BUCKET/" deploy.sh
    while true; do
        read -p "Delete deployment artifacts and bucket ($ARTIFACT_BUCKET)?" response
        case $response in
            [Yy]* ) aws s3 rb --force s3://$ARTIFACT_BUCKET; rm bucket-name.txt; break;;
            [Nn]* ) exit;;
            * ) echo "Response must start with y or n.";;
        esac
    done
fi
