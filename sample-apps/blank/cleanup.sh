#!/bin/bash
set -eo pipefail
OUTPUT_BUCKET=$(aws cloudformation describe-stack-resource --stack-name blank --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
while true; do
    read -p "Delete output bucket ($OUTPUT_BUCKET)?" response
    case $response in
        [Yy]* ) aws s3 rb --force s3://$OUTPUT_BUCKET; aws cloudformation delete-stack --stack-name blank; break;;
        [Nn]* ) aws cloudformation delete-stack --stack-name blank --retain-resources bucket; exit;;
        * ) echo "Response must start with y or n.";;
    esac
done
echo "Deleted function stack"
aws cloudformation delete-stack --stack-name blank
if [ -f bucket-name.txt ]; then
    ARTIFACT_BUCKET=$(cat bucket-name.txt)
    while true; do
        read -p "Delete deployment artifacts and bucket ($ARTIFACT_BUCKET)?" response
        case $response in
            [Yy]* ) aws s3 rb --force s3://$ARTIFACT_BUCKET; rm bucket-name.txt; break;;
            [Nn]* ) exit;;
            * ) echo "Response must start with y or n.";;
        esac
    done
fi
rm -f deploy.sh out.yml
