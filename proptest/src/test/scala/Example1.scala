import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class Example1 extends Properties("Example 1") {
  property("List has positive size") = forAll {
    lst: List[Int] => lst.size >= 0
  }
}
