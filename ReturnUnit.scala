/*
    Compile with:
    scalac -Ywarn-value-discard ReturnUnit.scala

    It generates a 'discarded non-Unit value' warning in def implicitUnit.

    Inspect with:
    javap -c Main$

    Conclusion: the bytecode output is the same.
*/

object Main extends App {
    val sb = new StringBuilder
    def implicitUnit: Unit = {
        sb.append("bla")
    }
    def explicitUnit: Unit = {
        sb.append("bla2")
        ()
    }
    implicitUnit
    explicitUnit
    println(sb.toString)

}