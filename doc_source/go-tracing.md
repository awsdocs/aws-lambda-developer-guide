# Instrumenting Go Code in AWS Lambda<a name="go-tracing"></a>

You can use the [X\-Ray SDK for Go](https://github.com/aws/aws-xray-sdk-go) with your Lambda function\. If your handler includes [AWS Lambda Context Object in Go](go-programming-model-context.md) as its first argument, that object can be passed to the X\-Ray SDK\. Lambda passes values through this context that the SDK can use to attach subsegments to the Lambda invoke service segment\. Subsegments created with the SDK will appear as a part of your Lambda traces\. 

## Installing the X\-Ray SDK for Go<a name="go-tracing-installing-sdk"></a>

Use the following command to install the X\-Ray SDK for Go\. \(The SDK's non\-testing dependencies will be included\)\.

```
go get -u github.com/aws/aws-xray-sdk-go/...
```

If you want to include the test dependencies, use the following command:

```
go get -u -t github.com/aws/aws-xray-sdk-go/...
```

You can also use [Glide](https://github.com/Masterminds/glide/blob/master/README.md) to manage dependencies\.

```
glide install
```

## Configuring the X\-Ray SDK for Go<a name="go-tracing-configuring-sdk"></a>

The following code sample illustrates how to configure the X\-Ray SDK for Go in your Lambda function:

```
import (
  "github.com/aws/aws-xray-sdk-go/xray"
) 
func myHandlerFunction(ctx context.Context, sample string) {
  xray.Configure(xray.Config{    
    LogLevel:         "info",           // default
    ServiceVersion:   "1.2.3",
  })
   ... //remaining handler code
}
```

## Create a subsegment<a name="go-tracing-create-segment"></a>

The following code illustrates how to start a subsegment:

```
// Start a subsegment
  ctx, subSeg := xray.BeginSubsegment(ctx, "subsegment-name")
  // ...
  // Add metadata or annotation here if necessary
  // ...
  subSeg.Close(nil)
```

## Capture<a name="go-tracing-capture"></a>

The following code illustrates how to trace and capture a critical code path:

```
func criticalSection(ctx context.Context) {
  // This example traces a critical code path using a custom subsegment
  xray.Capture(ctx, "MyService.criticalSection", func(ctx1 context.Context) error {
    var err error

    section.Lock()
    result := someLockedResource.Go()
    section.Unlock()

    xray.AddMetadata(ctx1, "ResourceResult", result)
  })
}
```

## Tracing HTTP Requests<a name="go-tracing-use-client-method"></a>

You can also use the `xray.Client()` method if you want to trace an HTTP client, as shown below:

```
func myFunction (ctx context.Context) ([]byte, error) {
    resp, err := ctxhttp.Get(ctx, xray.Client(nil), "https://aws.amazon.com")
    if err != nil {
      return nil, err
    }
    return ioutil.ReadAll(resp.Body), nil
}
```