import org.scalacheck.{Arbitrary, Prop, Properties, Shrink}
import org.scalacheck.Prop.forAll
import org.scalacheck.util.{Buildable, Pretty}

object Test extends Properties("Test1") {
  property("p1") = forAll {
    lst: List[Int] => lst.size >= 0
  }

  // Expands to:

  property.update(
    "p1extended",
    Prop.forAll[List[Int], Boolean](
      f = (lst: List[Int]) => lst.size.>=(0)
    )(
      p = (b: Boolean) => Prop.propBoolean(b),
      a1 = Arbitrary.arbContainer[List, Int](
        a = Arbitrary.arbInt,
        b = Buildable.buildableFactory[Int, List[Int]](
          List.iterableFactory[Int]
        ),
        t = $conforms[List[Int]]),
      s1 = Shrink.shrinkContainer[List, Int](
        $conforms[List[Int]],
        Shrink.shrinkIntegral[Int](Numeric.IntIsIntegral),
        Buildable.buildableFactory[Int, List[Int]](List.iterableFactory[Int])
      ),
      pp1 = (l: List[Any]) => Pretty.prettyList(l)
    )
  )
}
