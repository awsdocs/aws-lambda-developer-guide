#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
LAYER_ARN=$(cat layer-arn.txt)
cd src/blank-csharp
dotnet lambda deploy-function blank-csharp --function-layers $LAYER_ARN
cd ../../
