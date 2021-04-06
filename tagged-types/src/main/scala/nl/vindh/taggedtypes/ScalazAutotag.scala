package nl.vindh.taggedtypes

import io.chrisdavenport.cormorant._
import io.chrisdavenport.cormorant.generic.semiauto._
import io.chrisdavenport.cormorant.implicits._
import io.circe.{Decoder, Encoder}
import scalaz.{@@, Tag}
import io.circe.shapes._
import io.circe.generic.auto._
import org.scalacheck.Arbitrary
import org.scanamo.DynamoFormat
import spray.json.DefaultJsonProtocol._
import org.scanamo.auto._

object ScalazAutotag extends App {
  sealed trait NameTag
  sealed trait AgeTag

  type Name = String @@ NameTag
  type Age = Int @@ AgeTag

  implicit def tag[A, Tag](untagged: A): A @@ Tag = Tag[A, Tag](untagged)
  implicit def untag[A, Tag](tagged: A @@ Tag): A = tagged.asInstanceOf[A]

  def requireString(s: String): String = s
  def requireInt(i: Int): Int = i
  def requireName(n: Name): String = n.toString
  def requireAge(a: Age): Int = a

  val name: Name = "Joe"
  val age: Age = 30
  val str: String = "String"
  val int: Int = 12

  // (1) Type checking: on a place where a tagged type is expected, a tagged type can be substituted.
  requireName(name)
  requireAge(age)

  // (2) Non-automatic upcasting: a variable of an untagged type can __not__ be substituted on a place where a corresponding tagged type is expected:
  requireName(str) // This should not compile, but it does because of implicit def tag
  requireAge(int) // This should not compile, but it does

  // (3) Automatic downcasting: a variable of a tagged type can be substituted on a place where a corresponding untagged type is expected:
  requireString(name)
  requireInt(age)

  // (4) `ClassTag` inference: the compiler is able to find a suitable `ClassTag` for the type.
  // Array(1, 2, 3).map(i => i: Age) // Does not compile

  // (5) Pattern matching behaviour
  name match {
    case "Joe" => println("Correct pattern matching behaviour")
    case _ => println("Incorrect pattern matching behaviour")
  }

  // (6) Regular equality
  println(s"This should be true: ${name == "Joe"}")

  // (7) Built-in type classes
  val equiv = implicitly[Equiv[Age]]
  //val ordering = implicitly[Ordering[Age]]
  //val numeric = implicitly[Numeric[Age]]

  // === Library support
  case class TaggedCaseClass(name: String @@ Name, age: Int @@ Age)

  // Circe
  //val circeDecoder = implicitly[Decoder[TaggedCaseClass]] // Does not compile
  //val circeEncoder = implicitly[Encoder[TaggedCaseClass]] // Does not compile

  // Spray
  // val sprayJsonFormat = jsonFormat2(TaggedCaseClass.apply) // Does not compile

  // Scalacheck
  // val arbitraryName = implicitly[Arbitrary[String @@ Name]] // Does not compile
  // val arbitraryAge = implicitly[Arbitrary[Int @@ Age]] // Does not compile

  // Scanamo
  // val scanamoFormat = implicitly[DynamoFormat[TaggedCaseClass]] // Does not compile

  // Cormorant
  // val lw: LabelledWrite[TaggedCaseClass] = deriveLabelledWrite // Does not compile
  // val lr: LabelledRead[TaggedCaseClass] = deriveLabelledRead // Does not compile
}
