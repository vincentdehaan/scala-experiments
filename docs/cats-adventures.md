Adventures with cats
===

The goal of this adventure is to better understand Cats and to contribute to it. My strategy is to try to improve test coverage. This is a good thing in general, and if I am able to write tests for existing code, that means I understand it. And maybe along the way, I find possible improvements.

# Determining test coverage

Cats uses Scoverage to determine the test coverage, and Codecov to generate nice colorful reports. It turns out that these reports are not accurate, however. Codecov uses line coverage, while Scoverage uses statement coverage. Whenever one statement on a line has been covered, Codecov considers the whole line covered.

My first task was to adjust the Scoverage report in such a way that it does not only show which statements are covered, but also _by which test classes_. This makes it easier to improve on existing tests. For this purpose, I created two pull requests:

- [scoverage / sbt-coverage, PR #297](https://github.com/scoverage/sbt-scoverage/pull/297)
- [scoverage / scalac-scoverage-plugin, PR #282](https://github.com/scoverage/scalac-scoverage-plugin/pull/282)

> *How does Scoverage work?* Scoverage consists of a compiler plugin that does two things at compile time: (1) it generates a file with metadata about each statement in the codebase, assigning a unique number to each statement; (2) it adds an `invoke` method to each statement that is invoked when the statement is executed at runtime. Whenever a statement is executed at runtime, the accomopanying `invoke` method is called and writes the statement id to a second file. The new feature I made, adds the test identifier as well. Since it is impossible to determine which class in the stack trace is the test class, I use the heuristic that classnames ending in 'Test', 'Spec', or 'Suite' are tests.

The new feature can - as long as it has not been merged and released - used by taking the following steps:
1. Clone the [scalac-scoverage-plugin](https://github.com/vincentdehaan/scalac-scoverage-plugin) repository and checkout the `feature/report-test-names` branch.
2. Run `sbt publishLocal`.
3. Clone the [sbt-scoverage](https://github.com/vincentdehaan/sbt-scoverage) repository and checkout the `feature/report-test-names` branch.
4. Go to `sbt-coverage/src/main/scala/scoverage/ScoverageSbtPlugin.scala` and change `val DefaultScoverageVersion` to `1.4.1-SNAPSHOT`.
5. Run `sbt publishLocal`.
6. Change the Scoverage version in `plugins.sbt` in the target project to `1.6.1-SNAPSHOT`.

## `Apply.scala` is barely touched by Scoverage

On some occasions, for example in `Apply.scala`, the HTML report of Scoverage does not color the statements properly. This seems to be caused by the fact that in the instrumentation data, the statement starts and ends at the same position. It is unclear when this occurs. It is likely that this problem is not caused by Scoverage, but by the compiler.

# What is *?

TODOTODO

# Improving test coverage on `Composed.scala`

`Composed.scala` lacks tests for the following statements:
- line 17: `G.imap(gb)(g)(f)`
- line 123: `G.contramap(fb)(f)`
- line 171: `(G.map(g)(_._1), G.map(g)(_._2))` (Only this one is recognized by the Codecov report.)
- line 188: `G.map(gb)(g)`
- line 196: `G.contramap(gb)(f)`

# Improving test coverage on `Parallel.scala`

`Parallel.scala` lacks tests for the following statements:
- line 37: `Parallel.parMap2(ma, mb)((_, b) => b)(this)`
- line 40: `parProductR(ma)(mb)`
- line 47: `Parallel.parMap2(ma, mb)((a, _) => a)(this)`
- line 50: `parProdutL(ma)(mb)`
- line 103: `applicative.unlessA(cond)(f)`
- line 105: `applicative.whenA(cond)(f)`
- line 185: `P.sequential(UnorderedTraverse[T].unorderedTraverse(ta)(a => P.parallel(f(a))))`
- line 190: `parUnorderedTraverse[T, M, F, M[A], A](ta)(Predef.identity)`
- line 195: `P.monad.map(parUnorderedTraverse[T, M, F, A, T[B]](ta)(f))(FlatMap[T].flatten)`
- line 200: `parUnorderedFlatTraverse[T, M, F, M[T[A]], A](ta)(Predef.identity)`

# Checklists

## Implicit resolution

Problem: IntelliJ complains about an inresolvable implicit

Solution:
1. Try to compile. It is possible that IntelliJ is too strict.
2. Fill in the implicits manually.
- If you don't know how, find a working example and compile using `-Xprint:typer`. Then the compiler works out the implicits.

## Scala version compatibility

TODO

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