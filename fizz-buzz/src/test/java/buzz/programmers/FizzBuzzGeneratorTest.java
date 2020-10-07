package buzz.programmers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FizzBuzzGeneratorTest {

    private FizzBuzzGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new FizzBuzzGenerator();
    }

    @Test
    void invalid() {
        assertThatThrownBy(() -> generator.generate(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void single() {
        assertThat(generator.generate(1)).isEqualTo(List.of("1"));
    }

    @Test
    void firstFizz() {
        assertThat(generator.generate(3)).isEqualTo(List.of("1", "2", "Fizz"));
    }

    @Test
    void firstBuzz() {
        assertThat(generator.generate(5)).isEqualTo(List.of("1", "2", "Fizz", "4", "Buzz"));
    }

    @Test
    void firstFizzBuzz() {
        final List<String> expected = List.of("1", "2", "Fizz", "4", "Buzz",
                "Fizz", "7", "8", "Fizz", "Buzz", "11",
                "Fizz", "13", "14", "FizzBuzz");
        assertThat(generator.generate(15)).isEqualTo(expected);
    }

    @Test
    void fizzBuzz100() throws IOException, URISyntaxException {
        final List<String> expected = Files.lines(Paths.get(getClass()
                .getResource("/fizzbuzz100.sample").toURI())).collect(toList());
        assertThat(generator.generate(100)).isEqualTo(expected);
    }
}