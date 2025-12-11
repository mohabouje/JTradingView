package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;
import java.awt.*;

public class TimeAndSalesHeader extends JPanel {
    private final JLabel lastTradeLabel;
    private final JLabel minPriceLabel;
    private final JLabel maxPriceLabel;
    private final CircularBuffer<Trade> buffer;

    public TimeAndSalesHeader(CircularBuffer<Trade> buffer) {
        this.buffer = buffer;
        this.lastTradeLabel = new JLabel("--");
        this.minPriceLabel = new JLabel("Min: --");
        this.maxPriceLabel = new JLabel("Max: --");

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setPreferredSize(new Dimension(0, 40));

        lastTradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lastTradeLabel.setForeground(Color.BLACK);
        
        minPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        maxPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 2));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.add(minPriceLabel);
        rightPanel.add(maxPriceLabel);
        rightPanel.add(new JSeparator(SwingConstants.VERTICAL));
        rightPanel.add(new JLabel("Last:"));
        rightPanel.add(lastTradeLabel);

        add(rightPanel, BorderLayout.EAST);
    }

    public void refresh() {
        if (buffer.isEmpty()) {
            lastTradeLabel.setText("--");
            minPriceLabel.setText("Min: --");
            maxPriceLabel.setText("Max: --");
            lastTradeLabel.setForeground(Color.BLACK);
            return;
        }

        var lastTrade = buffer.getAt(0);
        double lastPrice = lastTrade.getPrice().doubleValue();
        lastTradeLabel.setText(String.format("%.2f", lastPrice));
        
        if (lastTrade.getSide() == OrderSide.BUY) {
            lastTradeLabel.setForeground(new Color(0, 150, 0));
        } else {
            lastTradeLabel.setForeground(new Color(200, 0, 0));
        }

        double minPrice = lastPrice;
        double maxPrice = lastPrice;

        for (int i = 0; i < buffer.size(); i++) {
            var trade = buffer.getAt(i);
            if (trade != null) {
                double price = trade.getPrice().doubleValue();
                minPrice = Math.min(minPrice, price);
                maxPrice = Math.max(maxPrice, price);
            }
        }

        minPriceLabel.setText(String.format("Min: %.2f", minPrice));
        maxPriceLabel.setText(String.format("Max: %.2f", maxPrice));
    }
}

