#!/bin/bash
set -eo pipefail
aws cloudformation deploy --template-file template-vpcefs.yml --stack-name efs-nodejs-vpc
