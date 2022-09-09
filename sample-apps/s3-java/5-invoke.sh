#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name s3-java --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
BUCKET_NAME=$(aws cloudformation describe-stack-resource --stack-name s3-java --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)

if [ ! -f event.json ]; then
  cp event.json.template event.json
  sed -i'' -e "s/BUCKET_NAME/$BUCKET_NAME/" event.json

fi
while true; do
  aws lambda invoke --function-name $FUNCTION --payload file://event.json out.json
  cat out.json
  echo ""
  sleep 2
done
