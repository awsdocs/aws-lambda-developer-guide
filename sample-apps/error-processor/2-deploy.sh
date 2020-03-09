#!/bin/bash
cd processor
ARTIFACT_BUCKET=$(cat bucket-name.txt)
npm install --production
cd ../random-error
npm install --production
cd ../
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name error-processor --capabilities CAPABILITY_NAMED_IAM
