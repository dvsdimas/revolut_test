package com.revolut.dmylnev;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class Counter {

    private Counter() { throw new IllegalStateException(); }

    public static @Nonnull Map<Character, Integer> countLetters(@Nonnull final String str) {

        Objects.requireNonNull(str);

        @Nonnull final Map<Character, Integer> map = new HashMap<>();

        for(Character c : str.toCharArray()) {

            final Integer i = map.computeIfAbsent(c, (cc) -> 0);

            map.put(c, i + 1);
        }

        return map;
    }

    public static @Nonnull Map<Character, Integer> countLetters(@Nonnull final Stream<Character> in) {

        @Nonnull final Map<Character, Integer> map = new HashMap<>();

        in.forEach((c) -> {
            final Integer i = map.computeIfAbsent(c, (cc) -> 0);

            map.put(c, i + 1);

        });

        return map;
    }

}
