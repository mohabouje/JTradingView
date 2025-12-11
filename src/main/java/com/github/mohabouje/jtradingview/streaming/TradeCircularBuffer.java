package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeCircularBuffer  {
    private final Trade[] buffer;
    private final int capacity;
    private int head = 0;
    private int size = 0;

    public TradeCircularBuffer() {
        this(1024);
    }

    public TradeCircularBuffer(int capacity) {
        this.buffer = new Trade[capacity];
        this.capacity = capacity;
    }

    public synchronized void onTrade(Trade trade) {
        buffer[head] = trade;
        head = (head + 1) % capacity;
        if (size < capacity) {
            size++;
        }
    }


    public synchronized List<Trade> getTrades() {
        List<Trade> trades = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            trades.add(getTradeAt(i));
        }
        return trades;
    }

    public synchronized Trade getTradeAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (head - 1 - index + capacity * 2) % capacity;
        return buffer[actualIndex];
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

