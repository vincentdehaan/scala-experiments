package nl.vindh.experiments

import ClassWithImplicits._

object ImplicitTest extends App {
    def f(implicit x: XX): Unit = println(x.x)

    f
}