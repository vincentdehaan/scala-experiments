object SpecializedMapOriginal extends App {
    val size = 10 * 1000 * 1000
    val arr = Range(0, size).toArray
    val f: Int => Int = _ * 2

    val t1 = System.nanoTime
    val arr2 = arr.map(f)
    val t2 = System.nanoTime

    println(arr2(1234567))
    println((t2 - t1) / 1000)
}