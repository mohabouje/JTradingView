package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.streaming.TradeCircularBuffer;

import javax.swing.*;

public class TimeAndSalesTab extends JScrollPane {
    private final TradeCircularBuffer buffer;
    private final TimeAndSalesTable table;

    public TimeAndSalesTab() {
        this.buffer = new TradeCircularBuffer();
        this.table = new TimeAndSalesTable(buffer);
        
        setViewportView(table);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
