#!/bin/bash
set -eo pipefail
aws cloudformation delete-stack --stack-name blank-ruby
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name blank-ruby --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
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
    read -p "Delete function logs? (log group /aws/lambda/$FUNCTION)" response
    case $response in
        [Yy]* ) aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION; break;;
        [Nn]* ) break;;
        * ) echo "Response must start with y or n.";;
    esac
done
rm -f out.yml out.json
rm -rf lib
