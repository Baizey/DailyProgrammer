package challenge350.hard;

import lsm.helpers.IO.write.text.console.Note;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class NumberRecurrence {

    private static final Random random = new Random();
    public static void main(String... args) {
        BigDecimal ONE = BigDecimal.ONE;
        BigDecimal chance = ONE;
        BigDecimal div = BigDecimal.valueOf(2);
        for(int i = 0; i < 16; i++)
            div = div.multiply(BigDecimal.valueOf(2));

        int i;
        for(i = 0; chance.doubleValue() > 0.9; i++) {
            chance = chance.multiply(ONE.subtract(ONE.divide(div, 20, RoundingMode.HALF_UP)));
            div = div.subtract(BigDecimal.ONE);
            Note.writenl(i + ": " + chance.doubleValue());
            if(Math.random() > chance.doubleValue()) return;
        }
        Note.writenl(i);
    }
}


class CountMinSketch {
    private int dInt, wInt;
    private BigInteger d, w, wMin1;
    private long[][] estimators;
    private BigInteger[] a, b;
    private BigInteger p = BigInteger.valueOf((long) (Math.pow(2, 31) - 1));
    private BigInteger numbersAdded = BigInteger.ZERO;

    CountMinSketch(int d, int w) {
        this.dInt = d;
        this.wInt = w;
        this.d = BigInteger.valueOf(d);
        this.w = BigInteger.valueOf(w);
        this.wMin1 = this.w.subtract(BigInteger.ONE);

        estimators = new long[d][w];
        a = new BigInteger[d];
        b = new BigInteger[d];

        Random random = new Random();
        for (int i = 0; i < d; i++) {
            a[i] = BigInteger.valueOf(Math.abs(random.nextLong())).mod(p);
            b[i] = BigInteger.valueOf(Math.abs(random.nextLong())).mod(p);
        }
    }

    void add(long value) { add(BigInteger.valueOf(value)); }
    private void add(BigInteger value) {
        numbersAdded = numbersAdded.add(BigInteger.ONE);
        for (int i = 0; i < dInt; i++) {
            int hash = hash(value, i);
            estimators[i][hash]++;
        }
    }

    long estimateFrequency(long value) { return estimateFrequency(BigInteger.valueOf(value)); }
    private long estimateFrequency(BigInteger value) {
        BigInteger e[] = new BigInteger[dInt];
        for(int i = 0; i < dInt; i++) {
            BigInteger sketchCounter = BigInteger.valueOf(estimators[i][hash(value, i)]);
            BigInteger noiseEstimation = (numbersAdded.subtract(sketchCounter)).divide(wMin1);
            e[i] = sketchCounter.subtract(noiseEstimation);
        }
        Arrays.sort(e);
        return e[e.length / 2].longValue();
    }

    private int hash(BigInteger value, int i) {
        return a[i].multiply(value).add(b[i]).mod(p).mod(w).intValue();
    }
}