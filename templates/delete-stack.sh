#!/bin/bash
set -eo pipefail
if [[ $# -eq 1 ]]; then
    TEMPLATE_NAME=$1
else
    echo "Usage: ./delete-stack.sh <template-name>"
    echo "e.g.   ./delete-stack.sh function-inline"
    exit 0
fi
STACK_NAME=lambda-${TEMPLATE_NAME}
while true; do
    read -p "Delete stack ${STACK_NAME}? (y/n)" response
    case $response in
        [Yy]* ) aws cloudformation delete-stack --stack-name ${STACK_NAME}; break;;
        [Nn]* ) break;;
        * ) echo "Response must start with y or n.";;
    esac
done