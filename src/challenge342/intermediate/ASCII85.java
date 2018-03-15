package challenge342.intermediate;


import lsm.helpers.IO.write.text.console.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ASCII85 {

    public static void main(String... args) {
        String[] strings = {
                "e Attack at dawn",
                "d 87cURD_*#TDfTZ)+T",
                "d 06/^V@;0P'E,ol0Ea`g%AT@",
                "d 7W3Ei+EM%2Eb-A%DIal2AThX&+F.O,EcW@3B5\\\\nF/hR",
                "e Mom, send dollars!",
                "d 6#:?H$@-Q4EX`@b@<5ud@V'@oDJ'8tD[CQ-+T"
        };
        Arrays.stream(strings).map(ASCII85::solve).forEach(Note::writenl);
    }

    private static final int[] powers = new int[5];
    static {
        powers[0] = 1;
        for(int i = 1; i < powers.length; i++)
            powers[i] = powers[i - 1] * 85;
    }

    public static String solve(String in) {
        return in.charAt(0) == 'e' ? encode(in.substring(2)) : decode(in.substring(2));
    }

    private static String encode(String input){
        return input.chars().boxed()
                .collect(blockCollector(4)).stream()
                .map(arr -> IntStream.range(0, arr.size())
                        .mapToLong(i -> arr.get(i) << (24 - i * 8)).sum())
                .flatMap(sum -> {
                    int[] res = new int[5];
                    for(int i = res.length - 1; i >= 0; i--, sum /= 85)
                        res[i] = (int)((sum + 33) % 85);
                    return Arrays.stream(res).boxed();
                }).map(Character::toChars).map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String decode(String input){
        return input.chars().boxed()
                .collect(blockCollector(5)).stream()
                .map(arr -> IntStream.range(0, arr.size())
                        .mapToLong(i -> (arr.get(i) - 33) * powers[4 - i]).sum())
                .flatMap(x -> IntStream.range(0, 4)
                        .mapToObj(i -> (int) (x >> (24 - 8 * i)) % 256))
                .map(i -> "" + ((char) i.intValue()))
                .collect(Collectors.joining());
    }

    private static Collector<Integer, List<List<Integer>>, List<List<Integer>>> blockCollector(int blockSize) {
        return Collector.of(
                ArrayList<List<Integer>>::new,
                (list, value) -> {
                    List<Integer> block = (list.isEmpty() ? null : list.get(list.size() - 1));
                    if (block == null || block.size() == blockSize)
                        list.add(block = new ArrayList<>(blockSize));
                    block.add(value);
                },
                (r1, r2) -> { throw new UnsupportedOperationException("Parallel processing not supported"); }
        );
    }
}