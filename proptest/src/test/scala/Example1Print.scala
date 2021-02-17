import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class Example1Print extends Properties("Example 1 with print") {
  property("List has positive size") = forAll {
    lst: List[Int] => {
      println(lst)
      lst.size >= 0
    }
  }
}
