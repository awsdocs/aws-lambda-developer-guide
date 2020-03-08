#!/bin/bash
BUCKET_ID=$(dd if=/dev/random bs=8 count=1 2>/dev/null | od -An -tx1 | tr -d ' \t\n')
BUCKET_NAME=lambda-artifacts-$BUCKET_ID
echo $BUCKET_NAME > bucket-name.txt
aws s3 mb s3://$BUCKET_NAME
cp 3-deploy.sh.template 3-deploy.sh
sed -i'' -e "s/MY_BUCKET/$BUCKET_NAME/" 3-deploy.sh
