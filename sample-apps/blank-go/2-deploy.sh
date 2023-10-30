#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
cd function
GOOS=linux GOARCH=amd64 go build main.go
cd ../
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name blank-go --capabilities CAPABILITY_NAMED_IAM
