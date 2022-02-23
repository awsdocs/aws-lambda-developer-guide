# Using AVX2 vectorization in Lambda<a name="runtimes-avx2"></a>

Advanced Vector Extensions 2 \(AVX2\) is a vectorization extension to the Intel x86 instruction set that can perform single instruction multiple data \(SIMD\) instructions over vectors of 256 bits\. For vectorizable algorithms with [highly parallelizable](https://en.wikipedia.org/wiki/Massively_parallel) operation, using AVX2 can enhance CPU performance, resulting in lower latencies and higher throughput\. Use the AVX2 instruction set for compute\-intensive workloads such as machine learning inferencing, multimedia processing, scientific simulations, and financial modeling applications\.

**Note**  
Lambda arm64 uses NEON SIMD architecture and does not support the x86 AVX2 extensions\.

To use AVX2 with your Lambda function, make sure that your function code is accessing AVX2\-optimized code\. For some languages, you can install the AVX2\-supported version of libraries and packages\. For other languages, you can recompile your code and dependencies with the appropriate compiler flags set \(if the compiler supports auto\-vectorization\)\. You can also compile your code with third\-party libraries that use AVX2 to optimize math operations\. For example, Intel Math Kernel Library \(Intel MKL\), OpenBLAS \(Basic Linear Algebra Subprograms\), and AMD BLAS\-like Library Instantiation Software \(BLIS\)\. Auto\-vectorized languages, such as Java, automatically use AVX2 for computations\.

You can create new Lambda workloads or move existing AVX2\-enabled workloads to Lambda at no additional cost\.

For more information about AVX2, see [Advanced Vector Extensions 2](https://en.wikipedia.org/wiki/Advanced_Vector_Extensions#Advanced_Vector_Extensions_2) in Wikipedia\.

## Compiling from source<a name="runtimes-avx2-cpp"></a>

If your Lambda function uses a C or C\+\+ library to perform compute\-intensive vectorizable operations, you can set the appropriate compiler flags and recompile the function code\. Then, the compiler automatically vectorizes your code\.

For the `gcc` or `clang` compiler, add `-march=haswell` to the command or set `-mavx2` as a command option\.

```
~ gcc -march=haswell main.c
or
~ gcc -mavx2 main.c  
 
~ clang -march=haswell main.c
or
~ clang -mavx2 main.c
```

To use a specific library, follow instructions in the library's documentation to compile and build the library\. For example, to build TensorFlow from source, you can follow the [ installation instructions](https://www.tensorflow.org/install/source) on the TensorFlow website\. Make sure to use the `-march=haswell` compile option\.

## Enabling AVX2 for Intel MKL<a name="runtimes-avx2-mkl"></a>

Intel MKL is a library of optimized math operations that implicitly use AVX2 instructions when the compute platform supports them\. Frameworks such as PyTorch [build with Intel MKL by default](https://software.intel.com/content/www/us/en/develop/articles/getting-started-with-intel-optimization-of-pytorch.html), so you don't need to enable AVX2\.

Some libraries, such as TensorFlow, provide options in their build process to specify Intel MKL optimization\. For example, with TensorFlow, use the `--config=mkl` option\.

You can also build popular scientific Python libraries, such as SciPy and NumPy, with Intel MKL\. For instructions on building these libraries with Intel MKL, see [Numpy/Scipy with Intel MKL and Intel Compilers](https://software.intel.com/content/www/us/en/develop/articles/numpyscipy-with-intel-mkl.html) on the Intel website\.

For more information about Intel MKL and similar libraries, see [Math Kernel Library](https://en.wikipedia.org/wiki/Math_Kernel_Library) in Wikipedia, the [OpenBLAS website](https://www.openblas.net/), and the [AMD BLIS repository](https://github.com/amd/blis) on GitHub\.

## AVX2 support in other languages<a name="runtimes-avx2-mkl"></a>

If you don't use C or C\+\+ libraries and don't build with Intel MKL, you can still get some AVX2 performance improvement for your applications\. Note that the actual improvement depends on the compiler or interpreter's ability to utilize the AVX2 capabilities on your code\.



Python  
Python users generally use SciPy and NumPy libraries for compute\-intensive workloads\. You can compile these libraries to enable AVX2, or you can use the Intel MKL\-enabled versions of the libraries\.

Node  
For compute\-intensive workloads, use AVX2\-enabled or Intel MKL\-enabled versions of the libraries that you need\.

Java  
Java's JIT compiler can auto\-vectorize your code to run with AVX2 instructions\. For information about detecting vectorized code, see the [Code vectorization in the JVM](https://cr.openjdk.java.net/~vlivanov/talks/2019_CodeOne_MTE_Vectors.pdf) presentation on the OpenJDK website\.

Go  
The standard Go compiler doesn't currently support auto\-vectorization, but you can use [gccgo](https://golang.org/doc/install/gccgo), the GCC compiler for Go\. Set the `-mavx2` option:  

```
gcc -o avx2 -mavx2 -Wall main.c
```

Intrinsics  
It's possible to use [intrinsic functions](https://en.wikipedia.org/wiki/Intrinsic_function) in many languages to manually vectorize your code to use AVX2\. However, we don't recommend this approach\. Manually writing vectorized code takes significant effort\. Also, debugging and maintaining such code is more difficult than using code that depends on auto\-vectorization\.