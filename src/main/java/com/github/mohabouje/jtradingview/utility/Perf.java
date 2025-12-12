package com.github.mohabouje.jtradingview.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class Perf {
    private static final Logger logger = LoggerFactory.getLogger(Perf.class);

    private Perf() {}

    public static void measure(String name, Runnable action) {
        long start = System.nanoTime();
        try {
            action.run();
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000L;
            logger.trace("{} took {} us", name, durationMs);
        }
    }

    public static <T> T measure(String name, Supplier<T> supplier) {
        long start = System.nanoTime();
        try {
            return supplier.get();
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000L;
            logger.trace("{} took {} us", name, durationMs);
        }
    }
}
