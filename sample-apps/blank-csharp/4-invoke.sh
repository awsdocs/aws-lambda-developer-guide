#!/bin/bash
set -eo pipefail
if [[ $(aws --version) =~ "aws-cli/2." ]]; then PAYLOAD_PROTOCOL="fileb"; else  PAYLOAD_PROTOCOL="file"; fi;
while true; do
  aws lambda invoke --function-name blank-csharp --payload $PAYLOAD_PROTOCOL://event.json out.json
  cat out.json
  echo ""
  sleep 2
done
