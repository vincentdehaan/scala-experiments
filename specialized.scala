// This file demonstrates the @specialized annotation
// To see what happens under the hood: 
//  compile with scalac -Ybrowse:specialize specialized.scala

class Container[@specialized T](val t: T) {
    def get: T = t
}

object SpecializedDemo extends App {
    val x = new Container(1)
    val y = new Container(true)
    println(s"type of Container(1): ${x.getClass.getName}")
    println(s"type of Container(true): ${y.getClass.getName}")

    // Now we fix T in f and g to Int
    def f[@specialized T](x: T): T = x
    def g[T](x: T): T = x
    def intToInt(f: Int => Int): Int => Int = f
    val fInt = intToInt(f)
    val gInt = intToInt(g)
    // Inspecting with -Ybrowse:specialize, we observe that in fInt, f$...$sp is
    //  called, while in gInt, g is called directly.
    
    def fNext[T](x: T): T = f(x)
    def gNext[T](x: T): T = g(x)
    // This does not generate specialized instances of fNext. fNext calls just
    //  the unspecialized version of f.
}