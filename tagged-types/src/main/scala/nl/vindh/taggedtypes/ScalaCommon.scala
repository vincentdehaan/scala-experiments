package nl.vindh.taggedtypes

import com.softwaremill.tagging.{@@, Tagger}
import io.circe.{Decoder, Encoder}
import io.circe.shapes._
import io.circe.generic._
import org.scalacheck.Arbitrary
import org.scanamo.DynamoFormat
import spray.json.DefaultJsonProtocol._
import org.scanamo.auto._

object ScalaCommon extends App {
  trait NameTag
  trait AgeTag

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: String @@ NameTag): String = n
  def requireAge(a: Int @@ AgeTag): Int = a

  val name: String @@ NameTag = "Joe".taggedWith[NameTag]
  val age: Int @@ AgeTag = 30.taggedWith[AgeTag]
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

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  Array(1, 2, 3).map(i => i.taggedWith[AgeTag])

  // (5) Pattern matching behaviour
  // name match { // Does not compile
  //   case "Joe" => println("Correct pattern matching behaviour")
  //   case _ => println("Incorrect pattern matching behaviour")
  // }

  // (6) Regular equality
  println(s"This should be true: ${name == "Joe"}")

  // === Library support
  case class TaggedCaseClass(name: String @@ NameTag, age: Int @@ AgeTag)
  import com.softwaremill.tagging.AnyTypeclassTaggingCompat._

  // Circe
  implicit val circeDecoderString = implicitly[Decoder[String]]
  implicit val circeDecoderTaggedString = implicitly[Decoder[String @@ NameTag]]
  implicit val circeDecoderInt = implicitly[Decoder[Int]]
  implicit val circeDecoderIntString = implicitly[Decoder[Int @@ AgeTag]]
  // It is unclear why this does not work, even with the helper implicits above
  //val circeDecoder = implicitly[Decoder[TaggedCaseClass]] // Does not compile
  //val circeEncoder = implicitly[Encoder[TaggedCaseClass]] // Does not compile

  // Spray
  val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply)

  // Scalacheck
  val arbitraryName = implicitly[Arbitrary[String @@ NameTag]]
  val arbitraryAge = implicitly[Arbitrary[Int @@ AgeTag]]

  // Scanamo
  // val scanamoFormat = implicitly[DynamoFormat[TaggedCaseClass]] // Does not compile

}
