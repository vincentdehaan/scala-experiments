package nl.vindh.taggedtypes

import io.circe.{Decoder, Encoder}
import io.circe.generic._
import io.circe.shapes._
import org.scalacheck.Arbitrary
import org.scanamo.DynamoFormat
import spray.json.DefaultJsonProtocol._
import org.scanamo.auto._
import taggedtypes._

object TvarohTagged extends App {
  object Name extends TaggedType[String]
  type Name = Name.Type
  object Age extends TaggedType[Int]
  type Age = Age.Type

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: Name): String = n.asInstanceOf[String]
  def requireAge(a: Age): Int = a.asInstanceOf[Int]

  val name: Name = "Joe".@@[Name.Tag]
  val age: Age = 30.@@[Age.Tag]
  val str: String = "String"
  val int: Int = 12

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  // requireName(str) // This should not compile
  // requireAge(int) // This should not compile

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name)
  requireInt(age)

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  Array(1, 2, 3).map(i => Age(i))

  // (5) Pattern matching behaviour
  // name match { // Does not compile
  //   case "Joe" => println("Correct pattern matching behaviour")
  //   case _ => println("Incorrect pattern matching behaviour")
  // }

  // (6) Regular equality
  println(s"This should be true: ${name == "Joe"}")

  // (7) Built-in type classes
  val equiv = implicitly[Equiv[Age]]
  val ordering = implicitly[Ordering[Age]]
  // val numeric = implicitly[Numeric[Age]] // Does not compile

  // === Library supprt
  case class TaggedCaseClass(name: Name, age: Age)

  // Circe
  // val circeDecoder = implicitly[Decoder[TaggedCaseClass]] // Does not compile
  // val circeEncoder = implicitly[Encoder[TaggedCaseClass]] // Does not compile

  // Spray
  // val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply) // Does not compile

  // Scalacheck
  // val arbitraryName = implicitly[Arbitrary[String @@ Name]] // Does not compile
  // val arbitraryAge = implicitly[Arbitrary[String @@ Age]] // Does not compile

  // Scanamo
  // val scanamoFormat = implicitly[DynamoFormat[TaggedCaseClass]] // Does not compile
}
