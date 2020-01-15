#!/bin/bash
set -eo pipefail
STREAM=$(aws cloudformation describe-stack-resource --stack-name list-manager --logical-resource-id stream --query 'StackResourceDetail.PhysicalResourceId' --output text)
aws kinesis put-record --stream-name $STREAM --data '{"title": "favorite movies", "user": "rdlysct", "type": "rank", "entries": {"blade runner": 2, "the empire strikes back": 3, "alien": 1}}' --partition-key 0
aws kinesis put-record --stream-name $STREAM --data '{"title": "stats", "user": "beth", "type": "tally", "entries": {"xp": 25}}' --partition-key 0
aws kinesis put-record --stream-name $STREAM --data '{"title": "favorite movies", "user": "mike", "type": "rank", "entries": {"blade runner": 1, "the empire strikes back": 2, "alien": 3}}' --partition-key 0
aws kinesis put-record --stream-name $STREAM --data '{"title": "stats", "user": "bill", "type": "tally", "entries": {"xp": 83}}' --partition-key 0