using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.Util;
using Amazon.Lambda;
using Amazon.Lambda.Model;
using Amazon.Lambda.Core;
using Amazon.Lambda.SQSEvents;
using Amazon.XRay.Recorder.Core;
using Amazon.XRay.Recorder.Handlers.AwsSdk;
using System.IO;

[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace blankCsharp
{
  public class Function
  {
        private AmazonLambdaClient lambdaClient;

        public Function()
        {
            initialize();
        }

        async void initialize()
        {
            AWSSDKHandler.RegisterXRayForAllServices();
            lambdaClient = new AmazonLambdaClient();
            await callLambda();
        }

        public async Task<AccountUsage> FunctionHandler(SQSEvent invocationEvent, ILambdaContext context)
        {
            GetAccountSettingsResponse accountSettings;
            try
            {
                accountSettings = await callLambda();
            }
            catch (AmazonLambdaException ex)
            {
                throw ex;
            }

            AccountUsage accountUsage = accountSettings.AccountUsage;
            MemoryStream logData = new MemoryStream();
            StreamReader logDataReader = new StreamReader(logData);

            Amazon.Lambda.Serialization.Json.JsonSerializer serializer = new Amazon.Lambda.Serialization.Json.JsonSerializer();

            serializer.Serialize<System.Collections.IDictionary>(System.Environment.GetEnvironmentVariables(), logData);
            LambdaLogger.Log("ENVIRONMENT VARIABLES: " + logDataReader.ReadLine());
            logData.Position = 0;
            serializer.Serialize<ILambdaContext>(context, logData);
            LambdaLogger.Log("CONTEXT: " + logDataReader.ReadLine());
            logData.Position = 0;
            serializer.Serialize<SQSEvent>(invocationEvent, logData);
            LambdaLogger.Log("EVENT: " + logDataReader.ReadLine());

            return accountUsage;
        }

        public async Task<GetAccountSettingsResponse> callLambda()
        {
            var request = new GetAccountSettingsRequest();
            var response = await lambdaClient.GetAccountSettingsAsync(request);
            return response;
        }
  }
}
