# Runtime Support Policy<a name="runtime-support-policy"></a>

AWS Lambda will only deprecate runtime versions that are marked as EOL \(End of Life\) at the end of their maintenance window\. Versions that are marked deprecated will stop supporting creation of new functions and updates of existing functions authored in the deprecated runtime \(unless re\-configured to use a supported runtime version\)\. AWS Lambda will also not provide security updates, technical support or hotfixes for deprecated runtimes and reserves the right to disable invocations of functions configured to run on a deprecated runtime at any time\. 


**Deprecated Node\.js Runtimes**  

| Name | Identifier | End of Life | 
| --- | --- | --- | 
|  Node\.js 0\.10  |  `nodejs`  |  2016\-10\-31  | 
|  Node\.js 4\.3  |  `nodejs4.3` `nodejs4.3-edge`  |  2018\-04\-30  | 