#!/bin/bash
set -eo pipefail
BUCKET=$(aws cloudformation describe-stack-resource --stack-name java-s3 --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
aws s3 cp images/sample-java-s3.png s3://$BUCKET/inbound/
