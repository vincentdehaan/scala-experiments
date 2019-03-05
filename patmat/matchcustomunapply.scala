object MatchCustomUnapply extends App {
    case class A(a: Int)
    object A {
        def unapply(a: A): Option[Int] = {
            println("Calling A.unapply ...")
            Some(a.a)
        }
    }

    def doIt2: Unit = {
        val a = A(7)

        a match {
            case A(_) => println("It's an A!")
        }
    }

    def doIt2: Unit = {
        val a = A(7)

        a match {
            case A(x) => println(s"It's an A! $x")
        }
    }

    doIt
    doIt2
}