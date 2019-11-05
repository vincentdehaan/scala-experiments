import scala.reflect.ClassTag

object SpecializedMapOld extends App {
    def mapcopy[A, B](xs: Array[A])(f: A => B)(implicit ct: ClassTag[B]): Array[B] = {
        val l = xs.length
        val res = new Array[B](l)
        var i = 0
        while (i < l) {
            res(i) = f(xs(i))
            i = i + 1
        }
        res
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