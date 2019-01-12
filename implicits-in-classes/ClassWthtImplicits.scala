package nl.vindh.experiments

case class YY(x: Int)

object ClassWthtImplicits {
    val x: YY = new YY(7) 
}