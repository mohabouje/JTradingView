package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.streaming.TradeCircularBuffer;

import javax.swing.*;

public class TimeAndSalesTab extends JScrollPane {
    private final TradeCircularBuffer buffer;
    private final TimeAndSalesTable table;
    private final Timer refreshTimer;

    public TimeAndSalesTab() {
        this.buffer = new TradeCircularBuffer();
        this.table = new TimeAndSalesTable(buffer);
        this.refreshTimer = new Timer(50, e -> refresh());
        
        setViewportView(table);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        refreshTimer.start();
    }

    public TradeCircularBuffer getBuffer() {
        return buffer;
    }

    public TimeAndSalesTable getTable() {
        return table;
    }

    public void refresh() {
        table.refresh();
    }
}
