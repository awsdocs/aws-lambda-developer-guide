#!/bin/bash
aws cloudformation package --template-file error-processor.yaml --s3-bucket lambda-artifacts-a5e5b22e491dbb0d --output-template-file out.yaml
aws cloudformation deploy --template-file out.yaml --stack-name lambda-error-processor --capabilities CAPABILITY_NAMED_IAM
