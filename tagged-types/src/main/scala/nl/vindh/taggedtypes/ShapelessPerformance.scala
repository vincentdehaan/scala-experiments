package nl.vindh.taggedtypes

import shapeless.tag
import shapeless.tag.@@

object ShapelessPerformance extends App {
  val size = 10 * 1000 * 1000
  val arr = Range(0, size).toArray
  sealed trait T

  val t1 = System.nanoTime
  val arrTagged = arr.map(i => tag[T][Int](i))
  val t2 = System.nanoTime
  val check: Int @@ T = arrTagged(12345) // Make sure arrTagged is really tagged

  val t3 = System.nanoTime
  val arrUntagged = arrTagged.map(i => i + 1) // It seems to be a little faster with i.asInstanceOf[Int]
  val t4 = System.nanoTime

  println(arrUntagged(123456))
  println((t2 - t1) / 1000)
  println((t4 - t3) / 1000)
}
