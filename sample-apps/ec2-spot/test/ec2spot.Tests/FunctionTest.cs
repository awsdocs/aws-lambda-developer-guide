using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using Amazon.XRay.Recorder.Core;
using Amazon.XRay.Recorder.Core.Internal.Entities;
using Amazon.XRay.Recorder.Core.Exceptions;
using Amazon.XRay.Recorder.Core.Sampling;
using Amazon.XRay.Recorder.Core.Internal.Context;
using Amazon.XRay.Recorder.Core.Internal.Utils;

using Xunit;
using Amazon.Lambda.Core;
using Amazon.Lambda.TestUtilities;

using ec2spot;

namespace ec2spot.Tests
{
    public class TraceFixture : IDisposable
    {
        private static readonly String _traceHeaderValue = "Root=" + "1-5d66d2fe-8e6fcab805a0833803735bc8" + ";Parent=53995c3f42cd8ad8;Sampled=1";

        public TraceFixture()
        {
            Environment.SetEnvironmentVariable(AWSXRayRecorder.LambdaTaskRootKey, "test");
            Environment.SetEnvironmentVariable(AWSXRayRecorder.LambdaTraceHeaderKey, _traceHeaderValue);
            Environment.SetEnvironmentVariable("AWS_REGION", "us-east-2");
        }

        public void Dispose()
        {
            Environment.SetEnvironmentVariable(AWSXRayRecorder.LambdaTaskRootKey, null);
            Environment.SetEnvironmentVariable(AWSXRayRecorder.LambdaTraceHeaderKey, null);
            Environment.SetEnvironmentVariable("AWS_REGION", null);
        }
    }

    public class FunctionTest : IClassFixture<TraceFixture>
    {
        TraceFixture fixture;

        [Fact]
        public void TestFunction()
        {
            var function = new Function();
            var context = new TestLambdaContext();
            Dictionary<string, string> input = new Dictionary<string, string>();
            input.Add("key", "value");
            var task = function.FunctionHandler(input, context);
            task.Wait(7000);
            bool completed = task.IsCompleted;
            Assert.True(completed);
        }
    }
}
