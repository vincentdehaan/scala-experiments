import scala.reflect.ClassTag

object SpecializedMapNew extends App {
    def mapcopy[A, B](xs: Array[A])(f: A => B)(implicit ct: ClassTag[B]): Array[B] = {
        val len = xs.length
        val ys = new Array[B](len)
        if(len > 0) {
        var i = 0
        (xs: Any) match {
            case _: Array[AnyRef]  => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Int]     => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Double]  => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Long]    => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Float]   => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Char]    => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Byte]    => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Short]   => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
            case _: Array[Boolean] => while (i < len) { ys(i) = f(xs(i)); i = i+1 }
        }
        }
        ys
    }

    val size = 10 * 1000 * 1000
    val arr = Range(0, size).toArray
    val f: Int => Int = _ * 2

    val t1 = System.nanoTime
    val arr2 = mapcopy(arr)(f)
    val t2 = System.nanoTime

    println(arr2(1234567))
    println((t2 - t1) / 1000)
}