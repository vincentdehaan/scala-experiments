package nl.vindh.taggedtypes

import io.chrisdavenport.cormorant.{LabelledRead, LabelledWrite}
import io.chrisdavenport.cormorant.generic.semiauto._
import io.circe.Codec
import io.circe._
import io.circe.shapes._
import io.circe.generic.auto._
import nl.vindh.taggedtypes.ScalazTagged.TaggedCaseClass
import org.scalacheck.Arbitrary
import org.scanamo.DynamoFormat
import shapeless.tag
import shapeless.tag._
import spray.json.DefaultJsonProtocol.jsonFormat2
import org.scanamo.auto._

import scala.math.Numeric.IntIsIntegral


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

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  Array(1, 2, 3).map(i => tag[AgeTag][Int](i))

  // (5) Pattern matching behaviour

  // name match { // Does not compile
  //  case "Joe" => println("Correct pattern matching behaviour")
  //  case _ => println("Incorrect pattern matching behaviour")
  //}

  // (6) Regular equality
  println(s"This should be true: ${name == "Joe"}")

  // (7) Built-in type classes
  val equiv = implicitly[Equiv[Int @@ AgeTag]]
  val ordering = implicitly[Ordering[Int @@ AgeTag]]
  //val numeric = implicitly[Numeric[Int @@ AgeTag]]


  // === Library support
  case class TaggedCaseClass(name: String @@ NameTag, age: Int @@ AgeTag)

  // Circe
  val circeDecoder = implicitly[Decoder[TaggedCaseClass]]
  val circeEncoder = implicitly[Encoder[TaggedCaseClass]]

  // Spray
  // val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply) // Does not compile

  // Scalacheck
  // val arbitraryName = implicitly[Arbitrary[String @@ NameTag]] // Does not compile
  // val arbitraryAge = implicitly[Arbitrary[Int @@ AgeTag]] // Does not compile

  // Scanamo
  // val scanamoFormat = implicitly[DynamoFormat[TaggedCaseClass]] // Does not compile

  // Cormorant
  // val lw: LabelledWrite[TaggedCaseClass] = deriveLabelledWrite // Does not compile
  // val lr: LabelledRead[TaggedCaseClass] = deriveLabelledRead // Does not compile
}