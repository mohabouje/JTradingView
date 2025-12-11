package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;
import java.awt.*;

public class TimeAndSalesHeader extends JPanel {
    private static final Color BG_COLOR = new Color(240, 240, 240);
    
    private final JLabel lastTradeLabel;
    private final JLabel minPriceLabel;
    private final JLabel maxPriceLabel;
    private final JLabel volumeLabel;
    private final JLabel bidLabel;
    private final JLabel askLabel;
    private final CircularBuffer<Ticker> buffer;


    public TimeAndSalesHeader(CircularBuffer<Ticker> buffer) {
        this.buffer = buffer;
        this.lastTradeLabel = new JLabel("--");
        this.minPriceLabel = new JLabel("Min: --");
        this.maxPriceLabel = new JLabel("Max: --");
        this.volumeLabel = new JLabel("Volume: --");
        this.bidLabel = new JLabel("Bid: --");
        this.askLabel = new JLabel("Ask: --");

        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(0, 50));

        lastTradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lastTradeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        minPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        minPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        maxPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        maxPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        volumeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bidLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bidLabel.setHorizontalAlignment(SwingConstants.CENTER);
        askLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        askLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        add(minPriceLabel, gbc);
        gbc.gridx = 1;
        add(maxPriceLabel, gbc);
        gbc.gridx = 2;
        add(volumeLabel, gbc);
        gbc.gridx = 3;
        add(bidLabel, gbc);
        gbc.gridx = 4;
        add(askLabel, gbc);
        gbc.gridx = 5;
        add(new JSeparator(SwingConstants.VERTICAL), gbc);
        gbc.gridx = 6;
        add(new JLabel("Last:"), gbc);
        gbc.gridx = 7;
        add(lastTradeLabel, gbc);
    }


    public void refresh() {
        if (buffer.isEmpty()) {
            lastTradeLabel.setText("--");
            minPriceLabel.setText("Min: --");
            maxPriceLabel.setText("Max: --");
            volumeLabel.setText("Volume: --");
            bidLabel.setText("Bid: --");
            askLabel.setText("Ask: --");
            return;
        }

        Ticker ticker = buffer.getAt(0);
        lastTradeLabel.setText(String.format("%.2f", ticker.getLastPrice().doubleValue()));
        minPriceLabel.setText(String.format("Min: %.2f", ticker.getLow().doubleValue()));
        maxPriceLabel.setText(String.format("Max: %.2f", ticker.getHigh().doubleValue()));
        volumeLabel.setText(String.format("Volume: %.2f", ticker.getVolume().doubleValue()));
        bidLabel.setText(String.format("Bid: %.2f", ticker.getBid().doubleValue()));
        askLabel.setText(String.format("Ask: %.2f", ticker.getAsk().doubleValue()));
    }
}

