#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name java-events --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
if [ $1 ]
then
  case $1 in
    apig)
      PAYLOAD='fileb://events/apigateway-v1.json'
      ;;
    cws)
      PAYLOAD='fileb://events/cloudwatch-scheduled.json'
      ;;
    cwl)
      PAYLOAD='fileb://events/cloudwatch-logs.json'
      ;;
    sns)
      PAYLOAD='fileb://events/sns-notification.json'
      ;;
    cdn)
      PAYLOAD='fileb://events/cloudfront.json'
      ;;
    cfg)
      PAYLOAD='fileb://events/config-rule.json'
      ;;
    cc)
      PAYLOAD='fileb://events/codecommit-push.json'
      ;;
    cog)
      PAYLOAD='fileb://events/cognito-sync.json'
      ;;
    kin)
      PAYLOAD='fileb://events/kinesis-record.json'
      ;;
    fh)
      PAYLOAD='fileb://events/firehose-record.json'
      ;;
    lex)
      PAYLOAD='fileb://events/lex-flowers.json'
      ;;
    ddb)
      PAYLOAD='fileb://events/dynamodb-record.json'
      ;;
    s3)
      PAYLOAD='fileb://events/s3-notification.json'
      ;;
    sqs)
      PAYLOAD='fileb://events/sqs-record.json'
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
    aws lambda invoke --function-name $FUNCTION --payload fileb://event.json out.json
  fi
  cat out.json
  echo ""
  sleep 2
done
