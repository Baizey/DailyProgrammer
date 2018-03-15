package challenge352.inter;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.Time;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SevenWonders {

    static final String[] problems = new String[]{
            "Cards [W/B/S/O, W, S/B, S]. Can you make WWSS?",
            "Cards [W/B/S/O, S/O, W/S, W/B, W/B, W, B]. Can you make WWBSSOO?",
            "Cards [A/B/D/E, A/B/E/F/G, A/D, A/D/E, A/D/E, B/C/D/G, B/C/E, B/C/E/F, B/C/E/F, B/D/E, B/D/E, B/E/F, C/D/F, C/E, C/E/F/G, C/F, C/F, D/E/F/G, D/F, E/G]. Can you make AABCCCCCCDDDEEEEFFGG?",
            "Cards [A/C/G/K/L/O/R/S, A/D/H/I/M/Q, A/D/K/W/X, A/D/M/U/Z, A/E/J/M/T, A/G/H/I/M/R/T/Z, A/G/M/T/U, A/H/I/J/Q, B/C/Q/U/V, B/D/F/K/M/R/W/Y, B/F/P/T/U/W/Y, B/G/K/M/S/T/X/Y, C/E/F/I/K/N/O, D/E/G/J/M/Q/Z, D/G/I/R/Z, D/H/I/T/U, E/G/H/J/M/Q, E/G/H/J/Q/R/T/U, E/G/J/M/Z, E/H/I/Q/T/U/Z, E/J/O/S/V/X, F/G/H/N/P/V, F/G/N/P/R/S/Z, F/I/M/Q/R/U/Z, F/L/M/P/S/V/W/Y, G/H/J/M/Q]. Can you make ABCDEFGHIJKLMNOPQRSTUVWXYZ?"
    };

    public static void main(String... args) {
        Time.init();
        for(String problem : problems)
            Time.takeTime(problem.split("Can you make ")[1].replace("?", ""), () ->
                    Note.writenl(solve(problem) ? "" : "N/A"));
        Time.write();
    }

    static boolean solve(String input) {
        String[] parts = input.replaceAll("^Cards \\[(.*)]\\. Can you make (.*)\\?$", "$1-$2").split("-");
        return solve(parts[0].split(", "), parts[1]);
    }

    private static boolean solve(String[] optionInput, String goalInput) {

        Node[] options = new Node[optionInput.length];
        for(int i = 0; i < options.length; i++)
            options[i] = new Node(optionInput[i]);
        int[] needed = new int[255];
        for(char c : goalInput.toCharArray())
            needed[c]++;
        int[] goalChars = goalInput.chars().boxed()
                .collect(Collectors.toSet()).stream()
                .mapToInt(i -> i).toArray();

        IntList best = new IntList(options.length);
        IntList genBest = new IntList(options.length);
        IntList curr = new IntList(options.length);

        int bestScore = 9999;
        for(int generation = 0; generation < 100; generation++) {
            int genBestScore = 999;
            genBest.reset();
            for(int pop = 0; pop < 100; pop++) {
                curr.reset();
                for (Node option : options)
                    curr.add(option.getRandomOption());
                int[] currChars = new int[255];
                for(int i = 0; i < curr.size; i++)
                    currChars[options[i].cards[curr.array[i]]]++;
                int currScore = 0;
                for(int goal : goalChars)
                    currScore += Math.max(0, needed[goal] - currChars[goal]);
                if(currScore < genBestScore) {
                    genBest.copyOver(curr);
                    genBestScore = currScore;
                }
            }
            for(int i = 0; i < options.length; i++)
                options[i].promoteOption(genBest.array[i]);
            if(genBestScore < bestScore) {
                best.copyOver(genBest);
                bestScore = genBestScore;
                if(bestScore == 0) break;
            }
        }

        if(bestScore == 0){
            Note.write("Solution: ");
            for(int i = 0; i < options.length; i++)
                Note.write((char) options[i].cards[best.array[i]]);
        }

        return bestScore == 0;
    }

}

class IntList {
    final int[] array;
    int size = 0;
    IntList(int size){ this.array = new int[size]; }
    void reset(){ this.size = 0; }
    void add(int value){ array[size++] = value; }
    void copyOver(IntList other) {
        size = other.size;
        System.arraycopy(other.array, 0, array, 0, size);
    }
}

class Node {

    int[] cards;
    double[] takeChance;

    Node(String rawOptions) {
        cards = Arrays.stream(rawOptions.split("/")).mapToInt(i -> i.charAt(0)).toArray();
        takeChance = new double[cards.length];
        Arrays.fill(takeChance, 1.0 / cards.length);
    }

    int getRandomOption() {
        double pick = Math.random();
        for (int i = 0; i < cards.length; i++) {
            pick -= takeChance[i];
            if (pick <= 0) return i;
        }
        return 0;
    }

    void promoteOption(int index) {
        double adding = 0;
        for (int i = 0; i < takeChance.length; i++) {
            double taking = takeChance[i] * 0.05;
            adding += taking;
            takeChance[i] -= taking;
        }
        takeChance[index] += adding;
    }

}