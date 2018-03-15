package challenge347.easy;


import lsm.helpers.IO.write.text.console.Note;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class LightsOn {

    public static void main(String... args) {
        String in = "15 18\n" +
                "13 16\n" +
                "9 12\n" +
                "3 4\n" +
                "17 20\n" +
                "9 11\n" +
                "17 18\n" +
                "4 5\n" +
                "5 6\n" +
                "4 5\n" +
                "5 6\n" +
                "13 16\n" +
                "2 3\n" +
                "15 17\n" +
                "13 14";
        Note.writenl(solve(in));
    }

    private static int solve(String input) {
        int[] asInteger = Pattern.compile("[ \n]").splitAsStream(input).mapToInt(Integer::parseInt).toArray();
        Activity[] times = IntStream.range(0, asInteger.length).mapToObj(i -> new Activity(asInteger[i], i % 2 == 0)).toArray(Activity[]::new);
        Arrays.sort(times, Comparator.comparingInt(a -> a.time));
        int inside = 1, last = 0, time = 0;
        for (int i = 1; i < times.length; i++) {
            if (inside == 1 && !times[i].entering)
                time += -times[last].time + times[(last = i)].time;
            else if (inside == 0) last = i;
            inside += times[i].entering ? 1 : -1;
        }
        return time;
    }

}

class Activity {
    boolean entering;
    int time;
    Activity(int time, boolean entering) {
        this.time = time;
        this.entering = entering;
    }
}