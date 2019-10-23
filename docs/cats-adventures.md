Adventures with cats
===

The goal of this adventure is to better understand Cats and to contribute to it. My strategy is to try to improve test coverage. This is a good thing in general, and if I am able to write tests for existing code, that means I understand it. And maybe along the way, I find possible improvements.

# Determining test coverage

Cats uses Scoverage to determine the test coverage, and Codecov to generate nice colorful reports.

# Open questions

## Implicit resolution

In the following example, implicits do not resolve. Why not:

```
class Test extends AnyFunSuiteLike {
  type F1[A] = Function[A, String]
  val instance = Contravariant[F1] // implicits resolve!
  test("bla") {
    type F2[A] = Function[A, String]
    val instance2 = Contravariant.apply[F2] // implicits do not resolve!
  }
}
```