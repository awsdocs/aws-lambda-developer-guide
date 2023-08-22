#!/bin/bash
BUCKET_ID=$(dd if=/dev/random bs=8 count=1 2>/dev/null | od -An -tx1 | tr -d ' \t\n')
BUCKET_NAME=lambda-artifacts-$BUCKET_ID
LAYER_BUCKET_NAME=$BUCKET_NAME-dotnet-layer
echo $BUCKET_NAME > bucket-name.txt
aws s3 mb s3://$BUCKET_NAME
aws s3 mb s3://$LAYER_BUCKET_NAME

aws iam create-role --role-name blank-csharp-role --assume-role-policy-document file://assume-policy.json
aws iam attach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
aws iam attach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/AWSLambda_ReadOnlyAccess
aws iam attach-role-policy --role-name blank-csharp-role --policy-arn arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess
