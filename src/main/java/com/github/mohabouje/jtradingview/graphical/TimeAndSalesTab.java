package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.streaming.EventListener;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;

public class TimeAndSalesTab extends JPanel implements EventListener {
    private final CircularBuffer<Trade> buffer;
    private final TimeAndSalesTable table;
    private final TimeAndSalesHeader header;
    private Ticker ticker = null;

    public TimeAndSalesTab() {
        this.buffer = new CircularBuffer<>();
        this.table = new TimeAndSalesTable(buffer);
        this.header = new TimeAndSalesHeader(buffer);

        setLayout(new java.awt.BorderLayout());
        add(header, java.awt.BorderLayout.NORTH);
        add(new JScrollPane(table), java.awt.BorderLayout.CENTER);
    }

    @Override
    public void onTrade(Trade trade) {
        buffer.add(trade);
    }

    @Override
    public void onTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    @Override
    public void onError(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    public CircularBuffer<Trade> getBuffer() {
        return buffer;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public TimeAndSalesTable getTable() {
        return table;
    }

    public void refresh() {
        header.refresh();
        table.refresh();
    }
}

