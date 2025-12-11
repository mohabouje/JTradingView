package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TimeAndSalesCellRenderer extends DefaultTableCellRenderer {
    private static final int ALPHA = 80;
    private static final Color BUY_COLOR = new Color(0, 160, 0, ALPHA);
    private static final Color SELL_COLOR = new Color(200, 0, 0, ALPHA);
    private static final Color DEFAULT_ROW_COLOR = Color.WHITE;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            Object sideValue = table.getValueAt(row, 3); // side column
            Color rowBackground = DEFAULT_ROW_COLOR;

            if (sideValue != null) {
                if ("BUY".equals(sideValue.toString())) {
                    rowBackground = BUY_COLOR;
                } else if ("SELL".equals(sideValue.toString())) {
                    rowBackground = SELL_COLOR;
                }
            }

            setBackground(rowBackground);
            setForeground(rowBackground == DEFAULT_ROW_COLOR ? Color.BLACK : Color.WHITE);
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        if (column == 4 || column == 5) {
            setFont(getFont().deriveFont(Font.BOLD));
        } else {
            setFont(getFont().deriveFont(Font.PLAIN));
        }

        setBorder(noFocusBorder);
        return this;
    }
}
