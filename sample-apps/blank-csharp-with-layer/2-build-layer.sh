#!/bin/bash
LAYER_BUCKET_NAME=$(cat bucket-name.txt)-dotnet-layer
cd src/blank-csharp
LAYER_ARN=$(dotnet lambda publish-layer blank-csharp-layer --layer-type runtime-package-store --s3-bucket "$LAYER_BUCKET_NAME" | tail -1 | cut -c 23-)
cd ../..
echo $LAYER_ARN > layer-arn.txt
