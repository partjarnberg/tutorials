package buzz.programmers;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static org.apache.commons.codec.digest.MurmurHash3.hash32x86;

public class BloomFilter {
    private static final long THE_ANSWER_TO_THE_ULTIMATE_QUESTION_OF_LIFE_THE_UNIVERSE_AND_EVERYTHING = 42;
    private final BitSet bitSet;
    private final Set<Integer> seeds = new HashSet<>();

    private BloomFilter(final int numberOfBits, final int numberOfHashFunctions) {
        bitSet = new BitSet(numberOfBits);
        new Random(THE_ANSWER_TO_THE_ULTIMATE_QUESTION_OF_LIFE_THE_UNIVERSE_AND_EVERYTHING)
                .ints(numberOfHashFunctions).forEach(seeds::add);
    }

    public void add(final byte[] bytes) {
        seeds.forEach(seed ->
                bitSet.set((int) (toUnsignedLong(hash32x86(bytes, 0, bytes.length, seed)) % bitSet.size()))
        );
    }

    public boolean mightContain(final byte[] bytes) {
        return seeds.stream().map(seed ->
                (int) (toUnsignedLong(hash32x86(bytes, 0, bytes.length, seed)) % bitSet.size())
        ).allMatch(bitSet::get);
    }

    public int getNumberOfHashFunctions() {
        return seeds.size();
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
