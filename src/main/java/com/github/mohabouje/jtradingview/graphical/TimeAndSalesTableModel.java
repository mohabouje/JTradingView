package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;

public class TimeAndSalesTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Time", "Exchange", "Symbol", "Side", "Price", "Quantity", "Notional"};
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    private final CircularBuffer<Trade> buffer;

    public TimeAndSalesTableModel(CircularBuffer<Trade> buffer) {
        this.buffer = buffer;
    }

    public void refresh() {
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return buffer.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Trade trade = buffer.getAt(rowIndex);
        if (trade == null) {
            return null;
        }

        return switch (columnIndex) {
            case 0 -> trade.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toLocalTime().format(TIME_FORMATTER);
            case 1 -> trade.getExchangeId();
            case 2 -> trade.getInternalSymbolId();
            case 3 -> trade.getSide();
            case 4 -> String.format("%.2f", trade.getPrice());
            case 5 -> String.format("%.8f", trade.getQuantity());
            case 6 -> String.format("%.2f", trade.getNotionalValue());
            default -> null;
        };
    }

    public Trade getTradeAtRow(int rowIndex) {
        return buffer.getAt(rowIndex);
    }
}
