#!/bin/bash
set -eo pipefail

if [ $1 ]
then
  if [ $1 = mvn ]
  then
    mvn prepare-package
  fi
else
  gradle -q packageLibs
  mv build/distributions/blank-java.zip build/blank-java-lib.zip
fi