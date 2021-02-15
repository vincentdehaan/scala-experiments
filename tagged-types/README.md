Tagged types in Scala
===
The goal of this project is to research different ways to implement type tagging in Scala. The solutions will be classified according to the following criteria:

| __Semantic properties__ | type alias | Scalaz | Scalaz + AutoTag | Shapeless | `@newsubtype` | Scala-common |
| --- | --- | --- | --- | --- | --- | --- |
| (1) Type checking | Yes | Yes | Yes | Yes | Yes | Yes |
| (2) Non-automatic upcasting | No | Yes | No | Yes | Yes | Yes |
| (3) Automatic downcasting | Yes | No. <br>Requires `asInstanceOf`. | Yes | Yes | Yes | Yes |
| (4) ClassTag inference | Yes | No | No | Yes | No | Yes |
| (5) Pattern matching | Yes | Yes | Yes | No | Yes | No |
| (6) Regular equality | Yes | Yes | Yes | Yes | Yes | Yes |
| (7) Built-in type classes | E, O, N | E | E | E, O | E, O | E, O | 
| __Other properties__ | __type alias__ | __Scalaz__ | __Scalaz + AutoTag__ | __Shapeless__ | __`@newsubtype`__ | __Scala-common__ |
| Performance | Baseline | Baseline | Baseline | Slow | ? | ? |
| Type inference in common IDEs (IntelliJ/Eclipse) | Yes | Yes | Yes | Yes | No | Yes |
| Informative compiler errors in case of a type error | ? | ? | ? | ? | ? | ? |
| Informative stack traces in case of a runtime error involving a tagged type | ? | ? | ? | ? | ? | ? |
| Compatibility with Java | ? | ? | ? | ? | ? | ? |
| Obfuscation in compiled code: the business logic is not derivable from the class file | ? | ? | ? | ? | ? | ? |
| Class file size | ? | ? | ? | ? | ? | ? |
| __Library support__ | __type alias__ | __Scalaz__ | __Scalaz + AutoTag__ | __Shapeless__ | __`@newsubtype`__ | __Scala-common__ |
| Circe: automatic encoder/decoder generation | Yes | No | No | [Yes](https://github.com/circe/circe/pull/1480) | No | No |
| Spray json: automatic encoder/decoder generation | Yes | No | No | No | No | Yes |
| ScalaCheck: automatic arbitrary generation | Yes | No | No | [Pending](https://github.com/alexarchambault/scalacheck-shapeless/pull/173) | No | Yes |
| Scanamo: automatic format generation | Yes | No | No | No | No | No |

TODO: look into Scalaz commit 85e1dae0e5c00929328833ce0e41946d7e4ab8cb. It seems something has changed regarding the casting behavior.

## Tagged types: a definition

What do we require of a tagged type?

(1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
```scala
def requireName(n: Name): String = ???
val name: Name = "Joe"
requireName(name)
```

(2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:

Together with (1): on a place where a tagged type is expected, __only__ a tagged type can be substituted.
```scala
def requireName(n: Name): String = ???
val str: String = "This is a string"
requireName(str) // This yields a type error
```

(3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
```scala
def requireString(s: String): String = ???
val name: Name = "Joe"
requireString(name)
```

(4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
```scala
Array(1, 2, 3).map(i => tag(i)) // Does not compile without a ClassTag
```

(5) Pattern matching is possible.

```scala
name match {
  case "Joe" => println("This is correct behaviour")
  case _ => println("This is incorrect, but it compiles.")
}
```

(6) Regular equality.
```scala
println(s"This should be true: ${name == "Joe"}")
```

(7) Built-in type classes
```scala
val equiv = implicitly[Equiv[Age]] // E
val ordering = implicitly[Ordering[Age]] // O
val numeric = implicitly[Numeric[Age]](IntIsIntegral) // N
```

## Performance

Time in ms, averaged over four runs, each time with a fresh JVM:
 
| . | Tag | Untag |
| --- | --- | --- |
| Baseline | 191 | 133 |
| Type alias | 188 | 118 |
| Scalaz | 194 | 103 |
| Scalaz + AutoTag | 191 | 102 |
| Shapeless | 218 | 2612 |
| Shapeless + explicit downcasting | 212 | 2323 |




TODO. Read:
- http://etorreborre.blogspot.com/2011/11/practical-uses-for-unboxed-tagged-types.html
- https://stackoverflow.com/questions/34266285/tagged-type-comparison-in-scalaz