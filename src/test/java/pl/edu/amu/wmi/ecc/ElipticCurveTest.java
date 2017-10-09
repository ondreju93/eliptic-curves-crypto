package pl.edu.amu.wmi.ecc;

import org.junit.Test;

import java.math.BigInteger;

import static pl.edu.amu.wmi.ecc.Remainder.remainderWithBaseAndValue;

public class ElipticCurveTest {
    private final RandomGenerator randomGenerator = new RandomGenerator();

    @Test
    public void simpleCurveExample() throws ElipticCurveException {
        final ElipticCurve elipticCurve = new ElipticCurve(BigInteger.valueOf(5),BigInteger.ONE, BigInteger.ONE);
        final CurvePoint point1 =
                new CurvePoint(remainderWithBaseAndValue(5, 4), remainderWithBaseAndValue(5, 2));
        final CurvePoint point2 =
                new CurvePoint(remainderWithBaseAndValue(5, 2),  remainderWithBaseAndValue(5, 1));

        System.out.println("point1 is : " + point1);
        System.out.println("Check if point1 is on curve y^2 = x^3 + x + b : " + elipticCurve.isOnCurve(point1));
        System.out.println("point2 is : " + point2);
        System.out.println("Check if point2 is on curve y^2 = x^3 + x + b : " + elipticCurve.isOnCurve(point2));

        System.out.println("point1 + point2 = " + elipticCurve.add(point1, point2));

        final CurvePoint twoTimesPoint1 = elipticCurve.multiply(ElipticCurve.TWO, point1);
        System.out.println("2 * point1 = " + twoTimesPoint1);

        final CurvePoint twoTimesPoint2 = elipticCurve.multiply(ElipticCurve.TWO, point2);
        System.out.println("2 * point2 = " + twoTimesPoint2);
        final CurvePoint threeTimesPoint2 = elipticCurve.multiply(ElipticCurve.THREE, point2);
        System.out.println("3 * point2 = " + threeTimesPoint2);
        final CurvePoint fourTimesPoint2 = elipticCurve.multiply(ElipticCurve.FOUR, point2);
        System.out.println("4 * point2 = " + fourTimesPoint2);
    }

    @Test
    public void bigPrimeBasedRandomCurve() throws ElipticCurveException {
        final BigInteger bigPrime = randomGenerator.randomPrimeEqualTo3Mod4(256);
        System.out.println("Generated prime is: " + bigPrime + "\tnumber of bits is: " + bigPrime.bitCount());

        final ElipticCurve randomElipticCurve = ElipticCurve.randomCurve(bigPrime);
        System.out.println("Generated random eliptic curve is: " + randomElipticCurve);
        System.out.println("curve's delta factor is: " + randomElipticCurve.delta());

        final CurvePoint point1 = randomElipticCurve.randomCurvePoint();
        System.out.println("point1 is : " + point1);
        final CurvePoint point2 = randomElipticCurve.randomCurvePoint();
        System.out.println("point2 is : " + point2);

        if(point1 != null && point2 != null) {
            final CurvePoint point1PlusPoint2 = randomElipticCurve.add(point1, point2);
            System.out.println("point1 + point2 = " + point1PlusPoint2);
            System.out.println("Check if point1 + point2 sum result is on curve: " + randomElipticCurve.isOnCurve(point1PlusPoint2));

            final BigInteger n = randomGenerator.random(BigInteger.valueOf(5000));
            System.out.println("Genereted random n = " + n);
            final CurvePoint nTimesPoint1 = randomElipticCurve.multiply(n, point1);
            System.out.println(n + " * point1 = " + nTimesPoint1);
            System.out.println("Check if n * point1 is on curve: " + randomElipticCurve.isOnCurve(nTimesPoint1));
        }
    }
}
