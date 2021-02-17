import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.ScalacheckShapeless._

class Example4 extends Properties("Example 4") {
  case class MyADT(s: String, i: Int, t: SubType)
  case class SubType(i1: Int, i2: Int)

  property("This property fails") = forAll {
    adt: MyADT => adt.s.length >= 0
  }
}
