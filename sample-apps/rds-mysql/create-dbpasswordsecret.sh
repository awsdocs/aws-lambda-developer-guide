#!/bin/bash
set -eo pipefail
DB_PASSWORD=$(dd if=/dev/random bs=8 count=1 2>/dev/null | od -An -tx1 | tr -d ' \t\n')
aws secretsmanager create-secret --name rds-mysql-admin --description "database password" --secret-string "{\"username\":\"admin\",\"password\":\"$DB_PASSWORD\"}"
