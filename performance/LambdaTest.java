import java.util.stream.Stream;
import java.util.Arrays;
import java.util.function.Function;

public class LambdaTest {
    public static void main(String[] args){
        Stream<Integer> s = Arrays.asList(1, 2, 3, 4, 5).stream();

        Stream<Integer> t = handleStream(s, true);

        t.forEach(System.out::println);
    }

    public static Stream<Integer> handleStream(Stream<Integer> s, Boolean w){
        Function<Integer, Integer> a = x -> x + 77;
        Function<Integer, Integer> b = x -> x + 78;
        return s.map(w ? a : b);
    }
}