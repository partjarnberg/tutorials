package buzz.programmers;

import java.util.BitSet;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.commons.codec.digest.MurmurHash3.hash32x86;

public class BloomFilter {
    private final BitSet bitSet;
    private final int numberOfHashFunctions;

    private BloomFilter(final int numberOfBits, final int numberOfHashFunctions) {
        bitSet = new BitSet(numberOfBits);
        this.numberOfHashFunctions = numberOfHashFunctions;
    }

    public void add(final byte[] bytes) {
        rangeClosed(1, numberOfHashFunctions).forEach(seed ->
                bitSet.set((int) (toUnsignedLong(hash32x86(bytes, 0, bytes.length, seed)) % bitSet.size()))
        );
    }

    public boolean mightContain(final byte[] bytes) {
        return rangeClosed(1, numberOfHashFunctions).map(seed ->
                (int) (toUnsignedLong(hash32x86(bytes, 0, bytes.length, seed)) % bitSet.size())
        ).allMatch(bitSet::get);
    }

    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }

    public int sizeInBytes() {
        return bitSet.size() / 8;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Double falsePositiveProbability;
        private Integer expectedInsertions;

        private Builder() {}

        public Builder falsePositiveProbability(final double falsePositiveProbability) {
            this.falsePositiveProbability = falsePositiveProbability;
            return this;
        }

        public Builder expectedInsertions(final int expectedInsertions) {
            this.expectedInsertions = expectedInsertions;
            return this;
        }

        public BloomFilter build() {
            checkNotNull(expectedInsertions);
            checkNotNull(falsePositiveProbability);

            final int numberOfBits = (int) (- ((double) expectedInsertions) * log(falsePositiveProbability) / pow(log(2), 2) );
            final int numberOfHashFunctions = (int) Math.ceil(-log(falsePositiveProbability) / log(2));
            return new BloomFilter(numberOfBits, numberOfHashFunctions);
        }
    }
}
