require 'json'
$region = ENV['AWS_REGION']

def lambda_handler(event:, context:)
    puts 'Region:'
    puts $region
    puts 'All environment variables:'
    puts ENV.to_a
    { status: 'success!' }
end
