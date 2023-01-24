# Lambda runtimes<a name="lambda-runtimes"></a>

Lambda supports multiple languages through the use of [runtimes](gettingstarted-concepts.md#gettingstarted-concepts-runtime)\. For a [function defined as a container image](gettingstarted-images.md), you choose a runtime and the Linux distribution when you [create the container image](images-create.md)\. To change the runtime, you create a new container image\.

When you use a \.zip file archive for the deployment package, you choose a runtime when you create the function\. To change the runtime, you can [update your function's configuration](configuration-function-zip.md)\. The runtime is paired with one of the Amazon Linux distributions\. The underlying execution environment provides additional libraries and [environment variables](configuration-envvars.md) that you can access from your function code\.

**Amazon Linux 2**
+ Image – Custom
+ Linux kernel – 4\.14

**Amazon Linux**
+ Image – [amzn\-ami\-hvm\-2018\.03\.0\.20220802\.0\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2018.03.0.20220802.0-x86_64-gp2)
+ Linux kernel – 4\.14

Lambda invokes your function in an [execution environment](lambda-runtime-environment.md)\. The execution environment provides a secure and isolated runtime environment that manages the resources required to run your function\. Lambda re\-uses the execution environment from a previous invocation if one is available, or it can create a new execution environment\. 

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](#runtime-support-policy) when the version reaches end of life\.

To use other languages in Lambda, you can implement a [custom runtime](runtimes-custom.md)\. The Lambda execution environment provides a [runtime interface](runtimes-api.md) for getting invocation events and sending responses\. You can deploy a custom runtime alongside your function code, or in a [layer](configuration-layers.md)\.

**Note**  
For new regions, Lambda will not support runtimes that are set to be deprecated within the next six months\.


**Supported Runtimes**  

| Name | Identifier | SDK | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | --- | 
|  Node\.js 16  |  `nodejs16.x`  |  2\.1083\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 14  |  `nodejs14.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 
|  Python 3\.9  |  `python3.9`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Python 3\.8  |  `python3.8`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux  |  x86\_64  |    | 
|  Java 11  |  `java11`  |    |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Java 8  |  `java8.al2`  |    |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Java 8  |  `java8`  |    |  Amazon Linux  |  x86\_64  |    | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |    |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 
|  \.NET 6  |  `dotnet6`  |    |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  \.NET 5  |  `dotnet5.0`  |    |  Amazon Linux 2  |  x86\_64  |    | 
|  Go 1\.x  |  `go1.x`  |    |  Amazon Linux  |  x86\_64  |    | 
|  Ruby 2\.7  |  `ruby2.7`  |  3\.1\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Custom Runtime  |  `provided.al2`  |    |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Custom Runtime  |  `provided`  |    |  Amazon Linux  |  x86\_64  |    | 

## Runtime deprecation policy<a name="runtime-support-policy"></a>

[Lambda runtimes](#lambda-runtimes) for \.zip file archives are built around a combination of operating system, programming language, and software libraries that are subject to maintenance and security updates\. When security updates are no longer available for a component of a runtime, Lambda deprecates the runtime\.

Deprecation \(end of support\) for a runtime occurs in two phases\.

Phase 1 \- Lambda no longer applies security patches or other updates to the runtime\. You can no longer **create** functions that use the runtime, but you can continue to update existing functions\. This includes updating the runtime version, and rolling back to the previous runtime version\. Note that functions that use a deprecated runtime are no longer eligible for technical support\.

Phase 2 \- you can no longer **create or update** functions that use the runtime\. To update a function, you need to migrate it to a supported runtime version\. After you migrate the function to a supported runtime version, you cannot rollback the function to the previous runtime\. Phase 2 starts at least 30 days after the start of Phase 1\.

Lambda does not block invocations of functions that use deprecated runtime versions\. Function invocations continue indefinitely after the runtime version reaches end of support\. However, AWS strongly recommends that you migrate functions to a supported runtime version so that you continue to receive security patches and remain eligible for technical support\.

In the table below, each phase starts at midnight \(Pacific time zone\) on the specified date\. The following runtimes have reached end of support:


**Deprecated runtimes**  

| Name | Identifier | Operating system | Deprecation Phase 1 | Deprecation Phase 2 | 
| --- | --- | --- | --- | --- | 
|  Python 3\.6  |  python3\.6  |  Amazon Linux  |  Jul 18, 2022  |  Aug 29, 2022  | 
|  Python 2\.7  |  python2\.7  |  Amazon Linux  |  Jul 15, 2021  |  May 30, 2022  | 
|  \.NET Core 2\.1  |  dotnetcore2\.1  |  Amazon Linux  |  Jan 5, 2022  |  Apr 13, 2022  | 
|  Ruby 2\.5  |  ruby2\.5  |  Amazon Linux  |  Jul 30, 2021  |  Mar 31, 2022  | 
|  Node\.js 10  |  nodejs10\.x  |  Amazon Linux 2  |  Jul 30, 2021  |  Feb 14, 2022  | 
|  Node\.js 8\.10  |  nodejs8\.10  |  Amazon Linux  |    |  Mar 6, 2020  | 
|  Node\.js 4\.3  |  nodejs4\.3  |  Amazon Linux  |    |  Mar 5, 2020  | 
|  Node\.js 6\.10  |  nodejs6\.10  |  Amazon Linux  |    |  Aug 12, 2019  | 
|  \.NET Core 1\.0  |  dotnetcore1\.0  |  Amazon Linux  |    |  Jul 30, 2019  | 
|  \.NET Core 2\.0  |  dotnetcore2\.0  |  Amazon Linux  |    |  May 30, 2019  | 
|  Node\.js 4\.3 edge  |  nodejs4\.3\-edge  |  Amazon Linux  |    |  Apr 30, 2019  | 
|  Node\.js 0\.10  |  nodejs  |  Amazon Linux  |    |  Oct 31, 2016  | 

In almost all cases, the end\-of\-life date of a language version or operating system is known well in advance\. Lambda notifies you by email if you have functions using a runtime that is scheduled for end of support in the next 60 days\. In rare cases, advance notice of support ending might not be possible\. For example, security issues that require a backwards\-incompatible update, or a runtime component that doesn't provide a long\-term support \(LTS\) schedule\.

**Language and framework support policies**
+ **Node\.js** – [github\.com](https://github.com/nodejs/Release#release-schedule)
+ **Python** – [devguide\.python\.org](https://devguide.python.org/#status-of-python-branches)
+ **Ruby** – [www\.ruby\-lang\.org](https://www.ruby-lang.org/en/downloads/branches/)
+ **Java** – [www\.oracle\.com](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) and [Corretto FAQs](http://aws.amazon.com/corretto/faqs/)
+ **Go** – [golang\.org](https://golang.org/doc/devel/release.html)
+ **\.NET Core** – [dotnet\.microsoft\.com](https://dotnet.microsoft.com/platform/support/policy/dotnet-core)

**Topics**
+ [Runtime deprecation policy](#runtime-support-policy)
+ [Modifying the runtime environment](runtimes-modify.md)
+ [Custom AWS Lambda runtimes](runtimes-custom.md)
+ [AWS Lambda runtime API](runtimes-api.md)
+ [Tutorial – Publishing a custom runtime](runtimes-walkthrough.md)
+ [Using AVX2 vectorization in Lambda](runtimes-avx2.md)