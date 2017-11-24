package challenge341.easy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RepeatingNumbers {

    public static void main(String... args) {
        find("11325992321982432123259");
        find("9870209870409898");
        find("82156821568221");
        find("11111011110111011");
        find("98778912332145");
        find("124489903108444899");
    }

    private static void find(String input){
        System.out.println("\n" + input);
        IntStream.range(0, input.length() - 2).boxed()
                .flatMap(i -> IntStream.rangeClosed(i + 2, input.length())
                        .mapToObj(j -> input.substring(i, j)))
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .entrySet().stream()
                .filter(i -> i.getValue() >= 2)
                .map(i -> i.getKey() + ":" + i.getValue())
                .forEach(System.out::println);
    }

}
