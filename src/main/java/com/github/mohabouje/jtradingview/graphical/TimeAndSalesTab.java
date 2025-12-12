package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.streaming.EventListener;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;

public class TimeAndSalesTab extends JPanel implements EventListener {
    private final CircularBuffer<Trade> tradeBuffer;
    private final CircularBuffer<Ticker> tickerBuffer;
    private final TimeAndSalesTable table;
    private final TimeAndSalesHeader header;
    private final TimeAndSalesChart chart;
    private static final int BUFFER_CAPACITY = 1024;

    public TimeAndSalesTab() {
        this.tradeBuffer = new CircularBuffer<>(BUFFER_CAPACITY);
        this.tickerBuffer = new CircularBuffer<>(BUFFER_CAPACITY);
        this.table = new TimeAndSalesTable(tradeBuffer);
        this.header = new TimeAndSalesHeader(tickerBuffer, tradeBuffer);
        this.chart = new TimeAndSalesChart(tradeBuffer, tickerBuffer);

        setLayout(new java.awt.BorderLayout());
        add(header, java.awt.BorderLayout.NORTH);
        add(chart, java.awt.BorderLayout.CENTER);
        add(new JScrollPane(table), java.awt.BorderLayout.SOUTH);
    }

    @Override
    public void onTrade(Trade trade) {
        tradeBuffer.add(trade);
    }

    @Override
    public void onTicker(Ticker ticker) {
        tickerBuffer.add(ticker);
    }

    @Override
    public void onError(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    public void refresh() {
        table.refresh();
        header.refresh();
        chart.refresh();
    }

}

