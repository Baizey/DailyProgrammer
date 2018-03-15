package challenge354.inter;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.utils.Numbers;
import natural.GA.DNA;
import natural.GA.Individual;
import natural.GA.Population;
import natural.GA.crossover.Crossover;
import natural.GA.mutations.Mutation;
import natural.GA.preCalc.PreCalcs;
import natural.GA.select.Selection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Complexity {

    public static void main(String... args) {
        int start = 1;
        int end = 1000;

        for (int i = start; i <= end; i++)
            Note.writenl(i + ": " + Node.get(i) + " = " + Node.get(i).getSum());
        Note.writenl(IntStream.range(start, end + 1).mapToLong(i -> Node.get(i).getSum().longValue()).sum());
    }
}

class Node {
    private static int[] primes = Numbers.primesInRange(0, 10000).stream().mapToInt(BigInteger::intValue).toArray();
    private static final HashMap<Integer, Node> lookup = new HashMap<>();

    public static Node get(int i) {
        return lookup.getOrDefault(i, new Node(i));
    }

    private BigInteger value, sum;

    private Node(int value) {
        this.value = BigInteger.valueOf(value);
        lookup.put(value, this);
        if (value <= 3) {
            sum = this.value;
            return;
        }
        if (Numbers.isPrime(value))
            sum = get(value - 1).sum.add(BigInteger.ONE);
        else {
            int sqrt = (int) Math.sqrt(value) + 1;
            ArrayList<BigInteger> primes = new ArrayList<>();
            for (int p : Node.primes) {
                if(p > value) break;
                for (; value % p == 0; value /= p)
                    primes.add(BigInteger.valueOf(p));
            }

            Population population = new Population(100, primes.size(), true, true, 1D,
                    Mutation.alwaysFlipOne(),
                    individual -> {
                        BigInteger a = BigInteger.ONE;
                        BigInteger b = BigInteger.ONE;
                        DNA dna = individual.getDna();
                        for(int i = 0; i < individual.getLength(); i++) {
                            if(dna.get(i)) a = a.multiply(primes.get(i));
                            else b = b.multiply(primes.get(i));
                        }
                        if(this.value.compareTo(a.multiply(b)) == 0)
                            individual.setFitness(Long.MAX_VALUE - (a.longValue() + b.longValue()));
                        else individual.setFitness(Long.MIN_VALUE);
                    },
                    Crossover.halfAndHalf(),
                    Selection.random(),
                    PreCalcs.none()
            );
            population.evolveUntilNoProgress(100);
            Individual individual = population.getBest();
            BigInteger a = BigInteger.ONE;
            BigInteger b = BigInteger.ONE;
            DNA dna = individual.getDna();
            for(int i = 0; i < individual.getLength(); i++) {
                if(dna.get(i)) a = a.multiply(primes.get(i));
                else b = b.multiply(primes.get(i));
            }
            sum = a.add(b);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        return sb.append(")").toString();
    }

    public BigInteger getSum() {
        return sum;
    }
}