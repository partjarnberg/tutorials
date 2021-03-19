package buzz.programmers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class BloomFilterTest {

    private BloomFilter bloomFilter;

    @BeforeEach
    void setUp() {
        bloomFilter = BloomFilter.builder()
                .expectedInsertions(1000000)
                .falsePositiveProbability(0.01)
                .build();

        final InputStream in = getClass().getResourceAsStream("/allMaliciousSites.csv");
        new CSVReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                .forEach(line -> bloomFilter.add(line[1].getBytes()));
    }

    @Test
    void getSizeInBytes() {
        assertThat(bloomFilter.sizeInBytes()).isCloseTo(1200000, withPercentage(1));
    }

    @Test
    void getNumberOfHashFunctions() {
        assertThat(bloomFilter.getNumberOfHashFunctions()).isEqualTo(7);
    }

    @Test
    void insertOneAndVerify() {
        final byte[] data = "This is my data".getBytes();
        assertThat(bloomFilter.mightContain(data)).isFalse();

        bloomFilter.add(data);
        assertThat(bloomFilter.mightContain(data)).isTrue();

        assertThat(bloomFilter.mightContain("Some other data".getBytes())).isFalse();
    }

    @Test
    void testForMaliciousInput() throws IOException, CsvException {
        final InputStream in = getClass().getResourceAsStream("/maliciousUserInput.csv");
        assertThat(new CSVReader(new InputStreamReader(in, StandardCharsets.UTF_8)).readAll().stream()
                .filter(line -> bloomFilter.mightContain(line[1].getBytes()))
                .count()).isEqualTo(200);
    }

    @Test
    void testForOkInput() throws IOException, CsvException {
        final InputStream in = getClass().getResourceAsStream("/okUserInput.csv");
        assertThat(new CSVReader(new InputStreamReader(in, StandardCharsets.UTF_8)).readAll().stream()
                .filter(line -> !bloomFilter.mightContain(line[1].getBytes()))
                .count()).isCloseTo(800, offset(10L));
    }
}