#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name rds-mysql --logical-resource-id processor --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  aws lambda invoke --function-name $FUNCTION --payload file://events/db-read-table.json out
  sleep 2
done
