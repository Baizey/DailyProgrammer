package challenge353.easy;

import lsm.helpers.Counter;
import lsm.helpers.IO.write.text.console.Note;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ClosestString {

    private static String[] problems = {
            "CTCCATCACAC\n" +
                    "AATATCTACAT\n" +
                    "ACATTCTCCAT\n" +
                    "CCTCCCCACTC",
            "AACACCCTATA\n" +
                    "CTTCATCCACA\n" +
                    "TTTCAATTTTC\n" +
                    "ACAATCAAACC\n" +
                    "ATTCTACAACT\n" +
                    "ATTCCTTATTC\n" +
                    "ACTTCTCTATT\n" +
                    "TAAAACTCACC\n" +
                    "CTTTTCCCACC\n" +
                    "ACCTTTTCTCA\n" +
                    "TACCACTACTT",
            "ACAAAATCCTATCAAAAACTACCATACCAAT\n" +
                    "ACTATACTTCTAATATCATTCATTACACTTT\n" +
                    "TTAACTCCCATTATATATTATTAATTTACCC\n" +
                    "CCAACATACTAAACTTATTTTTTAACTACCA\n" +
                    "TTCTAAACATTACTCCTACACCTACATACCT\n" +
                    "ATCATCAATTACCTAATAATTCCCAATTTAT\n" +
                    "TCCCTAATCATACCATTTTACACTCAAAAAC\n" +
                    "AATTCAAACTTTACACACCCCTCTCATCATC\n" +
                    "CTCCATCTTATCATATAATAAACCAAATTTA\n" +
                    "AAAAATCCATCATTTTTTAATTCCATTCCTT\n" +
                    "CCACTCCAAACACAAAATTATTACAATAACA\n" +
                    "ATATTTACTCACACAAACAATTACCATCACA\n" +
                    "TTCAAATACAAATCTCAAAATCACCTTATTT\n" +
                    "TCCTTTAACAACTTCCCTTATCTATCTATTC\n" +
                    "CATCCATCCCAAAACTCTCACACATAACAAC\n" +
                    "ATTACTTATACAAAATAACTACTCCCCAATA\n" +
                    "TATATTTTAACCACTTACCAAAATCTCTACT\n" +
                    "TCTTTTATATCCATAAATCCAACAACTCCTA\n" +
                    "CTCTCAAACATATATTTCTATAACTCTTATC\n" +
                    "ACAAATAATAAAACATCCATTTCATTCATAA\n" +
                    "CACCACCAAACCTTATAATCCCCAACCACAC"
    };

    public static void main(String... args) {
        for (String problem : problems) Note.writenl(solve(problem.split("\n")));
    }

    public static String solve(String[] input) {
        Counter<Character> counter = new Counter<>();
        int[] scores = new int[input.length];
        return IntStream.range(0, input[0].length())
                .peek(i -> counter.clear())
                .peek(i -> Arrays.stream(input).forEach(str -> counter.add(str.charAt(i))))
                .peek(i -> IntStream.range(0, input.length).forEach(j -> scores[j] += input.length - counter.get(input[j].charAt(i))))
                .skip(input[0].length() - 1)
                .mapToObj(i -> input[minIndexByScore(scores)])
                .findFirst().orElse(null);
    }

    private static int minIndexByScore(int[] arr) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }
}

class hamming_center {

    public static void main(String args[]) {

        String inx = "21\nACAAAATCCTATCAAAAACTACCATACCAAT\nACTATACTTCTAATATCATTCATTACACTTT\nTTAACTCCCATTATATATTATTAATTTACCC\nCCAACATACTAAACTTATTTTTTAACTACCA\nTTCTAAACATTACTCCTACACCTACATACCT\nATCATCAATTACCTAATAATTCCCAATTTAT\nTCCCTAATCATACCATTTTACACTCAAAAAC\nAATTCAAACTTTACACACCCCTCTCATCATC\nCTCCATCTTATCATATAATAAACCAAATTTA\nAAAAATCCATCATTTTTTAATTCCATTCCTT\nCCACTCCAAACACAAAATTATTACAATAACA\nATATTTACTCACACAAACAATTACCATCACA\nTTCAAATACAAATCTCAAAATCACCTTATTT\nTCCTTTAACAACTTCCCTTATCTATCTATTC\nCATCCATCCCAAAACTCTCACACATAACAAC\nATTACTTATACAAAATAACTACTCCCCAATA\nTATATTTTAACCACTTACCAAAATCTCTACT\nTCTTTTATATCCATAAATCCAACAACTCCTA\nCTCTCAAACATATATTTCTATAACTCTTATC\nACAAATAATAAAACATCCATTTCATTCATAA\nCACCACCAAACCTTATAATCCCCAACCACAC";
        String[] arx = inx.split("\\n");

        int size = Integer.parseInt(arx[0]);
        int len = arx[1].trim().length();

        int max = 0;
        int best = len * size;

        for (int j = 1; j <= size; j++) {
            int total = 0;
            for (int i = 1; i <= size; i++) {
                int c = 0;
                for (int k = 0; k < len - 1; k++)
                    if (arx[i].charAt(k) != arx[j].charAt(k) && i != j)
                        c++;
                total = total + c;
            }
            if (total < best) {
                max = j;
                best = total;
            }
        }

        System.out.println(arx[max]);
    }
}
