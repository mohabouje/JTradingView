package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;
import java.awt.*;

public class TimeAndSalesHeader extends JPanel {
    private final JLabel lastTradeLabel;
    private final JLabel minPriceLabel;
    private final JLabel maxPriceLabel;
    private final JLabel volumeLabel;
    private final JLabel bidLabel;
    private final JLabel askLabel;
    private Ticker ticker = null;

    public TimeAndSalesHeader() {
        this.lastTradeLabel = new JLabel("--");
        this.minPriceLabel = new JLabel("Min: --");
        this.maxPriceLabel = new JLabel("Max: --");
        this.volumeLabel = new JLabel("Volume: --");
        this.bidLabel = new JLabel("Bid: --");
        this.askLabel = new JLabel("Ask: --");

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setPreferredSize(new Dimension(0, 40));

        lastTradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));        
        minPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maxPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        volumeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bidLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        askLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 2));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.add(minPriceLabel);
        rightPanel.add(maxPriceLabel);
        rightPanel.add(volumeLabel);
        rightPanel.add(bidLabel);
        rightPanel.add(askLabel);
        rightPanel.add(volumeLabel);
        rightPanel.add(new JSeparator(SwingConstants.VERTICAL));
        rightPanel.add(new JLabel("Last:"));
        rightPanel.add(lastTradeLabel);

        add(rightPanel, BorderLayout.EAST);
    }

    public void onTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public void refresh() {
        if (ticker == null) {
            lastTradeLabel.setText("--");
            minPriceLabel.setText("Min: --");
            maxPriceLabel.setText("Max: --");
            volumeLabel.setText("Volume: --");
            bidLabel.setText("Bid: --");
            askLabel.setText("Ask: --");
            return;
        }

        lastTradeLabel.setText(String.format("%.2f", ticker.getLastPrice().doubleValue()));
        minPriceLabel.setText(String.format("Min: %.2f", ticker.getLow().doubleValue()));
        maxPriceLabel.setText(String.format("Max: %.2f", ticker.getHigh().doubleValue()));
        volumeLabel.setText(String.format("Volume: %.2f", ticker.getVolume().doubleValue()));
        bidLabel.setText(String.format("Bid: %.2f", ticker.getBid().doubleValue()));
        askLabel.setText(String.format("Ask: %.2f", ticker.getAsk().doubleValue()));
    }
}

