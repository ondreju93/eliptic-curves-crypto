package pl.edu.amu.wmi.ecc;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import java.math.BigInteger;

@Data
public class Remainder {
    private final BigInteger base;
    private final BigInteger value;

    public Remainder add(final Remainder other) {
        assert other.base.equals(this.base) : "Sum operation is possible only for arguments with same base value";
        return new Remainder(base, (this.value.add(other.value)).mod(base));
    }

    public Remainder subtract(final Remainder other) {
//        return add(remainderWithBaseAndValue(other.base.intValue(), -1 * other.value.intValue()));
        return remainderWithBaseAndValue(other.base, this.value.subtract(other.value).mod(base));
    }

    public Remainder multiplyByScalar(final BigInteger times) {
        return remainderWithBaseAndValue(base, value.multiply(times).mod(base));
    }

    public Remainder multiply(final Remainder other) {
        assert other.base.equals(this.base) : "Multiplication operation is possible only for arguments with same base value";
        return remainderWithBaseAndValue(base, value.multiply(other.value).mod(base));
    }

    public Remainder power(final BigInteger n) {
        assert n.compareTo(BigInteger.valueOf(-1)) >= 0 : "Cannot compute power with exponent less than -1";
        if(n.compareTo(BigInteger.valueOf(-1)) == 0) {
            return inverse();
        } else {
//            return power(n.subtract(BigInteger.valueOf(1))).multiply(this);
            return remainderWithBaseAndValue(base, value.modPow(n, base));
        }
    }

    public Remainder inverse() {
        BigInteger u = BigInteger.ONE;
        BigInteger w = this.value;
        BigInteger x = BigInteger.ZERO;
        BigInteger z = this.base;
        BigInteger q;
        while (!w.equals(BigInteger.ZERO)) {
            if (w.compareTo(z) == -1) {
                //u ↔ x;   w ↔ z
                Pair<BigInteger, BigInteger> toSwap = Pair.of(u, x);
                u = toSwap.getRight();
                x = toSwap.getLeft();
                toSwap = Pair.of(w, z);
                w = toSwap.getRight();
                z = toSwap.getLeft();
            }
            q = w.divide(z);
            u = u.subtract(q.multiply(x));
            w = w.subtract(q.multiply(z));
        }
        if (!z.equals(BigInteger.ONE)) {
            return null;
        }
        if (x.compareTo(BigInteger.ZERO) == -1) {
            x = x.add(this.base);
        }
        return remainderWithBaseAndValue(base, x);
    }

    public Remainder divide(final Remainder divisor) {
        return multiply(divisor.inverse());
    }

    public boolean isQuadraticResidue() {
        BigInteger pow = base.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
        final Remainder mod = this.power(pow);
        return mod.value.equals(BigInteger.ONE);
    }

    public Remainder squareRoot() throws RemainderOperationException {
        final BigInteger FOUR = BigInteger.valueOf(4);
        final BigInteger THREE = BigInteger.valueOf(3);
        if(isQuadraticResidue() && base.mod(FOUR).equals(THREE)) {
            return power(base.add(BigInteger.ONE).divide(FOUR));
        } else {
            throw new RemainderOperationException("Cannot compute root for point not being a quadratic residue nor satisfying condition: p mode 4 = 3");
        }
    }

    public static Remainder remainderWithBaseAndValue(final int base, final int value) {
        return remainderWithBaseAndValue(BigInteger.valueOf(base), BigInteger.valueOf(value));
    }

    public static Remainder remainderWithBaseAndValue(final BigInteger base, final BigInteger value) {
        if(value.compareTo(BigInteger.ZERO) < 0) {
            return remainderWithBaseAndValue(base, value.add(base));
        } else if(value.compareTo(base) > 0) {
            return remainderWithBaseAndValue(base, value.subtract(base));
        } else {
            return new Remainder(base, value);
        }
    }


    private class RemainderOperationException extends Exception {
        public RemainderOperationException(String s) {
            super(s);
        }
    }
}
