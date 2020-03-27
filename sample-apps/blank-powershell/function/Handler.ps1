#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.618.0'}
$region = $env:AWS_REGION
# Process event
foreach ($message in $LambdaInput.Records)
{
    Write-Host $message.body
}
# Log environment details
# Environment variables
Write-Host 'Region:' $region
# Event
Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 5)
# Context
Write-Host (ConvertTo-Json -InputObject $LambdaContext -Compress -Depth 5)
Write-Host 'Function name:' $LambdaContext.FunctionName
# Call Lambda API
Get-LMAccountSetting | Select-Object -Property AccountUsage