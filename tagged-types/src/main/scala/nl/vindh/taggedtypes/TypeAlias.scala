package nl.vindh.taggedtypes

import io.circe.{Decoder, Encoder}
import io.circe.shapes._
import io.circe.generic.auto._
import org.scalacheck.Arbitrary
import spray.json.DefaultJsonProtocol._
import spray.json._

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

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  requireName(str) // This should not compile, but it does
  requireAge(int) // This should not compile, but it does

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name)
  requireInt(age)

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  Array(1, 2, 3).map(i => i: Age)

  // === Library support

  // Circe
  case class TaggedCaseClass(name: Name, age: Age)
  val circeDecoder = implicitly[Decoder[TaggedCaseClass]]
  val circeEncoder = implicitly[Encoder[TaggedCaseClass]]

  // Spray
  val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply)

  // Scalacheck
  val arbitraryName = implicitly[Arbitrary[Name]]
  val arbitraryAge = implicitly[Arbitrary[Age]]
}
