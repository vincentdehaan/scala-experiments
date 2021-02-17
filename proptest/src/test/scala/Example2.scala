import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class Example2 extends Properties("Example 2") {
  property("This property fails") = forAll {
    lst: List[Int] => lst.size > 0
  }

  property("This non-trivial property fails") = forAll {
    lst: List[Int] => if(lst.size > 4) lst(3) > 3 else true
  }
}
