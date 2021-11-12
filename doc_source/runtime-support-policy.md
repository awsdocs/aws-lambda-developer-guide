# Runtime support policy<a name="runtime-support-policy"></a>

[Lambda runtimes](lambda-runtimes.md) for \.zip file archives are built around a combination of operating system, programming language, and software libraries that are subject to maintenance and security updates\. When security updates are no longer available for a component of a runtime, Lambda deprecates the runtime\.

Deprecation \(end of support\) for a runtime occurs in two phases\. In phase 1, Lambda no longer applies security patches or other updates to the runtime\. You can no longer create functions that use the runtime, but you can continue to update existing functions\. This includes updating the runtime version, and rolling back to the previous runtime version\. Note that functions that use a deprecated runtime are no longer eligible for technical support\.

In phase 2, which starts at least 30 days after the start of phase 1, you can no longer create or update functions that use the runtime\. To update a function, you need to migrate it to a supported runtime version\. After you migrate the function to a supported runtime version, you cannot rollback the function to the previous runtime\.

Lambda does not block invocations of functions that use deprecated runtime versions\. Function invocations continue indefinitely after the runtime version reaches end of support\. However, AWS strongly recommends that you migrate functions to a supported runtime version so that you continue to receive security patches and remain eligible for technical support\.

**Important**  
Python 2\.7 reached end of life on January 1, 2020\. End of support \(phase 1\) for the Python 2\.7 runtime started on July 15, 2021\. For more information, see [Announcing end of support for Python 2\.7 in AWS Lambda](http://aws.amazon.com/blogs/compute/announcing-end-of-support-for-python-2-7-in-aws-lambda/) on the AWS Compute Blog\.

In the table below, each of the phases starts at midnight \(Pacific time zone\) on the specified date\. The following runtimes have reached or are scheduled for end of support:


**Runtime end of support dates**  

| Name | Identifier | Operating system | End of support phase 1 start | End of support phase 2 start | 
| --- | --- | --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  Amazon Linux  |  Oct 26, 2021  |  Nov 30, 2021  | 
|  Python 2\.7  |  `python2.7`  |  Amazon Linux  |  July 15, 2021  |  Nov 22, 2021  | 
|  Ruby 2\.5  |  `ruby2.5`  |  Amazon Linux  |  July 30, 2021  |  Nov 30, 2021  | 
|  Node\.js 10\.x  |  `nodejs10.x`  |  Amazon Linux 2  |  July 30, 2021  |  Nov 30, 2021  | 
|  Node\.js 8\.10  |  `nodejs8.10`  |  Amazon Linux  |     |  March 6, 2020  | 
|  Node\.js 6\.10  |  `nodejs6.10`  |  Amazon Linux  |     |  August 12, 2019  | 
|  Node\.js 4\.3 edge  |  `nodejs4.3-edge`  |  Amazon Linux  |     |  April 30, 2019  | 
|  Node\.js 4\.3  |  `nodejs4.3`  |  Amazon Linux  |     |  March 6, 2020  | 
|  Node\.js 0\.10  |  `nodejs`  |  Amazon Linux  |     |  October 31, 2016  | 
|  \.NET Core 2\.0  |  `dotnetcore2.0`  |  Amazon Linux  |     |  May 30, 2019  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  Amazon Linux  |     |  July 30, 2019  | 

In almost all cases, the end\-of\-life date of a language version or operating system is known well in advance\. Lambda notifies you by email if you have functions using a runtime that is scheduled for end of support in the next 60 days\. In rare cases, advance notice of support ending might not be possible\. For example, security issues that require a backwards\-incompatible update, or a runtime component that doesn't provide a long\-term support \(LTS\) schedule\.

**Language and framework support policies**
+ **Node\.js** – [github\.com](https://github.com/nodejs/Release#release-schedule)
+ **Python** – [devguide\.python\.org](https://devguide.python.org/#status-of-python-branches)
+ **Ruby** – [www\.ruby\-lang\.org](https://www.ruby-lang.org/en/downloads/branches/)
+ **Java** – [www\.oracle\.com](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) and [Corretto FAQs](http://aws.amazon.com/corretto/faqs/)
+ **Go** – [golang\.org](https://golang.org/doc/devel/release.html)
+ **\.NET Core** – [dotnet\.microsoft\.com](https://dotnet.microsoft.com/platform/support/policy/dotnet-core)