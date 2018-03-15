import lsm.helpers.IO.write.text.TextWriter;
import lsm.helpers.Time;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shitpost {

    private static String longInput;
    private static String longStart = "abcdefghijklmnop";

    public static void main(String... args) throws Exception {
        Time.init();
        createChallenge("abcdefg", 30);
        //BufferedReader reader =  TextReader.getWebsiteReader("https://pastebin.com/raw/2wA04VUR");
    }

    private static void createChallenge(String start, int moves) throws IOException {
        HashMap<Integer, IntWrap> lookup = new HashMap<>();
        IntWrap[] perm = IntStream.range(0, start.length()).mapToObj(IntWrap::new).toArray(IntWrap[]::new);
        for (IntWrap wrap : perm) lookup.put(wrap.value, wrap);
        IntWrap t1, t2;
        IntWrap[] temp = new IntWrap[perm.length];
        int a, b, n;

        Random random = new Random();
        StringBuilder move = new StringBuilder();
        String[] moveList = new String[moves];
        for(int i = 0; i < moves; i++) {
            if(i > 0) move.append(",");
            switch(random.nextInt(2)) {
                case 0:
                    move = new StringBuilder("s");
                    a = random.nextInt(perm.length - 2) + 1;
                    move.append(a);
                    System.arraycopy(perm, 0, temp, 0, a);
                    System.arraycopy(perm, a, perm, 0, perm.length - a);
                    System.arraycopy(temp, 0, perm, perm.length - a, a);
                    break;
                case 1:
                    move = new StringBuilder("x");
                    a = random.nextInt(perm.length);
                    do { b = random.nextInt(perm.length); } while(b == a);
                    move.append(a).append("/").append(b);
                    t1 = perm[a];
                    perm[a] = perm[b];
                    perm[b] = t1;
                    break;
                case 2:
                    move = new StringBuilder("p");
                    a = random.nextInt(perm.length);
                    do { b = random.nextInt(perm.length); } while(b == a);
                    move.append(a).append("/").append(b);
                    t1 = lookup.get(a);
                    t2 = lookup.get(b);
                    n = t1.value;
                    t1.value = t2.value;
                    t2.value = n;
                    lookup.put(t1.value, t1);
                    lookup.put(t2.value, t2);
                    break;
            }
            moveList[moves - 1 - i] = move.toString();
        }
        for(int i = 0; i < moveList.length; i++) {
            if(moveList[i].charAt(0) != 'p') continue;
            int[] nums = Arrays.stream(moveList[i].substring(1).split("/")).mapToInt(Integer::parseInt).toArray();
            for(int k = 0; k < nums.length; k++)
                for(int j = 0; j < perm.length; j++)
                    if(perm[j].value == nums[k])
                        nums[k] = j;
            moveList[i] = "p" + nums[0] + "/" + nums[1];
        }

        BufferedWriter writer = TextWriter.getWriter("output", "txt", true);
        writer.write(Arrays.stream(perm).map(i -> String.valueOf(start.charAt(i.value))).collect(Collectors.joining()));
        writer.write("\n");
        writer.write(String.join(",", moveList));
        writer.flush();
        writer.close();
    }

    public static String solve(String start, String input) {
        String[] moves = input.split(",");

        HashMap<Integer, IntWrap> lookup = new HashMap<>();
        IntWrap[] perm = IntStream.range(0, start.length()).mapToObj(IntWrap::new).toArray(IntWrap[]::new);
        for (IntWrap wrap : perm) lookup.put(wrap.value, wrap);

        IntWrap t1, t2;
        IntWrap[] temp = new IntWrap[perm.length];
        int a, b, n;
        for (String move : moves) {
            switch (move.charAt(0)) {
                case 's':
                    n = Integer.parseInt(move.substring(1));
                    System.arraycopy(perm, perm.length - n, temp, 0, n);
                    System.arraycopy(perm, 0, perm, n, perm.length - n);
                    System.arraycopy(temp, 0, perm, 0, n);
                    break;
                case 'x':
                    a = Integer.parseInt(move.substring(1).split("/")[0]);
                    b = Integer.parseInt(move.substring(1).split("/")[1]);
                    temp[0] = perm[a];
                    perm[a] = perm[b];
                    perm[b] = temp[0];
                    break;
                case 'p':
                    a = Integer.parseInt(move.substring(1).split("/")[0]);
                    b = Integer.parseInt(move.substring(1).split("/")[1]);
                    t1 = lookup.get(a);
                    t2 = lookup.get(b);
                    n = t1.value;
                    t1.value = t2.value;
                    t2.value = n;
                    lookup.put(t1.value, t1);
                    lookup.put(t2.value, t2);
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (IntWrap wrap : perm)
            sb.append(start.charAt(wrap.value));
        return sb.toString();
    }


}

class IntWrap {
    int value;
    IntWrap(int value) {
        this.value = value;
    }
}