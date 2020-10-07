package buzz.programmers;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

public class FizzBuzzGenerator {
    public List<String> generate(final int numbers) {
        if(numbers < 1) {
            throw new IllegalArgumentException("Number must be larger than or equal to 1.");
        }
        return rangeClosed(1, numbers)
                .mapToObj(n -> {
                    if (0 == n % 15)
                        return "FizzBuzz";
                    if (0 == n % 3)
                        return "Fizz";
                    if (0 == n % 5)
                        return "Buzz";
                    return "" + n;
                }).collect(toList());
    }
}
