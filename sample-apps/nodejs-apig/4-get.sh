#!/bin/bash
set -eo pipefail
APIID=$(aws cloudformation describe-stack-resource --stack-name nodejs-apig --logical-resource-id api --query 'StackResourceDetail.PhysicalResourceId' --output text)
REGION=$(aws configure get region)

curl https://$APIID.execute-api.$REGION.amazonaws.com/api/ -v