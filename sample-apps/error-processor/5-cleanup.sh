#!/bin/bash
OUTPUT_BUCKET=$(aws cloudformation describe-stack-resource --stack-name error-processor --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
while true; do
    read -p "Delete logs, traces, and output bucket ($OUTPUT_BUCKET)?" response
    case $response in
        [Yy]* ) aws s3 rb --force s3://$OUTPUT_BUCKET; aws cloudformation delete-stack --stack-name error-processor; break;;
        [Nn]* ) aws cloudformation delete-stack --stack-name error-processor --retain-resources bucket; exit;;
        * ) echo "Response must start with y or n.";;
    esac
done
if [ -f bucket-name.txt ]; then
    ARTIFACT_BUCKET=$(cat bucket-name.txt)
    while true; do
        read -p "Delete deployment artifacts and bucket ($ARTIFACT_BUCKET)?" response
        case $response in
            [Yy]* ) aws s3 rb --force s3://$ARTIFACT_BUCKET; rm bucket-name.txt; break;;
            [Nn]* ) break;;
            * ) echo "Response must start with y or n.";;
        esac
    done
fi
rm -f out.yml out.json
rm -rf processor/node_modules random-error/node_modules processor/package-lock.json random-error/package-lock.json