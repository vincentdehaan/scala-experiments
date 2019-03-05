object MatchBugs extends App {
    var sideEffect = 1
    case class A(a: Int)
    object A {
        def unapply(a: A): Option[Int] = {
            sideEffect = 2
            Some(a.a + 1)
        }
    }

    case class B(b: Int)
    object B {
        def unapply(a: A): Option[(Int, Int)] = {
            Some((a.a, a.a + 1))
        }
    }

    object AA {
        def unapply(a: A): Option[(Int, Int)] = {
            Some((a.a, a.a + 1))
        }
    }

    // The first problem is that the compiler optimizes `case A(_)` to case `case _: A`, 
    // but in case of a custom unapply method, this is not allowed.
    def bug1: Unit = {
        val a = A(7)

        a match {
            case A(_) => println(sideEffect) // returns 1
        }
    }

    // The second problem is that the custom unapply method with the same type signature
    // as the original unapply method, is not attached.
    def bug2: Unit = {
        val a = A(7)

        a match {
            case A(a) => println(a) // returns 7
        }
    }

    // The third problem, filed as bug 11252, is that an unapply method with a different
    // type signature, is not attached either, and results in a syntax error
    def bug3: Unit = {
        val a = A(7)

        a match {
            case B(a, b) => println(s"B $a $b") // DOES NOT COMPILE
            case AA(a, b) => println(s"AA $a $b")
        }
    }

    bug1
    bug2
    bug3
}