package challenge350.inter;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.IO.write.text.console.Padding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Balancing {

    public static void main(String... args) {
        String[][] problems = new String[][]{
                {"0 -3 5 -4 -2 3 1 0", "->", ""},
                {"3 -2 2 0 3 4 -6 3 5 -4 8", "->", ""},
                {"9 0 -5 -4 1 4 -4 -9 0 -7 -1", "->", ""},
                {"9 -7 6 -8 3 -9 -5 3 -6 -8 5", "->", ""}
        };
        IntStream.range(0, problems.length)
                .forEach(i -> problems[i][2] = solve(problems[i][0]).toString());
        Note.setTags("", "").setSeperator(" ").setPadding(Padding.LEFT, Padding.VERTICAL)
                .writenl(problems);
    }

    static List<Integer> solve(String in){
        return solve(Pattern.compile(" ").splitAsStream(in).mapToInt(Integer::parseInt).toArray());
    }

    private static List<Integer> solve(int[] ints) {
        int left = 0;
        int right = Arrays.stream(ints).sum();
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < ints.length; i++) {
            right -= ints[i];
            if(left == right) indexes.add(i);
            left += ints[i];
        }
        return indexes;
    }
}