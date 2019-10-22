#!/bin/bash
aws cloudformation package --template-file error-processor.yaml --s3-bucket BUCKET_NAME --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name error-processor --capabilities CAPABILITY_NAMED_IAM
