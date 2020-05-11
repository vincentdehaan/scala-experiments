Property based testing
===

## Example 1

Run `Example1`:

```
sbt "testOnly *Example1"
```

To see what happens, run `Example1Print`.

## Example 2

Finding counterexamples using shrinking. Run `Example2`.

## Example 3

How does this all work under the hood? How does ScalaCheck generate and shrink arbitrary types? Let's explore the implicits.

Add this line to `build.sbt`:

```scala
scalacOptions += "-Ybrowse:typer"
```

Run `Example3`.

## Example 4

Generate random ADTs:

Add to `build.sbt`:
```scala
libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3"
```

Import:
```scala
import org.scalacheck.ScalacheckShapeless._
```

## Problem: what if not all random values make sense?

Suppose we have a store with a store ID which is a string of the format `<country code>-<number>`. If we would have this model, we couldn't generate valid random stores:

```scala
Store(
  storeId: String,
  address: String
)
```

### Solution 1: remodel

Make illegal state unrepresentable:

```scala
StoreId(country: String, num: Int)
Store(
  storeId: StoreId,
  address: String  
)
```

But we still have negative numbers

### Solution 2: conditional testing
Problem: too little test cases

### Solution 3: refined types

### Solution 4: custom generators and shrinks

### How far do we go?
Ideally, we have a one-to-one relationship between domain and model+generators+shrinks. However, in practice, we need to go as far as necessary to test.

### Problem 2: json parsing + generating

Tests:
- generate + parse => same result
- each primitive value is a substring of the end result (use `productElement` and `productElementName` to traverse the tree)