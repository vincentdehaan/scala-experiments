object MatchUnapply extends App {
    case class A(a: Int)
    object A {
        /*def unapply(a: Any): Option[Int] = a match {
            case aa: A =>
            println("Calling A.unapply ...")
            Some(aa.a)
        }*/
    }

    def doIt: Unit = {
        val a = A(7)

        a match {
            case A(_) => println("It's an A!")
        }
    }

    doIt
}