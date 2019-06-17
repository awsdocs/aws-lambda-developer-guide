#!/bin/bash
ERROR_FUNCTION=$(aws cloudformation describe-stack-resource --stack-name error-processor --logical-resource-id randomerror --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  aws lambda invoke --function-name $ERROR_FUNCTION --payload '{}' out
  sleep 2
done