#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name java-events --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
if [ $1 ]
then
  case $1 in
    apig)
      PAYLOAD='file://events/apigateway-v1.json'
      ;;
    cws)
      PAYLOAD='file://events/cloudwatch-scheduled.json'
      ;;
    cwl)
      PAYLOAD='file://events/cloudwatch-logs.json'
      ;;
    sns)
      PAYLOAD='file://events/sns-notification.json'
      ;;
    cfg)
      PAYLOAD='file://events/config-rule.json'
      ;;
    cc)
      PAYLOAD='file://events/codecommit-push.json'
      ;;
    cog)
      PAYLOAD='file://events/cognito-sync.json'
      ;;
    fh)
      PAYLOAD='file://events/firehose-record.json'
      ;;
    lex)
      PAYLOAD='file://events/lex-flowers.json'
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
