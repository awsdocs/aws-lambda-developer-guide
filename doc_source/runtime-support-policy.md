# Runtime Support Policy<a name="runtime-support-policy"></a>

AWS Lambda runtimes are built around a combination of operating system, programming language, and software libraries that are subject to maintenance and security updates\. When a component of a runtime is no longer supported for security updates, Lambda deprecates the runtime\.

Deprecation occurs in two phases\. During the first phase, you can no longer create functions that use the deprecated runtime\. For at least 30 days, you can continue to update existing functions that use the deprecated runtime\. After this period, both function creation and updates are disabled permanently\. However, the function continues to be available to process invocation events\.

In most cases, the end\-of\-life date of a language version or operating system is known well in advance\. If you have functions running on a runtime that will be deprecated in the next 60 days, Lambda will notify you by email that you should prepare by migrating your function to a supported runtime\. In some cases, such as security issues that require a backwards\-incompatible update, or software that doesn't support a long\-term support \(LTS\) schedule, advance notice may not be possible\.

After a runtime is deprecated, Lambda may retire it completely at any time by disabling invocation\. Deprecated runtimes are not eligible for security updates or technical support\. Prior to retiring a runtime, Lambda will send additional notifications to affected customers\. No runtimes are scheduled to be retired at this time\.


**Deprecated Runtimes**  

| Name | Identifier | End of Life | 
| --- | --- | --- | 
|  Node\.js 0\.10  |  `nodejs`  |  2016\-10\-31  | 
|  Node\.js 4\.3  |  `nodejs4.3` `nodejs4.3-edge`  |  2018\-04\-30  | 