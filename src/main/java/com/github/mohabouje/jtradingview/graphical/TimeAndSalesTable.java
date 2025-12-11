package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TimeAndSalesTable extends JTable {
    private final TimeAndSalesTableModel model;
    private final CircularBuffer<Trade> buffer;

    public TimeAndSalesTable(CircularBuffer<Trade> buffer) {
        this.buffer = buffer;
        this.model = new TimeAndSalesTableModel(buffer);
        
        initializeTable();
        initializeStyling();
    }

    private void initializeTable() {
        setModel(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(Object.class, new TimeAndSalesCellRenderer());
        
        setRowHeight(24);
        setShowGrid(true);
        setGridColor(new Color(230, 230, 230));
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void initializeStyling() {
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(45, 45, 48));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 28));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(45, 45, 48));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setDefaultRenderer(headerRenderer);
        
        setBackground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 11));
    }

    public void refresh() {
        model.refresh();
    }

    public TimeAndSalesTableModel getTableModel() {
        return model;
    }

    public CircularBuffer<Trade> getBuffer() {
        return buffer;
    }
}
