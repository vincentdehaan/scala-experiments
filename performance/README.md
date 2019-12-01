Scala vs Java performance
===

## A motivating example

See `ArrayTestJava` and `ArrayTestScala` (Git commit `3898d1`). The Java version is considerably (+- 40%) faster. Compare the bytecode of the `map2` method:

```
JAVA:                   SCALA:

0: iconst_0             0: iconst_0
1: istore_0             1: istore_1
2: iload_0              2: iload_1
                        3: aload_0
3: getstatic     #2     4: invokevirtual #100      // Field/Method size:I
6: if_icmpge     28     7: if_icmpge     31
                        10: aload_0
9: getstatic     #4     11: invokevirtual #102     // Field/Method arr2:[I
12: iload_0             14: iload_1
                        15: aload_0
13: getstatic     #3    16: invokevirtual #104     // Field/Method arr:[I
16: iload_0             19: iload_1
17: iaload              20: iaload
18: iconst_2            21: iconst_2
19: imul                22: imul
20: iastore             23: iastore
21: iload_0             24: iload_1
22: iconst_1            25: iconst_1
23: iadd                26: iadd
24: istore_0            27: istore_1
25: goto          2     28: goto          2
                        31: aload_0
28: getstatic     #4    32: invokevirtual #102     // Field/Method arr2:[I
31: ldc           #12   35: ldc                    // int 1234500
33: iaload              37: iaload
34: ireturn             38: ireturn
```

The main difference in performance can be explained by the fact that Scala needs to call the getter of `arr`, `arr2` and `size` every time.

Let's look at some more results (average over three consecutive runs):

| . | Java | Scala |
| --- | --- | --- |
| Regular | 9.416 ms | 14.284 ms |
| `-Xint` | 229.3 ms | 2153.5 ms |
| `-XX:MaxInlineSize=1 -XX:FreqInlineSize=1` | 9.476ms | 61.391 ms |

It turns out that the Scala code is much more dependent on the positive effects of method inlining. This is in accordance with our earlier explanation. 

The time difference can even be fully explained by the initial cost of inlining. When running `map2` twice in the same session (Git commit `4bde5f`), the difference vanishes completely:

| . | Scala |
| --- | --- |
| First run | 14.571 ms |
| Second run | 9.412 ms |

## How about `map`?

The previous example can also be implemented in Scala using `map`. What would be the effect on performance? For a fair comparison, we will need to bring the initialization of `arr2` into scope of the benchmark, since `map` performs this initialization as well.

| . | Java | Scala `while` | Scala `map` |
| --- | --- | --- | --- |
| Regular | 24.465 ms | 29.024 ms | 124.60 ms |
| Second run | - | 24.362 ms | 110.47 ms |

The result is disastrous. Let's look at the bytecode of `map` (`scala.collection.ArrayOps.map$extension`, Scala 2.13.1):

```
88: iload         5
90: iload         15
92: if_icmpge     508
95: getstatic     #153                // Field scala/runtime/ScalaRunTime$.MODULE$:Lscala/runtime/ScalaRunTime$;
98: aload         4
100: iload         5
102: aload_2
103: aload         7
105: iload         5
107: iaload
108: invokestatic  #806                // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
111: invokeinterface #372,  2          // InterfaceMethod scala/Function1.apply:(Ljava/lang/Object;)Ljava/lang/Object;
116: invokevirtual #627                // Method scala/runtime/ScalaRunTime$.array_update:(Ljava/lang/Object;ILjava/lang/Object;)V
119: iload         5
121: iconst_1
122: iadd
123: istore        5
125: goto          88
```

A number of things stand out:
- The new element is added to the array using the method `scala.runtime.ScalaRuntime.array_update`. This method performs additional logic (pattern matching and casting), so even if it could be inlined - which is not likely due to its size - it is inefficient.
- The function passed to `map` is implemented as an object that implements the `Function1` trait/interface. If there is more than one instance of `Function1` in the application (which is highly likely in a common Scala application), the compiler will probably not be able to perform speculative compilation. TODO: can we show this?
- Additional casting is performed. This is necessary to undo the effect of the pattern match which has apparently some performance advantage. See the [commit message](https://github.com/scala/scala/commit/efa562bf3c1931ded3008e10e2134c6ec0572683). However, it seems to be possible to omit the class cast as follows (I did not test if this is an improvement):
```scala
// old: 
case xs: Array[AnyRef]  => while (i < len) { ys(i) = f(xs(i).asInstanceOf[AnyRef]); i = i+1 }
// new:
case _: Array[AnyRef]  => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
```
- Another difference is that `map` uses a local variable to store the new array, whereas my previous examples use a class scoped variable, which is implemented by a getter method. It is unclear if this causes a performance difference.

## Studying the JVM

Useful runtime flags:

-XX:+LogCompilation
-XX:MaxInlineSize=size
-XX:UnlockDiagnosticVMOptions
-XX:+PrintCompilation
-XX:MaxTrivialSize=size
-XX:+PrintAssembly
-XX:+PrintInlining
-XX:LoopUnrollLimit
-XX:+UseSuperWord

TODO: turn of GC???

See also [https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)

See [https://advancedweb.hu/2016/05/27/jvm_jit_optimization_techniques/](https://advancedweb.hu/2016/05/27/jvm_jit_optimization_techniques/)

http://fasihkhatib.com/2018/05/20/JVM-JIT-Loop-Unrolling/

https://web.archive.org/web/20180309233206/https://www.nayuki.io/page/a-fundamental-introduction-to-x86-assembly-programming

http://cr.openjdk.java.net/~vlivanov/talks/2015_JIT_Overview.pdf

https://gist.github.com/retronym/0178c212e4bacffed568

http://jpbempel.blogspot.com/2015/12/printassembly-output-explained.html