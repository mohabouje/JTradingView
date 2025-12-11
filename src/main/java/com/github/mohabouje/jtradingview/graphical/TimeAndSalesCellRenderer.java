package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TimeAndSalesCellRenderer extends DefaultTableCellRenderer {
    private static final Color BUY_COLOR = new Color(200, 240, 200);
    private static final Color SELL_COLOR = new Color(240, 200, 200);
    private static final Color ALTERNATE_ROW_COLOR = new Color(250, 250, 250);
    private static final Color DEFAULT_ROW_COLOR = Color.WHITE;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            Object sideValue = table.getValueAt(row, 1);
            Color rowBackground = DEFAULT_ROW_COLOR;

            if (sideValue != null && !isSelected) {
                if ("BUY".equals(sideValue.toString())) {
                    rowBackground = BUY_COLOR;
                } else if ("SELL".equals(sideValue.toString())) {
                    rowBackground = SELL_COLOR;
                }
            }

            if (rowBackground == DEFAULT_ROW_COLOR && row % 2 == 0) {
                rowBackground = ALTERNATE_ROW_COLOR;
            }

            setBackground(rowBackground);
            setForeground(Color.BLACK);
        }

        if (column == 0) {
            setHorizontalAlignment(SwingConstants.CENTER);
        } else if (column >= 3) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        setBorder(noFocusBorder);
        return this;
    }
}
