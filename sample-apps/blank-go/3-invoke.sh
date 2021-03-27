#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name blank-go --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  aws lambda invoke --function-name $FUNCTION --payload file://event.json out.json --cli-binary-format raw-in-base64-out
  cat out.json
  echo ""
  sleep 2
done
