package com.example.Backend.Utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@UtilityClass
public class HandleEmail {
    public String createCode() {
        return IntStream.range(0, 6)
                // Use ThreadLocalRandom to generate random number with multi-threading
                .mapToObj(i -> String.valueOf(ThreadLocalRandom.current().nextInt(0, 10)))
                .reduce("", String::concat);
    }
}
