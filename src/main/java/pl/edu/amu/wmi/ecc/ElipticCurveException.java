package pl.edu.amu.wmi.ecc;

import java.math.BigInteger;

public class ElipticCurveException extends Exception {

    public static ElipticCurveException givenFieldBaseIsNotPrimeNumber(final BigInteger complexNumber) {
        return new ElipticCurveException("Given number is not a prime: " + complexNumber);
    }

    public static ElipticCurveException givenFieldBaseIsNotCongruentToThreeModuloFour(final BigInteger fieldBase) {
        return new ElipticCurveException("Given field base is not congruent to three modulo four:" + fieldBase);
    }

    ElipticCurveException(String message) {
        super(message);
    }
}
