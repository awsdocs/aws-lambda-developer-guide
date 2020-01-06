#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.618.0'}
$region = $env:AWS_REGION
Write-Host 'Region:' $region
Write-Host 'Function name:' $LambdaContext.FunctionName
