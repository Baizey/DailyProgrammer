package TBD;

import lsm.helpers.IO.write.text.console.Note;

import java.util.Arrays;

public class RabbitColony {

    private long month = 0,
            matureFemales = 0,
            aliveRabbits,
            deadRabbits = 0;
    private int maturity, maxAge;
    private long[] males, females;

    private RabbitColony(long[] males, long[] females, int maxAge, int maturity) {
        this.maxAge = maxAge;
        this.maturity = maturity;
        this.males = new long[maxAge];
        this.females = new long[maxAge];
        System.arraycopy(males, 0, this.males, 0, males.length);
        System.arraycopy(females, 0, this.females, 0, females.length);
        matureFemales = Arrays.stream(females).skip(maturity).sum();
        aliveRabbits = Arrays.stream(males).sum() + Arrays.stream(females).sum();
    }

    private void progress() {
        month++;

        long newMales = matureFemales * 5L,
            newFemales = matureFemales * 9L;

        aliveRabbits += newMales + newFemales - males[maxAge - 1] - females[maxAge - 1];
        deadRabbits += males[maxAge - 1] + females[maxAge - 1];

        matureFemales -= females[maxAge - 1];

        System.arraycopy(males, 0, males, 1, males.length - 1);
        System.arraycopy(females, 0, females, 1, females.length - 1);

        males[0] = newMales;
        females[0] = newFemales;

        matureFemales += females[maturity];

        Note.writenl("~~~~~~~~~~~~~~~~ Month: " + month);
        Note.writenl("Mature: " + matureFemales);
        Note.writenl("Alive: " + aliveRabbits);
        Note.writenl(females);

        // Note.writenl(males); Note.writenl(females);
    }

    public static void main(String... args) {

        long[] males = new long[3];
        males[2] = 2;
        long[] females = new long[3];
        females[2] = 1;

        RabbitColony colony = new RabbitColony(males, females, 8 * 12, 4);
        long goal = 1000000000L;
        while(colony.aliveRabbits < goal && colony.aliveRabbits > 0)
            colony.progress();
        Note.writenl(colony.month);
    }

}
