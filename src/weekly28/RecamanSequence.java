package weekly28;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.Time;

import java.util.HashSet;
import java.util.stream.IntStream;

public class RecamanSequence {

    public static void main(String... args){
        int[] problems = new int[]{5, 15, 25, 100, 1005};
        Time.takeTime(() -> {
            for(int problem : problems)
                Note.writenl(solve(problem));
        });
    }

    public static int solve(int n) {
        HashSet<Integer> seen = new HashSet<>();
        final int[] prev = {0};
        IntStream.range(1, n + 1)
                .map(i -> prev[0] + ((prev[0] > i && !seen.contains(prev[0] - i)) ? -i : i))
                .forEach(i -> {seen.add(i); prev[0] = i;});
        return prev[0];
    }



}