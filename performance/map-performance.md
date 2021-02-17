Performance of `map` in the Scala collection framework
===

The goal of this document is to study the performance of `map` (and other iteration methods) in the Scala collection framework.

In Scala 2.13.1 `scala.collection.ArrayOps.map` has been implemented using specialized code for the primitive types. This is done probably to prevent performance setbacks caused by unboxing. 

Research questions:
1. Why does the specialized approach lead to performance gain?
2. Can I reproduce the performance gain between the current approach and the unspecialized approach?
3. Can I improve the performance by getting rid of some type casting?
4. Can this also help other specialized methods in the collection framework?

## Reproducing previous work

According to commit [efa562bf](https://github.com/scala/scala/commit/efa562bf3c1931ded3008e10e2134c6ec0572683), the specialized `map` function has measurable performance advantages.


| Class name | Time* | Description |
| --- | --- | --- |
| `SpecializedMapOriginal` | 124.04 ms | Uses the original `scala.collection.ArrayOps.map` implementation of Scala 2.13.1. |
| `SpecializedMapCopy` | 127.10 ms | Uses an exact copy of the implementation of `scala.collection.ArrayOps.map` (to make sure that the standard library does not use some compilation tricks that are not available to the test setup). |
| `SpecializedMapOld` | 152.59 ms | Uses an exact copy of the implementation of `scala.collection.ArrayOps.map` before commit [efa562bf](https://github.com/scala/scala/commit/efa562bf3c1931ded3008e10e2134c6ec0572683). |
| `SpecializedMapNew` | 156.01 ms | Uses a specialized implementation without explicit casting. |

*Time was measured over 3 consecutive runs.

The results above show the following:
- Specialization leads indeed to a performance advantage.
- Removal of explicit casting has a negative impact on performance.

SEE https://scalac.io/specialized-generics-object-instantiation/