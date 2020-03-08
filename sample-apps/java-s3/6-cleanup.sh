#!/bin/bash
set -eo pipefail
APP_BUCKET=$(aws cloudformation describe-stack-resource --stack-name java-s3 --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
aws cloudformation delete-stack --stack-name java-s3
echo "Deleted function stack"
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
while true; do
    read -p "Delete application bucket ($APP_BUCKET)?" response
    case $response in
        [Yy]* ) aws s3 rb --force s3://$APP_BUCKET; break;;
        [Nn]* ) break;;
        * ) echo "Response must start with y or n.";;
    esac
done
rm -f 3-deploy.sh out.yml out.json event.json
rm -rf build .gradle target
