package nl.vindh.taggedtypes

object TypeAlias extends App {
  type Name = String
  type Age = Int

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: Name): String = n.toString
  def requireAge(a: Age): Int = a.toInt

  val name: Name = "Joe"
  val age: Age = 30
  val str: String = "String"
  val int: Int = 12

  // (1) The a literal of the untagged type may be substituted on a place where a corresponding tagged type is expected:
  requireName("Joe")
  requireAge(30)

  // (2) A variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  requireName(str) // This should not compile, but it does
  requireAge(int) // This should not compile, but it does

  // (3) A variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name)
  requireInt(age)

  // (4) On a place where a tagged type is expected, only a tagged type can be substituted.
  requireName(name)
  requireAge(age)
}
