object ArrayTestScala extends App {
    val size = 10 * 1000 * 1000
    val arr = Range(0, size).toArray
    var arr2: Array[Int] = _

    def map2: Int = {
        arr2 = new Array[Int](size)
        var i = 0;
        while (i < size) {
            arr2(i) = arr(i) * 2
            i = i + 1
        }
        arr2(1234500)
    }

    val t1 = System.nanoTime
    println(map2)
    val t2 = System.nanoTime
    println(map2)
    val t3 = System.nanoTime
    println((t2 - t1) / 1000)
    println((t3 - t2) / 1000)
}