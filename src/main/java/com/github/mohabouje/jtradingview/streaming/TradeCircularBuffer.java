package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.Trade;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

public class TradeCircularBuffer implements TradeListener {
    private final CircularFifoQueue<Trade> buffer;
    private volatile Throwable lastError = null;

    public TradeCircularBuffer() {
        this(1024);
    }

    public TradeCircularBuffer(int capacity) {
        this.buffer = new CircularFifoQueue<>(capacity);
    }

    @Override
    public synchronized void onTrade(Trade trade) {
        buffer.add(trade);
    }

    @Override
    public synchronized void onError(Throwable throwable) {
        this.lastError = throwable;
    }

    public synchronized List<Trade> getTrades() {
        return new ArrayList<>(buffer);
    }

    public synchronized Trade getTradeAt(int index) {
        if (index < 0 || index >= buffer.size()) {
            return null;
        }
        return buffer.toArray(new Trade[0])[index];
    }

    public synchronized int size() {
        return buffer.size();
    }

    public synchronized boolean isEmpty() {
        return buffer.isEmpty();
    }

    public synchronized boolean isFull() {
        return buffer.isFull();
    }

    public synchronized void clear() {
        buffer.clear();
    }

    public int capacity() {
        return buffer.maxSize();
    }

    public Throwable getLastError() {
        return lastError;
    }
}
