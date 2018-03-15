package challenge354.easy;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.IO.write.text.console.Padding;
import lsm.helpers.Time;
import natural.GA.DNA;
import natural.GA.Individual;
import natural.GA.Population;
import natural.GA.crossover.Crossover;
import natural.GA.mutations.Mutation;
import natural.GA.preCalc.PreCalcs;
import natural.GA.select.Selection;

import java.math.BigInteger;
import java.util.Arrays;

public class Complexity {


    public static void main(String... args) {
        long[] problems = new long[]{12, 456, 4567, 12345};
        Time.init();
        String[][] solutions = Arrays.stream(problems).mapToObj(Complexity::solve).toArray(String[][]::new);
        Note.setTags("", "").setSeperator("").setPadding(Padding.RIGHT, Padding.VERTICAL).write(solutions);
        long[] primes = Arrays.stream(("3*3*3*53*79*1667*20441*19646663*89705489".split("\\*"))).mapToLong(Long::parseLong).toArray();
        Note.writenl(solve(1234567891011L));
        Note.writenl(solve(primes));
        Time.write();
    }


    public static String[] solve(long[] rawPrime) {
        BigInteger[] primes = Arrays.stream(rawPrime).mapToObj(BigInteger::valueOf).toArray(BigInteger[]::new);
        BigInteger value = BigInteger.ONE;
        for(long prime : rawPrime) value = value.multiply(BigInteger.valueOf(prime));
        BigInteger finalValue = value;
        Population population = new Population(100, rawPrime.length, true, true, 1D,
                Mutation.alwaysFlipOne(),
                individual -> {
                    BigInteger a = BigInteger.ONE;
                    BigInteger b = BigInteger.ONE;
                    DNA dna = individual.getDna();
                    for(int i = 0; i < individual.getLength(); i++) {
                        if(dna.get(i)) a = a.multiply(primes[i]);
                        else b = b.multiply(primes[i]);
                    }
                    if(finalValue.compareTo(a.multiply(b)) == 0)
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
            if(dna.get(i)) a = a.multiply(primes[i]);
            else b = b.multiply(primes[i]);
        }
        return new String[]{String.valueOf(value), " => ", String.valueOf(a), " + ", String.valueOf(b), " = ", String.valueOf((a.add(b)))};
    }

    private static String[] solve(long value) {
        for (long i = (long) (Math.sqrt(value) + 1L); i > 0L; i--) {
            long left = value / i;
            if (left * i == value)
                return new String[]{String.valueOf(value), " => ", String.valueOf(left), " + ", String.valueOf(i), " = ", String.valueOf((left + i))};
        }
        return null;
    }
}
