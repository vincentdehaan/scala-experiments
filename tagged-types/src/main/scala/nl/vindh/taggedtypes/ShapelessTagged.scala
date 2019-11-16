package nl.vindh.taggedtypes

import shapeless.tag
import shapeless.tag.@@

object ShapelessTagged extends App {
  trait NameTag
  trait AgeTag

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: String @@ NameTag): String = n
  def requireAge(a: Int @@ AgeTag): Int = a

  val name: String @@ NameTag = tag[NameTag][String]("Joe")
  val age: Int @@ AgeTag = tag[AgeTag][Int](30)
  val str: String = "String"
  val int: Int = 12

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  //requireName(str) // This should not compile
  //requireAge(int) // This should not compile

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name)
  println(requireInt(age))
}