package com.github.mohabouje.jtradingview.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class CircularBufferTest {

    @Test
    void addAndGetAtBehavesAsRing() {
        CircularBuffer<Integer> buf = new CircularBuffer<>(5);
        for (int i = 1; i <= 7; i++) {
            buf.add(i);
        }
        assertThat(buf.size()).isEqualTo(5);
        assertThat(buf.getAt(0)).isEqualTo(7);
        assertThat(buf.getAt(1)).isEqualTo(6);
        assertThat(buf.getAt(2)).isEqualTo(5);
        assertThat(buf.getAt(3)).isEqualTo(4);
        assertThat(buf.getAt(4)).isEqualTo(3);
        assertThat(buf.getAt(5)).isNull();
    }

    @Test
    void getAllReturnsInChronologicalOrder() {
        CircularBuffer<String> buf = new CircularBuffer<>(3);
        buf.add("A");
        buf.add("B");
        buf.add("C");
        List<String> all = buf.getAll();
        assertThat(all).containsExactly("A", "B", "C");
    }

}
