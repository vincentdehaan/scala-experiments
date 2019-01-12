package nl.vindh.experiments

case class XX(x: Int)

object ClassWithImplicits {
    implicit val x: XX = new XX(7) 
}