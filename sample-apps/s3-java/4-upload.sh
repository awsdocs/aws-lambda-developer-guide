#!/bin/bash
set -eo pipefail
BUCKET=$(aws cloudformation describe-stack-resource --stack-name s3-java --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
aws s3 cp images/sample-s3-java.png s3://$BUCKET/inbound/
