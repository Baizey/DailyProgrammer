package TBD;

import lsm.helpers.IO.write.text.console.Note;

@SuppressWarnings("WeakerAccess")
public class Bitwise {

    public static void main(String... args) {
        Bitwise value = new Bitwise(10);
        Note.writenl(value.add(-10));
        Note.writenl(value.sub(-15));
        Note.writenl(value.mul(0));
        Note.writenl(value.div(3));

    }

    private int value;

    public Bitwise(int value) {
        this.value = value;
    }

    public Bitwise neg() {
        value = neg(value);
        return this;
    }

    public Bitwise add(Bitwise other) {
        return add(other.value);
    }

    public Bitwise sub(Bitwise other) {
        return sub(other.value);
    }

    public Bitwise mul(Bitwise other) {
        return mul(other.value);
    }

    public Bitwise div(Bitwise other) {
        return mul(other.value);
    }

    public Bitwise add(int other) {
        int carry = value & other;
        value ^= other;
        while (carry != 0) {
            int shift = carry << 1;
            carry = value & shift;
            value ^= shift;
        }
        return this;
    }

    private int neg(int value) {
        return new Bitwise(~value).add(1).value;
    }

    public Bitwise sub(int other) {
        return add(neg(other));
    }

    public Bitwise mul(int other) {
        Bitwise b = new Bitwise(other);
        boolean isNeg = makeAbsolute(this, b);

        Bitwise result = new Bitwise(0);
        if ((b.value & 1) == 1) result.add(value);
        for (int mask = 2; mask != 0; mask <<= 1) {
            int times = (mask & b.value) >>> 1;
            if (times != 0) result.add(value << times);
        }

        if (isNeg) result.neg();
        value = result.value;
        return this;
    }

    public Bitwise div(int other) {
        Bitwise divisor = new Bitwise(other);
        boolean isNeg = makeAbsolute(this, divisor);

        Bitwise result = new Bitwise(0);
        while (divisor.value <= value) {
            int quotient = 1;
            int temp = divisor.value;
            while (temp < value) {
                temp <<= 1;
                quotient <<= 1;
            }
            if (quotient > 1) {
                temp >>>= 1;
                quotient >>>= 1;
            }
            result.add(quotient);
            sub(temp);
        }

        if (isNeg) result.neg();
        value = result.value;
        return this;
    }

    private static boolean makeAbsolute(Bitwise a, Bitwise b) {
        boolean isNeg = (a.value < 0 && b.value >= 0) || (b.value < 0 && a.value >= 0);
        if (a.value < 0) a.neg();
        if (b.value < 0) b.neg();
        return isNeg;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
