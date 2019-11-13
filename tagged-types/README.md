Tagged types in Scala
===
The goal of this project is to research different ways to implement type tagging in Scala. The solutions will be classified according to the following criteria:

| . | type alias | Scalaz | Scalaz + AutoTag | Shapeless |
| --- | --- | --- | --- | --- |
| Type checking | Yes | Yes | Yes | ? |
| Non-automatic upcasting | No | Yes | Yes | ? |
| Automatic downcasting | Yes | No | Yes | ? |
| Performance | ? | ? | ? | ? |
| Type inference in common IDEs (IntelliJ/Eclipse) | ? | ? | ? | ? |
| Informative compiler errors in case of a type error | ? | ? | ? | ? |
| Informative stack traches in case of a runtime error involving a tagged type | ? | ? | ? | ? |
| Compatibility with Java | ? | ? | ? | ? |
| Obfuscation in compiled code: the business logic is not derivable from the class file | ? | ? | ? | ? |
| Class file size | ? | ? | ? | ? |

TODO: https://github.com/softwaremill/scala-common

TODO: extends AnyVal

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



TODO. Read:
- http://etorreborre.blogspot.com/2011/11/practical-uses-for-unboxed-tagged-types.html
- https://stackoverflow.com/questions/34266285/tagged-type-comparison-in-scalaz