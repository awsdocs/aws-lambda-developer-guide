#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name java17-examples --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
if [ $1 ]
then
  case $1 in
    string)
      PAYLOAD='"MYSTRING"'
      ;;

    int | integer)
      PAYLOAD=12345
      ;;

    list)
      PAYLOAD='[24,25,26]'
      ;;

    divide)
      PAYLOAD='[235241,17]'
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
