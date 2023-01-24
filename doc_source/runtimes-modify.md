# Modifying the runtime environment<a name="runtimes-modify"></a>

You can use [internal extensions](lambda-extensions.md) to modify the runtime process\. Internal extensions are not separate processes—they run as part of the runtime process\.

Lambda provides language\-specific [environment variables](configuration-envvars.md) that you can set to add options and tools to the runtime\. Lambda also provides [wrapper scripts](#runtime-wrapper), which allow Lambda to delegate the runtime startup to your script\. You can create a wrapper script to customize the runtime startup behavior\.

## Language\-specific environment variables<a name="runtimes-envvars"></a>

Lambda supports configuration\-only ways to enable code to be pre\-loaded during function initialization through the following language\-specific environment variables:
+ `JAVA_TOOL_OPTIONS` – On Java, Lambda supports this environment variable to set additional command\-line variables in Lambda\. This environment variable allows you to specify the initialization of tools, specifically the launching of native or Java programming language agents using the `agentlib` or `javaagent` options\.
+ `NODE_OPTIONS` – On Node\.js 10x and above, Lambda supports this environment variable\.
+ `DOTNET_STARTUP_HOOKS` – On \.NET Core 3\.1 and above, this environment variable specifies a path to an assembly \(dll\) that Lambda can use\.

Using language\-specific environment variables is the preferred way to set startup properties\.

### Example: Intercept Lambda invokes with `javaagent`<a name="runtimes-envvars-ex1"></a>

The Java virtual machine \(JVM\) tries to locate the class that was specified with the `javaagent` parameter to the JVM, and invoke its `premain` method before the application's entry point\.

The following example uses [Byte Buddy](https://bytebuddy.net/), a library for creating and modifying Java classes during the runtime of a Java application without the help of a compiler\. Byte Buddy offers an additional API for generating Java agents\. In this example, the `Agent` class intercepts every call of the `handleRequest` method made to the [RequestStreamHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestStreamHandler.java) class\. This class is used internally in the runtime to wrap the handler invocations\.

```
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
            .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
            .type(ElementMatchers.isSubTypeOf(RequestStreamHandler.class))
            .transform((builder, typeDescription, classLoader, module) -> builder
                .method(ElementMatchers.nameContains("handleRequest"))
                .intercept(Advice.to(TimerAdvice.class)))
            .installOn(inst);
    }
}
```

The agent in the preceding example uses the `TimerAdvice` method\. `TimerAdvice` measures how many milliseconds are spent with the method call and logs the method time and details, such as name and passed arguments\.

```
import static net.bytebuddy.asm.Advice.AllArguments;
import static net.bytebuddy.asm.Advice.Enter;
import static net.bytebuddy.asm.Advice.OnMethodEnter;
import static net.bytebuddy.asm.Advice.OnMethodExit;
import static net.bytebuddy.asm.Advice.Origin;

public class TimerAdvice {

    @OnMethodEnter
    static long enter() {
        return System.currentTimeMillis();
    }

    @OnMethodExit
    static void exit(@Origin String method, @Enter long start, @AllArguments Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
            sb.append(", ");
        }
        System.out.println(method + " method with args: " + sb.toString() + " took " + (System.currentTimeMillis() - start) + " milliseconds ");
    }
}
```

The `TimerAdvice` method above has the following dependencies\.

```
*'com.amazonaws'*, *name*: *'aws-lambda-java-core'*, *version*: *'1.2.1'*
*'net.bytebuddy'*, *name*: *'byte-buddy-dep'*, *version*: *'1.10.14'*
*'net.bytebuddy'*, *name*: *'byte-buddy-agent'*, *version*: *'1.10.14'*
```

After you create a layer that contains the agent JAR, you can pass the JAR name to the runtime's JVM by setting an environment variable\.

```
JAVA_TOOL_OPTIONS=-javaagent:"/opt/ExampleAgent-0.0.jar"
```

After invoking the function with `{key=lambdaInput}`, you can find the following line in the logs:

```
public java.lang.Object lambdainternal.EventHandlerLoader$PojoMethodRequestHandler.handleRequest
 (java.lang.Object,com.amazonaws.services.lambda.runtime.Context) method with args:
  {key=lambdaInput}, lambdainternal.api.LambdaContext@4d9d1b69, took 106 milliseconds
```

### Example: Adding a shutdown hook to the JVM runtime process<a name="runtimes-envvars-ex2"></a>

When an extension is registered during a `Shutdown` event, the runtime process gets up to 500 ms to handle graceful shutdown\. You can hook into the runtime process, and when the JVM begins its shutdown process, it starts all registered hooks\. To register a shutdown hook, you must [register as an extension](runtimes-extensions-api.md#runtimes-extensions-registration-api)\. You do not need to explicitly register for the `Shutdown` event, as that is automatically sent to the runtime\.

```
import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
//      Register the extension.
//      ...

//      Register the shutdown hook
        addShutdownHook();
    }

    private static void addShutdownHook() {
//      Shutdown hooks get up to 500 ms to handle graceful shutdown before the runtime is terminated.
//
//      You can use this time to egress any remaining telemetry, close open database connections, etc.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            Inside the shutdown hook's thread we can perform any remaining task which needs to be done.
        }));
    }

}
```

### Example: Retrieving the InvokedFunctionArn<a name="runtimes-envvars-ex3"></a>

```
 @OnMethodEnter
    static long enter() {
        String invokedFunctionArn = null;
        for (Object arg : args) {
            if (arg instanceof Context) {
                Context context = (Context) arg;
                invokedFunctionArn = context.getInvokedFunctionArn();
            }
        }
    }
```

## Wrapper scripts<a name="runtime-wrapper"></a>

You can create a *wrapper script* to customize the runtime startup behavior of your Lambda function\. A wrapper script enables you to set configuration parameters that cannot be set through language\-specific environment variables\.

**Note**  
Invocations may fail if the wrapper script does not successfully start the runtime process\.

The following [Lambda runtimes](lambda-runtimes.md) support wrapper scripts:
+ Node\.js 14\.x
+ Node\.js 12\.x
+ Node\.js 10\.x
+ Python 3\.9
+ Python 3\.8
+ Ruby 2\.7
+ Java 11
+ Java 8 \(`java8.al2`\)
+ \.NET 6
+ \.NET 5
+ \.NET Core 3\.1

When you use a wrapper script for your function, Lambda starts the runtime using your script\. Lambda sends to your script the path to the interpreter and all of the original arguments for the standard runtime startup\. Your script can extend or transform the startup behavior of the program\. For example, the script can inject and alter arguments, set environment variables, or capture metrics, errors, and other diagnostic information\.

You specify the script by setting the value of the `AWS_LAMBDA_EXEC_WRAPPER` environment variable as the file system path of an executable binary or script\.

### Example: Create and use a wrapper script with Python 3\.8<a name="runtime-wrapper-example"></a>

In the following example, you create a wrapper script to start the Python interpreter with the `-X importtime` option\. When you run the function, Lambda generates a log entry to show the duration of the import time for each import\.

**To create and use a wrapper script with Python 3\.8**

1. To create the wrapper script, paste the following code into a file named `importtime_wrapper`:

   ```
     #!/bin/bash
   
     # the path to the interpreter and all of the originally intended arguments
     args=("$@")
   
     # the extra options to pass to the interpreter
     extra_args=("-X" "importtime")
   
     # insert the extra options
     args=("${args[@]:0:$#-1}" "${extra_args[@]}" "${args[@]: -1}")
   
     # start the runtime with the extra options
     exec "${args[@]}"
   ```

1. To give the script executable permissions, enter `chmod +x importtime_wrapper` from the command line\.

1. Deploy the script as a [Lambda layer](configuration-layers.md)\.

1. Create a function using the Lambda console\.

   1. Open the [Lambda console](https://console.aws.amazon.com/lambda/home)\.

   1. Choose **Create function**\.

   1. Under **Basic information**, for **Function name**, enter **wrapper\-test\-function**\.

   1. For **Runtime**, choose **Python 3\.8**\.

   1. Choose **Create function**\.

1. Add the layer to your function\.

   1. Choose your function, and then choose **Code** if it is not already selected\.

   1. Choose **Add a layer**\.

   1. Under **Choose a layer**, choose the **Name** and **Version** of the compatible layer that you created earlier\.

   1. Choose **Add**\.

1. Add the code and the environment variable to your function\.

   1. In the function [code editor](foundation-console.md#code-editor), paste the following function code:

      ```
      import json
      
        def lambda_handler(event, context):
            # TODO implement
            return {
                'statusCode': 200,
                'body': json.dumps('Hello from Lambda!')
            }
      ```

   1. Choose **Save**\.

   1. Under **Environment variables**, choose **Edit**\.

   1. Choose **Add environment variable**\.

   1. For **Key**, enter `AWS_LAMBDA_EXEC_WRAPPER`\.

   1. For **Value**, enter `/opt/importtime_wrapper`\.

   1. Choose **Save**\.

1. To run the function, choose **Test**\.

   Because your wrapper script started the Python interpreter with the `-X importtime` option, the logs show the time required for each import\. For example:

   ```
     ...
     2020-06-30T18:48:46.780+01:00 import time: 213 | 213 | simplejson
     2020-06-30T18:48:46.780+01:00 import time: 50 | 263 | simplejson.raw_json
     ...
   ```