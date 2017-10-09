package pl.edu.amu.wmi.ecc;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Predicate;

public class RandomGenerator {
    private static final Random RANDOM = new Random(System.nanoTime());


    public BigInteger randomPrime(int bits, Predicate<BigInteger> predicate) {
        BigInteger prime;
        do {
            prime = new BigInteger(bits, RANDOM).abs();
        } while(!prime.isProbablePrime(1000) || !predicate.test(prime));

        return prime;
    }

    public BigInteger randomPrime(int bits) {
        return this.randomPrime(bits, t -> true);
    }

    public BigInteger randomPrimeEqualTo3Mod4(int bits) {
        return this.randomPrime(bits, prime -> prime.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)));
    }

    public BigInteger randomPrime() {
        return this.randomPrime(30);
    }

    public BigInteger random(BigInteger max) {
        int maxNumBitLength = max.bitLength();
        BigInteger aRandomBigInt;
        do {
            aRandomBigInt = new BigInteger(maxNumBitLength, RANDOM);
            // compare random number lessthan ginven number
        } while (aRandomBigInt.compareTo(max) > 0);

        return aRandomBigInt;
    }
}
