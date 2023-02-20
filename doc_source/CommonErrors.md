# Common Errors<a name="CommonErrors"></a>

This section lists the errors common to the API actions of all AWS services\. For errors specific to an API action for this service, see the topic for that API action\.

 **AccessDeniedException**   
You do not have sufficient access to perform this action\.  
HTTP Status Code: 400

 **IncompleteSignature**   
The request signature does not conform to AWS standards\.  
HTTP Status Code: 400

 **InternalFailure**   <a name="CommonErrors-InternalFailure"></a>
The request processing has failed because of an unknown error, exception or failure\.  
HTTP Status Code: 500

 **InvalidAction**   <a name="CommonErrors-InvalidAction"></a>
The action or operation requested is invalid\. Verify that the action is typed correctly\.  
HTTP Status Code: 400

 **InvalidClientTokenId**   <a name="CommonErrors-InvalidClientTokenId"></a>
The X\.509 certificate or AWS access key ID provided does not exist in our records\.  
HTTP Status Code: 403

 **InvalidParameterCombination**   <a name="CommonErrors-InvalidParameterCombination"></a>
Parameters that must not be used together were used together\.  
HTTP Status Code: 400

 **InvalidParameterValue**   <a name="CommonErrors-InvalidParameterValue"></a>
An invalid or out\-of\-range value was supplied for the input parameter\.  
HTTP Status Code: 400

 **InvalidQueryParameter**   <a name="CommonErrors-InvalidQueryParameter"></a>
The AWS query string is malformed or does not adhere to AWS standards\.  
HTTP Status Code: 400

 **MalformedQueryString**   <a name="CommonErrors-MalformedQueryString"></a>
The query string contains a syntax error\.  
HTTP Status Code: 404

 **MissingAction**   <a name="CommonErrors-MissingAction"></a>
The request is missing an action or a required parameter\.  
HTTP Status Code: 400

 **MissingAuthenticationToken**   <a name="CommonErrors-MissingAuthenticationToken"></a>
The request must contain either a valid \(registered\) AWS access key ID or X\.509 certificate\.  
HTTP Status Code: 403

 **MissingParameter**   <a name="CommonErrors-MissingParameter"></a>
A required parameter for the specified action is not supplied\.  
HTTP Status Code: 400

 **NotAuthorized**   <a name="CommonErrors-NotAuthorized"></a>
You do not have permission to perform this action\.  
HTTP Status Code: 400

 **OptInRequired**   <a name="CommonErrors-OptInRequired"></a>
The AWS access key ID needs a subscription for the service\.  
HTTP Status Code: 403

 **RequestExpired**   <a name="CommonErrors-RequestExpired"></a>
The request reached the service more than 15 minutes after the date stamp on the request or more than 15 minutes after the request expiration date \(such as for pre\-signed URLs\), or the date stamp on the request is more than 15 minutes in the future\.  
HTTP Status Code: 400

 **ServiceUnavailable**   <a name="CommonErrors-ServiceUnavailable"></a>
The request has failed due to a temporary failure of the server\.  
HTTP Status Code: 503

 **ThrottlingException**   <a name="CommonErrors-ThrottlingException"></a>
The request was denied due to request throttling\.  
HTTP Status Code: 400

 **ValidationError**   <a name="CommonErrors-ValidationError"></a>
The input fails to satisfy the constraints specified by an AWS service\.  
HTTP Status Code: 400