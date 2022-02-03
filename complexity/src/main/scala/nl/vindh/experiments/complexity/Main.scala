package nl.vindh.experiments.complexity

import org.joda.time.DateTime

object Main extends App {

  //test(idx => mkList(idx * 100000))(lst => lst.reverse(0))
  //test(idx => mkList(idx * 100000))(lst => lst.last)
  //test(idx => mkVect(idx * 100000))(vec => vec.last)




  def test[T](init: Int => T)(f: T => Int): Unit = {
    var out = 0

    (1 to 20).foreach {
      idx => {
        val t = init(idx)

        val d1 = DateTime.now()
        var count = 0
        while (count < 100) {
          val res = f(t)
          out += res
          count += 1
        }
        val d2 = DateTime.now()
        //println(s"$idx\t${d2.getMillis - d1.getMillis}")
        println((d2.getMillis - d1.getMillis).toString)
      }
    }

    println()
    println()
    println(out)
  }

  def mkList(size: Int, acc: List[Int] = Nil): List[Int] =
    if(size == 0) acc else mkList(size - 1, size :: acc)

  def mkVect(size: Int, acc: Vector[Int] = Vector()): Vector[Int] =
    if(size == 0) acc else mkVect(size - 1, Vector(size) ++ acc)



}
