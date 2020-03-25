#!/bin/bash
set -eo pipefail
gradle -q packageLibs
mv build/distributions/s3-java.zip build/s3-java-lib.zip