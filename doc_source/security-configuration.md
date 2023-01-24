# Configuration and vulnerability analysis in AWS Lambda<a name="security-configuration"></a>

AWS Lambda provides [runtimes](lambda-runtimes.md) that run your function code in an Amazon Linux–based execution environment\. Lambda is responsible for keeping software in the runtime and execution environment up to date, releasing new runtimes for new languages and frameworks, and deprecating runtimes when the underlying software is no longer supported\.

If you use additional libraries with your function, you're responsible for updating the libraries\. You can include additional libraries in the [deployment package](images-create.md), or in [layers](configuration-layers.md) that you attach to your function\. You can also build [custom runtimes](runtimes-custom.md) and use layers to share them with other accounts\.

Lambda deprecates runtimes when the software on the runtime or its execution environment reaches end of life\. When Lambda deprecates a runtime, you're responsible for migrating your functions to a supported runtime for the same language or framework\. For details, see [Runtime deprecation policy](lambda-runtimes.md#runtime-support-policy)\.