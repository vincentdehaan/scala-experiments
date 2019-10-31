Scala vs Java performance
===

# A motivating example

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

The time difference can even be fully explained by the incidental cost of inlining. When running `map2` twice in the same session, the difference vanishes completely:

| . | Scala |
| --- | --- |
| first run | 14.571 ms |
| second run | 9.412 ms |