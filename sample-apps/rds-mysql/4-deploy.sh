#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
STACK=rds-mysql
if [[ $# -eq 1 ]] ; then
    STACK=$1
    echo "Deploying to stack $STACK"
fi
cd lib/nodejs && npm install && cd ../../
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name $STACK --capabilities CAPABILITY_NAMED_IAM

# attach to different VPC
#aws cloudformation deploy --template-file out.yml --stack-name $STACK --capabilities CAPABILITY_NAMED_IAM --parameter-overrides vpcStackName=lambda-vpc secretName=lambda-db-password