#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name java-events-v1sdk --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
if [ $1 ]
then
  case $1 in
    ddb)
      PAYLOAD='file://events/dynamodb-record.json'
      ;;
    kin)
      PAYLOAD='file://events/kinesis-record.json'
      ;;
    *)
      echo -n "Unknown event type"
      ;;
  esac
fi
while true; do
  if [ $PAYLOAD ]
  then
    aws lambda invoke --function-name $FUNCTION --payload $PAYLOAD out.json
  else
    aws lambda invoke --function-name $FUNCTION --payload file://event.json out.json
  fi
  cat out.json
  echo ""
  sleep 2
done
