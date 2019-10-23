Adventures with cats
===

The goal of this adventure is to better understand Cats and to contribute to it. My strategy is to try to improve test coverage. This is a good thing in general, and if I am able to write tests for existing code, that means I understand it. And maybe along the way, I find possible improvements.

# Determining test coverage

Cats uses Scoverage to determine the test coverage, and Codecov to generate nice colorful reports.

# Open questions

## Implicit resolution

In the following example, implicits do not resolve. Why not:

```scala
class Test extends AnyFunSuiteLike {
  type F1[A] = Function[A, String]
  val instance = Contravariant[F1] // implicits resolve!
  test("a test") {
    type F2[A] = Function[A, String]
    val instance2 = Contravariant[F2] // implicits do not resolve!
  }
}
```

Finding a solution:
1. We add `scalacOptions += "-Xprint:typer"` to `build.sbt` such that the compiler resolves the implicits for us in the working example. Then we try to fill in the implicits explicitly and see if the code compiles. If so, there is a problem with implicit resolution. Otherwise, there is a scope problem.

We get the following result:
```scala
class Test extends AnyFunSuiteLike {
  type F1[A] = Function[A, String]
  val instance = Contravariant.apply[F1](
    cats.implicits.catsStdContravariantMonoidalForFunction1[String](
      cats.implicits.catsKernelStdMonoidForString))
  test("a test") {
    type F2[A] = Function[A, String]
    val instance2 = Contravariant.apply[F2](
      cats.implicits.catsStdContravariantMonoidalForFunction1[String](
        cats.implicits.catsKernelStdMonoidForString))
  }
}
```
Something remarkable happens: the code compiles, but with regards to `instance2`, IntelliJ complains that it required a `Contravariant[F2]` and that it found a `ContravariantMonoidsl[({type L[a$6$] = a$6$ => String })#L]`. This suggests that type `F2` is in some way transformed incorrectly.

There is additional evidence for this hypothesis:
- Implicits without type declarations resolve perfectly.
- If I place the type definition of `F2` outside the `test` block, the implicits resolve perfectly.

I suspect that somewehere in the definition of `test`, a macro is transforming the code.

2. We compile the code with the `-Ymacro-debug-lite` compiler flag, but no relevant macros appear to be in place.

3. Hmm, apparently the problem is with the presentation compiler. Although IntelliJ complains about the implicits, compilation is possible. (Note to self: I should have found this out earlier.)

4. The problem is not related to ScalaTest. It also happens here:

```scala
def test2[A](f: => A): A = f
test2 {
  type F2[A] = Function[A, String]
  val instance2 = Contravariant[F2] // implicits do not resolve!
}
```

5. It seems to be related to a known bug in the [IntelliJ Scala plugin](https://youtrack.jetbrains.com/issue/SCL-16123)