Resolving common errors
===

## Scalac errors

### Ambiguous reference to overloaded definition

1. Apparently there are two definitions that could be applicable here. Look in particular for default arguments that may mess things up. In this case, the solution may be to add `()` explicitly.

Example:
```scala
object Test {
    def f: Int = 1
    def f(i: Int = 0): Int = i

    f // Which one is it?
    f() // This can only be the first one
}
```