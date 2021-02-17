package nl.vindh.taggedtypes

object TypeAliasPerformance extends App {
  val size = 10 * 1000 * 1000
  val arr = Range(0, size).toArray
  type T = Int

  val t1 = System.nanoTime
  val arrTagged = arr.map(i => i: T)
  val t2 = System.nanoTime
  val check: Int = arrTagged(12345) // Make sure arrTagged is really tagged

  val t3 = System.nanoTime
  val arrUntagged = arrTagged.map(i => i + 1)
  val t4 = System.nanoTime

  println(arrUntagged(123456))
  println((t2 - t1) / 1000)
  println((t4 - t3) / 1000)
}
