#!/bin/bash
set -eo pipefail
if [[ $# -gt 1 ]]; then
    TEMPLATE_NAME=$1
    shift
    OVERRIDES="$@"
    OVERRIDES_ARG="--parameter-overrides ${OVERRIDES}"
    TEMPLATE=$(cat ${TEMPLATE_NAME}.yml)
elif [[ $# -eq 1 ]]; then
    TEMPLATE_NAME=$1
    TEMPLATE=$(cat ${TEMPLATE_NAME}.yml)
    if [[ "$TEMPLATE" =~ PLACEHOLDER ]]; then
        echo "Usage: ./create-stack.sh <template-name> <parameters>"
        echo "e.g.   ./create-stack.sh my-template parameter=value parameter2=value2"
        exit 0
    fi
else
    echo "Usage: ./create-stack.sh <template-name> <parameters>"
    echo "e.g.   ./create-stack.sh function-inline"
    echo "e.g.   ./create-stack.sh my-template parameter=value parameter2=value2"
    exit 0
fi
STACK_NAME=lambda-${TEMPLATE_NAME}
if [[ "$TEMPLATE" =~ "AWS::IAM::Role" ]]; then
    CAPA_ARG="--capabilities CAPABILITY_NAMED_IAM"
fi
aws cloudformation deploy --template-file ${TEMPLATE_NAME}.yml --stack-name ${STACK_NAME} ${CAPA_ARG} ${OVERRIDES_ARG}