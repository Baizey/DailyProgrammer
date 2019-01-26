package challenge373.hard;

import lsm.datastructures.time.Time;
import lsm.helpers.IO.read.text.TextReader;
import org.junit.Test;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static challenge373.hard.Node.from;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Solver {

    private static Map<String, Boolean> equalTests = new HashMap<>() {{
        put("((())()) (()(()))", true);
        put("((()((())()))(())) ((())(()(()(()))))", true);
        put("((())) (()())", false);
        put("(((()())())()()) (((()())()())())", false);
    }};

    private static Map<String, Boolean> embedTests = new HashMap<>() {{
        put("(()()) (())", true);
        put("((()())()) (()()())", false);
    }};

    public static void main(String... args) throws Exception {
        Time.using(Time.AUTO);
        Time.takeTime("Challenge", Solver::challenge);
    }

    private static void challenge() {
    }

    @Test
    public void embeddableBasicTest() {
        embedTests.forEach((key, expected) -> {
            var str = key.split(" ");
            var actual = from(str[0]).embeddable(from(str[1]));
            assertThat(Arrays.toString(str), actual, (is(equalTo(expected))));
        });
    }

    @Test
    public void embeddableAdvancedTest() throws IOException {
        var actual = TextReader.read("https://gist.githubusercontent.com/cosmologicon/dcf49d29c563dfc36a3d1c5053124be4/raw/9c663c5a9071571f041d11b08bf3c8958a22b3dd/tree-embed.txt").stream()
                             .map(line -> line.split(" "))
                             .map(line -> new SimpleEntry<>(from(line[0]), from(line[1])))
                             .filter(set -> set.getKey().embeddable(set.getValue()))
                             .count();

        assertThat(actual, (is(equalTo(138L))));
    }

    @Test
    public void equalsBasicTest() {
        equalTests.forEach((key, expected) -> {
            var str = key.split(" ");
            var actual = from(str[0]).equals(from(str[1]));
            assertThat(Arrays.toString(str), actual, (is(equalTo(expected))));
        });
    }

    @Test
    public void equalsAdvancedTest() throws IOException {
        var actual = TextReader.read("https://gist.githubusercontent.com/cosmologicon/be38523b48f7da5ab9c886fca94a57b4/raw/37abb03b7365ff17056f7f59beb77d999fd6c81b/tree-equal.txt").stream()
                             .map(line -> line.split(" "))
                             .map(line -> new SimpleEntry<>(from(line[0]), from(line[1])))
                             .filter(set -> set.getKey().equals(set.getValue()))
                             .count();

        assertThat(actual, (is(equalTo(121L))));
    }

}


