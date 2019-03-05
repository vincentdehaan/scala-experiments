object MatchType extends App {
    case class A(a: Int)

    def doIt: Unit = {
        val a = A(7)

        a match {
            case _: A => println("It's an A!")
        }
    }

    doIt
}