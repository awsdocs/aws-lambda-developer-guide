# Using environment variables<a name="golang-envvars"></a>

To access [environment variables](configuration-envvars.md) in Go, use the [Getenv](https://golang.org/pkg/os/#Getenv) function\.

The following explains how to do this\. Note that the function imports the [fmt](https://golang.org/pkg/fmt/) package to format the printed results and the [os](https://golang.org/pkg/os/) package, a platform\-independent system interface that allows you to access environment variables\.

```
package main

import (
	"fmt"
	"os"
	"github.com/aws/aws-lambda-go/lambda"
)

func main() {
	fmt.Printf("%s is %s. years old\n", os.Getenv("NAME"), os.Getenv("AGE"))

}
```

For a list of environment variables that are set by the Lambda runtime, see [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.