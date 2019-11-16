package nl.vindh.taggedtypes

import scalaz.{@@, Tag}

object ScalazTagged extends App {
  sealed trait Name
  sealed trait Age

  def Name(s: String): String @@ Name = Tag[String, Name](s)
  def Age(i: Int): Int @@ Age = Tag[Int, Age](i)

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: String @@ Name): String = n.asInstanceOf[String]
  def requireAge(a: Int @@ Age): Int = a.asInstanceOf[Int]

  val name: String @@ Name = Name("Joe")
  val age: Int @@ Age = Age(30)
  val str: String = "String"
  val int: Int = 12

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  //requireName(str) // This should not compile
  //requireAge(int) // This should not compile

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  //requireString(name) // Does not compile
  //requireInt(age) // Does not compile



  // NOTE: the type inferencer is extremely slow
}
