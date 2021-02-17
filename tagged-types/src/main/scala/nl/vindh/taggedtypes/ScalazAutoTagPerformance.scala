package nl.vindh.taggedtypes

import scalaz._

import scala.reflect.ClassTag

object ScalazAutoTagPerformance extends App {
  val size = 10 * 1000 * 1000
  val arr = Range(0, size).toArray
  sealed trait T
  implicit def untag[A, Tag](tagged: A @@ Tag): A = tagged.asInstanceOf[A]

  val t1 = System.nanoTime
  val arrTagged = arr.map(i => Tag[Int, T](i))(ClassTag[Int @@ T](java.lang.Integer.TYPE))
  val t2 = System.nanoTime
  val check: Int @@ T = arrTagged(12345) // Make sure arrTagged is really tagged

  val t3 = System.nanoTime
  val arrUntagged = arrTagged.map(i => i + 1)
  val t4 = System.nanoTime

  println(arrUntagged(123456))
  println((t2 - t1) / 1000)
  println((t4 - t3) / 1000)
}
