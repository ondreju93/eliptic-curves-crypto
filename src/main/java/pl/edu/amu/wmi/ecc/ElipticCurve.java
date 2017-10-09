package pl.edu.amu.wmi.ecc;

import lombok.Data;

import java.math.BigInteger;

import static pl.edu.amu.wmi.ecc.CurvePoint.POINT_AT_INFINITY;
import static pl.edu.amu.wmi.ecc.Remainder.remainderWithBaseAndValue;

@Data
public class ElipticCurve {

    static final BigInteger TWO = BigInteger.valueOf(2);
    static final BigInteger THREE = BigInteger.valueOf(3);
    static final BigInteger FOUR = BigInteger.valueOf(4);

    private static final RandomGenerator RANDOM_GENERATOR = new RandomGenerator();

    public static ElipticCurve randomCurve(final BigInteger base) throws ElipticCurveException {
        Remainder delta = remainderWithBaseAndValue(base, BigInteger.ZERO);
        Remainder x = null;
        Remainder y = null;
        Remainder a = null;
        Remainder b = null;
        while(delta.getValue().equals(BigInteger.ZERO)) {
            x = remainderWithBaseAndValue(base, RANDOM_GENERATOR.random(base));
            y = remainderWithBaseAndValue(base, RANDOM_GENERATOR.random(base));
            a = remainderWithBaseAndValue(base, RANDOM_GENERATOR.random(base));
            b = y.power(TWO).subtract(x.power(THREE)).subtract(x.multiply(a));
            delta = a.power(THREE)
                    .multiplyByScalar(FOUR)
                    .add(b.power(TWO)
                            .multiplyByScalar(BigInteger.valueOf(27)));
        }
        return new ElipticCurve(base, a.getValue(), b.getValue());
    }

    private final BigInteger p;
    private final BigInteger A;
    private final BigInteger B;

    public ElipticCurve(final BigInteger p, final BigInteger A, final BigInteger B) throws ElipticCurveException {
        if(!p.isProbablePrime(100)) {
            throw ElipticCurveException.givenFieldBaseIsNotPrimeNumber(p);
        }
        this.p = p;
        this.A = A;
        this.B = B;
    }

    public CurvePoint randomCurvePoint() throws ElipticCurveException {
        if(!p.mod(FOUR).equals(THREE)) {
            System.out.println("Cannot generate random point for Field with p value no satisfying condition: p mod 4 = 3");
            throw ElipticCurveException.givenFieldBaseIsNotCongruentToThreeModuloFour(p);
        }
        CurvePoint randomCurvePoint = null;
        do {
            try {
                final Remainder x = remainderWithBaseAndValue(p, RANDOM_GENERATOR.random(p));
                final Remainder ySquared =
                        x.power(THREE).add(x.multiplyByScalar(A)).add(remainderWithBaseAndValue(p, B));
                if(ySquared.isQuadraticResidue()) {
                    randomCurvePoint = new CurvePoint(x, ySquared.squareRoot());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!isOnCurve(randomCurvePoint));

        if(randomCurvePoint != null) {
            return randomCurvePoint;
        } else {
            throw new ElipticCurveException("Problem occured while generating random point on curve");
        }
    }

    public boolean isOnCurve(CurvePoint point) {

        return point != null &&
                point.getX().getBase().equals(p) && point.getY().getBase().equals(p) &&
                point.getY().power(TWO).equals(
                        point.getX().power(THREE)
                                .add(point.getX().multiplyByScalar(A))
                                .add(remainderWithBaseAndValue(p, B)));
    }

    public Remainder delta() {
        return remainderWithBaseAndValue(p, A).power(THREE).multiplyByScalar(FOUR)
                .add(remainderWithBaseAndValue(p, B).power(TWO).multiplyByScalar(BigInteger.valueOf(27)));
    }


    /**
     * Performs the group operation multiple times
     * Hence the name - in multiplicative algebraic notation this means multiplication
     * @return
     */
    public CurvePoint multiply(final BigInteger n, final CurvePoint point) {
        if(n.equals(BigInteger.ONE)) {
            return point;
        } else {
            final CurvePoint nMinusOneTimesPoint = multiply(n.subtract(BigInteger.ONE), point);
            return !POINT_AT_INFINITY.equals(nMinusOneTimesPoint) ?  add(nMinusOneTimesPoint, point) : point;
        }
    }

    public CurvePoint add(final CurvePoint point1, final CurvePoint point2) {
        if(point1.getX().equals(point2.getX())) {
            return point1.getY().equals(point2.getY()) && !point1.getY().getValue().equals(BigInteger.ZERO) ?
                    twoTimes(point1) : POINT_AT_INFINITY;
        } else {
            return addDistinctPoints(point1, point2);
        }
    }

    private CurvePoint addDistinctPoints(CurvePoint point1, CurvePoint point2) {
        final Remainder slope = point2.getY().subtract(point1.getY()).divide(point2.getX().subtract(point1.getX()));
        final Remainder x = slope.power(TWO).subtract(point1.getX()).subtract(point2.getX());
        final Remainder y = slope.multiply(point1.getX().subtract(x)).subtract(point1.getY());
        return new CurvePoint(x, y);
    }

    private CurvePoint twoTimes(CurvePoint point) {
        final Remainder slope =
                point.getX().power(TWO).multiplyByScalar(THREE).add(remainderWithBaseAndValue(p, A))
                        .divide(point.getY().multiplyByScalar(TWO));
        final Remainder x = slope.power(TWO).subtract(point.getX()).subtract(point.getX()); //TODO ogarnać teorię
        final Remainder y = slope.multiply(point.getX().subtract(x)).subtract(point.getY());
        return new CurvePoint(x, y);
    }

}
