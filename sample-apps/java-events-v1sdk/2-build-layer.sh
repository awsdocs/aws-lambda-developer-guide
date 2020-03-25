#!/bin/bash
set -eo pipefail
gradle -q packageLibs
mv build/distributions/java-events-v1sdk.zip build/java-events-v1sdk-lib.zip