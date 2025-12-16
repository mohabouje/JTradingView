package com.github.mohabouje.jtradingview.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CircularBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final AtomicInteger head = new AtomicInteger(0);
    private final AtomicInteger size = new AtomicInteger(0);

    public CircularBuffer(int capacity) {
        this.buffer = new Object[capacity];
        this.capacity = capacity;
    }

    public void add(T element) {
        int currentHead = head.get();
        buffer[currentHead] = element;
        head.set((currentHead + 1) % capacity);
        
        if (size.get() < capacity) {
            size.incrementAndGet();
        }
    }

    public List<T> getAll() {
        int currentHead = head.get();
        int currentSize = size.get();
        
        List<T> items = new ArrayList<>(currentSize);
        int idx = (currentHead - currentSize + capacity) % capacity;
        for (int i = 0; i < currentSize; i++) {
            items.add((T) buffer[idx]);
            idx = (idx + 1) % capacity;
        }
        return items;
    }

    public Stream<T> stream() {
        return getAll().stream();
    }

    public void forEach(Consumer<T> action) {
        int currentHead = head.get();
        int currentSize = size.get();
        
        int idx = (currentHead - currentSize + capacity) % capacity;
        for (int i = 0; i < currentSize; i++) {
            action.accept((T) buffer[idx]);
            idx = (idx + 1) % capacity;
        }
    }

    @SuppressWarnings("unchecked")
    public T getAt(int index) {
        int currentSize = size.get();
        if (index < 0 || index >= currentSize) {
            return null;
        }
        int currentHead = head.get();
        int actualIndex = (currentHead - 1 - index + capacity * 2) % capacity;
        return (T) buffer[actualIndex];
    }

    public int size() {
        return size.get();
    }

    public boolean isEmpty() {
        return size.get() == 0;
    }

    public boolean isFull() {
        return size.get() == capacity;
    }

    public void clear() {
        head.set(0);
        size.set(0);
        for (int i = 0; i < capacity; i++) {
            buffer[i] = null;
        }
    }

    public int capacity() {
        return capacity;
    }
}

