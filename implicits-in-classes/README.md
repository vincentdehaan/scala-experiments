Implicits represented in Java classes
---

When compiling a Scala source that needs some implicit value from another source file, it does not matter whether this source file is a `.scala` file or a compiled `.class` file. This means that the class file format is able to represent the implicitness of members, although this feature does not exist in Java. Let's find out how this works.

## Demonstration

First we demonstrate that this actually works:

```
scalac -d subdir ClassWithImplicits.scala
cd subdir
scalac -cp . ImplicitTest.scala # Note that at this point only the compiled class file is available to the compiler
scala -cp . nl.vindh.experiments.ImplicitTest
```

## How does this work?

Use my special version of scalap with `-extraVerbose` flag:

```
# in the vincentdehaan/scala repository
git checkout scalap-extraverbose
sbt scalap
java -Xbootclasspath/a:build/pack/lib/scala-compiler.jar:build/pack/lib/scala-library.jar:build/pack/lib/scalap.jar:build/pack/lib/scala-reflect.jar -classpath '""' scala.tools.scalap.Main -extraVerbose -cp [path of class] [className]
```

This shows that the flags of Scala methods (for example, the `implicit` flag) is encoded in a ScalaSignature that is stored in the form of a RuntimeVisibleAnnotation. See for more information [this blog](http://blog.vityuk.com/2010/12/how-scala-compiler-stores-meta.html).