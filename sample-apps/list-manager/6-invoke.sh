#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name list-manager --logical-resource-id processor --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  aws lambda invoke --function-name $FUNCTION --payload file://events/kinesis.json out.json
  cat out.json
  echo ""
  sleep 2
done
