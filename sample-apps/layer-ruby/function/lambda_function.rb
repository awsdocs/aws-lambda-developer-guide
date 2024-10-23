require 'json'
require 'tzinfo'

def lambda_handler(event:, context:)
    tz = TZInfo::Timezone.get('America/New_York')
    { statusCode: 200, body: tz.to_local(Time.utc(2018, 2, 1, 12, 30, 0)) }
end

