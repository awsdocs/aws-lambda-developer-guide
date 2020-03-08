#!/bin/bash
set -eo pipefail
gradle -q packageLibs
mv build/distributions/java-s3.zip build/java-s3-lib.zip