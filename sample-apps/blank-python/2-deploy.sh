#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
cd function
rm -rf package
pip install --target ./package -r requirements.txt
cp lambda_function.py package
cd ../
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name blank-python --capabilities CAPABILITY_NAMED_IAM
