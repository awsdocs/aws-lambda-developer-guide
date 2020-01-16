#!/bin/bash
set -eo pipefail
DB_PASSWORD=$(dd if=/dev/random bs=8 count=1 2>/dev/null | od -An -tx1 | tr -d ' \t\n')
aws secretsmanager create-secret --name list-manager --description "List-manager database password" --secret-string "{\"password\":\"$DB_PASSWORD\"}"
