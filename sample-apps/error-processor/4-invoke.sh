#!/bin/bash
ERROR_FUNCTION=$(aws cloudformation describe-stack-resource --stack-name error-processor --logical-resource-id randomerror --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  if [ $1 ]
  then
    case $1 in
      async)
        aws lambda invoke --function-name $ERROR_FUNCTION --payload file://event.json --invocation-type Event out.json
        ;;
      *)
        echo -n "Unknown argument"
        ;;
    esac
  else
    aws lambda invoke --function-name $ERROR_FUNCTION --payload file://event.json out.json
  fi
  cat out.json
  echo ""
  sleep 2
done