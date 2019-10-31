object ArrayTestScala extends App {
    val size = 10 * 1000 * 1000
    val arr = Range(0, size).toArray
    val arr2 = new Array[Int](size)

    def map2: Int = {
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
    println((t2 - t1) / 1000)
}