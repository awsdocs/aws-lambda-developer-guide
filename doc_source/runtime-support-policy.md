# Runtime Support Policy<a name="runtime-support-policy"></a>

[AWS Lambda runtimes](lambda-runtimes.md) are built around a combination of operating system, programming language, and software libraries that are subject to maintenance and security updates\. When a component of a runtime is no longer supported for security updates, Lambda deprecates the runtime\.

Deprecation occurs in two phases\. During the first phase, you can no longer create functions that use the deprecated runtime\. For at least 30 days, you can continue to update existing functions that use the deprecated runtime\. After this period, both function creation and updates are disabled permanently\. However, the function continues to be available to process invocation events\.


**Deprecation Schedule**  

| Name | Identifier | End of Life | Deprecation \(Create\) | Deprecation \(Update\) | 
| --- | --- | --- | --- | --- | 
|  Node\.js 0\.10  |  `nodejs`  |  October 31, 2016  |  October 31, 2016  |  October 31, 2016  | 
|  Node\.js 4\.3  |  `nodejs4.3` `nodejs4.3-edge`  |  April 30, 2018  |  December 15, 2018  |  April 30, 2019  | 
|  Node\.js 6\.10  |  `nodejs6.10`  |  April 30, 2019  |  April 30, 2019  |  August 12, 2019  | 
|  \.NET Core 2\.0  |  `dotnetcore2.0`  |  April 30, 2019  |  April 30, 2019  |  May 30, 2019  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  June 27, 2019  |  June 27, 2019  |  July 31, 2019  | 
|  Node\.js 8\.10  |  `nodejs8.10`  |  December 31, 2019  |  January 6, 2020  |  February 3, 2020  | 

In most cases, the end\-of\-life date of a language version or operating system is known well in advance\. If you have functions running on a runtime that will be deprecated in the next 60 days, Lambda notifies you by email that you should prepare by migrating your function to a supported runtime\. In some cases, such as security issues that require a backwards\-incompatible update, or software that doesn't support a long\-term support \(LTS\) schedule, advance notice might not be possible\.

**Language and Framework Support Policies**
+ **Node\.js** – [github\.com](https://github.com/nodejs/Release#release-schedule)
+ **Python** – [devguide\.python\.org](https://devguide.python.org/#status-of-python-branches)
+ **Ruby** – [www\.ruby\-lang\.org](https://www.ruby-lang.org/en/downloads/branches/)
+ **Java** – [www\.oracle\.com](https://www.oracle.com/technetwork/java/java-se-support-roadmap.html)
+ **Go** – [golang\.org](https://golang.org/s/release)
+ **\.NET Core** – [dotnet\.microsoft\.com](https://dotnet.microsoft.com/platform/support/policy/dotnet-core)

After a runtime is deprecated, Lambda might retire it completely at any time by disabling invocation\. Deprecated runtimes aren't eligible for security updates or technical support\. Before retiring a runtime, Lambda sends additional notifications to affected customers\. No runtimes are scheduled to be retired at this time\.