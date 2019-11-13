Tagged types in Scala
===
The goal of this project is to research different ways to implement type tagging in Scala. The solutions will be classified according to the following criteria:

| . | type alias | Scalaz | Shapeless |
| --- | --- | --- | --- |
| Real types: does the type checker enforce the tag?| No | ? | ? |
| Performance | ? | ? | ? |
| Type inference in common IDEs (IntelliJ/Eclipse) | ? | ? | ? |
| Informative compiler errors in case of a type error | ? | ? | ? |
| Informative stack traches in case of a runtime error involving a tagged type | ? | ? | ? |
| Compatibility with Java | ? | ? | ? |
| Obfuscation in compiled code: the business logic is not derivable from the class file | ? | ? | ? |
| Class file size | ? | ? | ? |

TODO: https://github.com/softwaremill/scala-common

TODO: extends AnyVal

## Tagged types: a definition

What do we require of a tagged type?

(1) The a literal of the untagged type may be substituted on a place where a corresponding tagged type is expected:

```scala
def requireName(n: Name): String = ???
requireName("Just a string literal")
```

NOTE: it seems that this requirement basically undoes the whole idea of tagged types since this will also apply to variables (because of referential transparancy).

(2) A variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
```scala
def requireName(n: Name): String = ???
val str: String = "This is a string"
requireName(str) // This yields a type error
```

(3) Automatic downcast: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
```scala
def requireString(s: String): String = ???
val name: Name = "Joe"
requireString(name)
```

(4) On a place where a tagged type is expected, only a tagged type can be substituted.
```scala
def requireName(n: Name): String = ???
val name: Name = "Joe"
requireName(name)
```


TODO. Read:
- http://etorreborre.blogspot.com/2011/11/practical-uses-for-unboxed-tagged-types.html
- https://stackoverflow.com/questions/34266285/tagged-type-comparison-in-scalaz