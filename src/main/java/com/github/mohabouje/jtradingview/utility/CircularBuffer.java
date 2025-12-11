package com.github.mohabouje.jtradingview.utility;

import java.util.ArrayList;
import java.util.List;


public class CircularBuffer<T> {    
    private final Object[] buffer;
    private final int capacity;
    private int head = 0;
    private int size = 0;

    public CircularBuffer(int capacity) {
        this.buffer = new Object[capacity];
        this.capacity = capacity;
    }

    public synchronized void add(T element) {
        buffer[head] = element;
        head = (head + 1) % capacity;
        if (size < capacity) {
            size++;
        }
    }

    public synchronized List<T> getAll() {
        List<T> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(getAt(i));
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public synchronized T getAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (head - 1 - index + capacity * 2) % capacity;
        return (T) buffer[actualIndex];
    }

    public synchronized int size() {
        return size;
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean isFull() {
        return size == capacity;
    }

    public synchronized void clear() {
        head = 0;
        size = 0;
        for (int i = 0; i < capacity; i++) {
            buffer[i] = null;
        }
    }

    public int capacity() {
        return capacity;
    }
}
