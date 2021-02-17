Scala vs Java performance
===

# Comparing a simple loop

```scala
val len = 100000000
val arr = Range(0, len).toArray
// ...
val arr2 = new Array[Int](len)
var i = 0
while(i < len){
    arr2(i) = arr(i) * 2 //f(arr(i))
    i = i + 1
}
```

```java
int len = 100000000;
int arr[] = new int[len];
// ...
int arr2[] = new int[len];
int i = 0;
while(i < len){
    arr2[i] = arr[i] * 2;
    len++;
}
```
The Scala loop compiles to:
```
0: getstatic     #47                 // Field MODULE$:LTest$;
3: invokevirtual #145                // Method len:()I
6: newarray       int
8: astore_0
9: iconst_0
10: istore_1
11: iload_1
12: getstatic     #47                 // Field MODULE$:LTest$;
15: invokevirtual #145                // Method len:()I
18: if_icmpge     41
21: aload_0
22: iload_1
23: getstatic     #47                 // Field MODULE$:LTest$;
26: invokevirtual #172                // Method arr:()[I
29: iload_1
30: iaload
31: iconst_2
32: imul
33: iastore
34: iload_1
35: iconst_1
36: iadd
37: istore_1
38: goto          11
```


The Java loop compiles to:
```
44: iconst_0
45: istore        8
47: iload         8
49: iload_1             // local var 1 contains len = 100000000
50: if_icmpge     71
53: aload         7     // arr2
55: iload         8
57: aload         4
59: iload         8
61: iaload
62: iconst_2
63: imul
64: iastore
65: iinc          1, 1
68: goto          47
```

Combined:
```
JAVA                            SCALA
                                0: getstatic     #47                 // Field MODULE$:LTest$;
                                3: invokevirtual #145                // Method len:()I
                                6: newarray       int
                                8: astore_0
44: iconst_0                    9: iconst_0
45: istore        8             10: istore_1
47: iload         8             11: iload_1
                                12: getstatic     #47                 // Field MODULE$:LTest$;
49: iload_1                     15: invokevirtual #145                // Method len:()I
50: if_icmpge     71            18: if_icmpge     41
53: aload         7             21: aload_0
55: iload         8             22: iload_1
                                23: getstatic     #47                 // Field MODULE$:LTest$;
57: aload         4             26: invokevirtual #172                // Method arr:()[I
59: iload         8             29: iload_1
61: iaload                      30: iaload
62: iconst_2                    31: iconst_2
63: imul                        32: imul
64: iastore                     33: iastore
                                34: iload_1
                                35: iconst_1
65: iinc          1, 1          36: iadd
                                37: istore_1
68: goto          47            38: goto          11

```