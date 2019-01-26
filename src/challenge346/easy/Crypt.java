package challenge346.easy;

import lsm.datastructures.permutation.Permutations;
import lsm.datastructures.time.Time;
import lsm.helpers.IO.write.text.console.Note;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.IntStream;

public class Crypt {

    // Turn everything into addition only
    static final long[][] lookup = new long[10][19];
    static {
        long val;
        for(int i = 0; i < lookup.length; i++) {
            val = i;
            for(int j = 0; j < lookup[i].length; j++, val *= 10L)
                lookup[i][j] = val;
        }
    }

    public static void main(String... args) throws Exception {
        String[] problems = {
                "\"WHAT + WAS + THY == CAUSE\"",
                "\"HIS + HORSE + IS == SLAIN\"",
                "\"HERE + SHE == COMES\"",
                "\"FOR + LACK + OF == TREAD\"",
                "\"I + WILL + PAY + THE == THEFT\"",
                "\"TEN + HERONS + REST + NEAR + NORTH + SEA + SHORE + AS + TAN + TERNS + SOAR + TO + ENTER + THERE + AS + HERONS + NEST + ON + STONES + AT + SHORE + THREE + STARS + ARE + SEEN + TERN + SNORES + ARE + NEAR == SEVVOTH\"",
                "\"SO + MANY + MORE + MEN + SEEM + TO + SAY + THAT + THEY + MAY + SOON + TRY + TO + STAY + AT + HOME +  SO + AS + TO + SEE + OR + HEAR + THE + SAME + ONE + MAN + TRY + TO + MEET + THE + TEAM + ON + THE + MOON + AS + HE + HAS + AT + THE + OTHER + TEN == TESTS\"",
                "\"THIS + A + FIRE + THEREFORE + FOR + ALL + HISTORIES + I + TELL + A + TALE + THAT + FALSIFIES + ITS + TITLE + TIS + A + LIE + THE + TALE + OF + THE + LAST + FIRE + HORSES + LATE + AFTER + THE + FIRST + FATHERS + FORESEE + THE + HORRORS + THE + LAST + FREE + TROLL + TERRIFIES + THE + HORSES + OF + FIRE + THE + TROLL + RESTS + AT + THE + HOLE + OF + LOSSES + IT + IS + THERE + THAT + SHE + STORES + ROLES + OF + LEATHERS + AFTER + SHE + SATISFIES + HER + HATE + OFF + THOSE + FEARS + A + TASTE + RISES + AS + SHE + HEARS + THE + LEAST + FAR + HORSE + THOSE + FAST + HORSES + THAT + FIRST + HEAR + THE + TROLL + FLEE + OFF + TO + THE + FOREST + THE + HORSES + THAT + ALERTS + RAISE + THE + STARES + OF + THE + OTHERS + AS + THE + TROLL + ASSAILS + AT + THE + TOTAL + SHIFT + HER + TEETH + TEAR + HOOF + OFF + TORSO + AS + THE + LAST + HORSE + FORFEITS + ITS + LIFE + THE + FIRST + FATHERS + HEAR + OF + THE + HORRORS + THEIR + FEARS + THAT + THE + FIRES + FOR + THEIR + FEASTS + ARREST + AS + THE + FIRST + FATHERS + RESETTLE + THE + LAST + OF + THE + FIRE + HORSES + THE + LAST + TROLL + HARASSES + THE + FOREST + HEART + FREE + AT + LAST + OF + THE + LAST + TROLL + ALL + OFFER + THEIR + FIRE + HEAT + TO + THE + ASSISTERS + FAR + OFF + THE + TROLL + FASTS + ITS + LIFE + SHORTER + AS + STARS + RISE + THE + HORSES + REST + SAFE + AFTER + ALL + SHARE + HOT + FISH + AS + THEIR + AFFILIATES + TAILOR + A + ROOFS + FOR + THEIR + SAFE == FORTRESSES\""
        };

        Time.takeTime(() -> {
            for (String str : problems)
                Time.takeTime(str, () -> solve(str));
        });
    }

    private static void solve(String input) {
        String[] words = input.replaceAll("[\" ]", "").split("\\+|==");
        HashMap<Character, Value> map = new HashMap<>();
        Arrays.stream(words)
                .forEach(word -> word.chars().mapToObj(i -> (char) i)
                        .forEach(c -> map.put(c, new Value())));
        for(int i = 'a'; i < 'z' && map.size() < 10; i++)
            map.put((char) i, new Value());

        Sentence    left = new Sentence(Arrays.copyOf(words, words.length - 1), map),
                    right = new Sentence(new String[]{words[words.length - 1]}, map);

        HashSet<Character> firstLetters = new HashSet<>();
        Arrays.stream(words).forEach(word -> firstLetters.add(word.charAt(0)));

        Character[] array = map.keySet().toArray(new Character[map.size()]);

        Character[] result = Permutations.stream(array)
                .peek(perm -> IntStream.range(0, perm.length).forEach(i -> map.get(perm[i]).value = i))
                .filter(perm -> !firstLetters.contains(perm[0]) && left.sum() == right.sum())
                .findFirst().orElse(null);

        if(result == null) Note.writenl("Done fuck up");
        else Note.writenl(map);
    }

}

class Sentence {
    private final Word[] words;
    Sentence(String[] strings, HashMap<Character, Value> map){
        words = new Word[strings.length];
        for(int i = 0; i < words.length; i++)
            words[i] = new Word(strings[i], map);
    }
    long sum(){ return Arrays.stream(words).mapToLong(Word::sum).sum(); }
}

class Word {
    private final Letter[] letters;
    Word(String word, HashMap<Character, Value> map) {
        letters = new Letter[word.length()];
        for(int i = 0; i < letters.length; i++)
            letters[i] = new Letter(letters.length - i - 1, map.get(word.charAt(i)));
    }
    long sum(){ return Arrays.stream(letters).mapToLong(Letter::sum).sum(); }
}

class Letter {
    private final int index;
    private final Value value;
    Letter(int index, Value value) {
        this.index = index;
        this.value = value;
    }
    long sum(){ return Crypt.lookup[value.value][index]; }
}

class Value {
    int value;
    public String toString(){ return String.valueOf(value); }
}