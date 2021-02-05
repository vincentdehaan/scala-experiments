package nl.vindh.taggedtypes

import io.circe.{Decoder, Encoder}
import io.circe.shapes._
import io.circe.generic._
import io.estatico.newtype.macros.newsubtype
import org.scalacheck.Arbitrary
import spray.json.DefaultJsonProtocol._

object NewType extends App {
  @newsubtype case class Name(str: String)
  @newsubtype case class Age(i: Int)

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: Name): String = n // IntelliJ 2020.2 does not recognize the correct type here
  def requireAge(a: Age): Int = a// IntelliJ 2020.2 does not recognize the correct type here

  val name: Name = Name("Joe")
  val age: Age = Age(30)
  val str: String = "String"
  val int: Int = 12

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  //requireName(str) // This should not compile
  //requireAge(int) // This should not compile

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name) // IntelliJ 2020.2 does not recognize the correct type here
  println(requireInt(age)) // IntelliJ 2020.2 does not recognize the correct type here

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  //Array(1, 2, 3).map(i => Age(i)) // Does not compile

  // === Library support

  // Circe
  case class TaggedCaseClass(name: Name, age: Age)
  //val circeDecoder = implicitly[Decoder[TaggedCaseClass]] // Does not compile
  //val circeEncoder = implicitly[Encoder[TaggedCaseClass]] // Does not compile

  // Spray
  //implicit val sprayJsonFormatAge = jsonFormat1(Age.apply) // Does not compile
  //implicit val sprayJsonFormatName = jsonFormat1(Name.apply) // Does not compile
  //val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply) // Does not compile

  // Scalacheck
  // val arbitraryName = implicitly[Arbitrary[Name]] // Does not compile
  // val arbitraryAge = implicitly[Arbitrary[Age]] // Does not compile

}
