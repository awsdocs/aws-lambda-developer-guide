#!/bin/bash
aws cloudformation package --template-file error-processor.yaml --s3-bucket MY_BUCKET --output-template-file out.yaml
aws cloudformation deploy --template-file out.yaml --stack-name error-processor --capabilities CAPABILITY_NAMED_IAM
