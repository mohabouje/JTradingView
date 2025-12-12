package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;

import javax.swing.*;
import java.awt.*;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class TimeAndSalesHeader extends JPanel {
    private static final Color BG_COLOR = new Color(240, 240, 240); 
    private final JLabel lastTradeLabel = new JLabel("--");
    private final JLabel bidLabel = new JLabel("Bid: --");
    private final JLabel askLabel = new JLabel("Ask: --");
    private final JLabel buyCountLabel = new JLabel("Count: --");
    private final JLabel buyAvgPriceLabel = new JLabel("Avg: --");
    private final JLabel buyMinPriceLabel = new JLabel("Min: --");
    private final JLabel buyMaxPriceLabel = new JLabel("Max: --");
    private final JLabel buyVolLabel = new JLabel("Vol: --");
    private final JLabel sellCountLabel = new JLabel("Count: --");
    private final JLabel sellAvgPriceLabel = new JLabel("Avg: --");
    private final JLabel sellMinPriceLabel = new JLabel("Min: --");
    private final JLabel sellMaxPriceLabel = new JLabel("Max: --");
    private final JLabel sellVolLabel = new JLabel("Vol: --");
    private final CircularBuffer<Ticker> tickerBuffer;
    private final CircularBuffer<Trade> tradeBuffer;

    public TimeAndSalesHeader(CircularBuffer<Ticker> tickerBuffer, CircularBuffer<Trade> tradeBuffer) {
        this.tickerBuffer = tickerBuffer;
        this.tradeBuffer = tradeBuffer;

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(0, 100));

        styleTickerLabels(lastTradeLabel);
        styleTickerLabels(bidLabel);
        styleTickerLabels(askLabel);
        styleTradeLabels(new Color(0, 128, 0), buyCountLabel, buyAvgPriceLabel, buyMinPriceLabel, buyMaxPriceLabel, buyVolLabel);
        styleTradeLabels(new Color(220, 20, 60), sellCountLabel, sellAvgPriceLabel, sellMinPriceLabel, sellMaxPriceLabel, sellVolLabel);

        JPanel tickerPanel = new JPanel();
        tickerPanel.setOpaque(false);
        tickerPanel.setLayout(new BoxLayout(tickerPanel, BoxLayout.X_AXIS));
        tickerPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tickerPanel.setPreferredSize(new Dimension(0, 40));
        tickerPanel.add(Box.createHorizontalGlue());
        tickerPanel.add(bidLabel);
        tickerPanel.add(Box.createHorizontalStrut(12));
        tickerPanel.add(askLabel);
        tickerPanel.add(Box.createHorizontalStrut(12));
        tickerPanel.add(lastTradeLabel);
        tickerPanel.add(Box.createHorizontalGlue());
        add(tickerPanel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        statsPanel.setPreferredSize(new Dimension(0, 40));
        statsPanel.add(Box.createHorizontalGlue());
        statsPanel.add(buyCountLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(buyAvgPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(buyMinPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(buyMaxPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(buyVolLabel);
        statsPanel.add(Box.createHorizontalStrut(24));
        statsPanel.add(sellCountLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(sellAvgPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(sellMinPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(sellMaxPriceLabel);
        statsPanel.add(Box.createHorizontalStrut(8));
        statsPanel.add(sellVolLabel);
        statsPanel.add(Box.createHorizontalGlue());
        add(statsPanel, BorderLayout.CENTER);
    }


    public void refresh() {
        refreshTicker();
        refreshTrade();
    }

    private void refreshTicker() {
        if (tickerBuffer.isEmpty()) {
            return;
        }
        
        Ticker ticker = tickerBuffer.getAt(0);
        bidLabel.setText(String.format("Bid: %.2f", ticker.getBid().doubleValue()));
        askLabel.setText(String.format("Ask: %.2f", ticker.getAsk().doubleValue()));
        lastTradeLabel.setText(String.format("Last: %.2f", ticker.getLastPrice().doubleValue()));
    }

    private void refreshTrade() {
        if (tradeBuffer.isEmpty()) {
            return;
        }
        
        updateTradeStats(OrderSide.BUY, buyCountLabel, buyAvgPriceLabel, buyMinPriceLabel, buyMaxPriceLabel, buyVolLabel);
        updateTradeStats(OrderSide.SELL, sellCountLabel, sellAvgPriceLabel, sellMinPriceLabel, sellMaxPriceLabel, sellVolLabel);
    }

    private void updateTradeStats(OrderSide side, JLabel countLabel, JLabel avgPriceLabel, JLabel minPriceLabel, JLabel maxPriceLabel, JLabel volLabel) {
        TradeStats stats = computeTradeStatistics(side);
        countLabel.setText(String.format("Count: %d", stats.count));
        avgPriceLabel.setText(String.format("Avg: %.2f", stats.averagePrice));
        minPriceLabel.setText(String.format("Min: %.2f", stats.minPrice));
        maxPriceLabel.setText(String.format("Max: %.2f", stats.maxPrice));
        volLabel.setText(String.format("Vol: %.2f", stats.totalQty));
    }
    
    private void styleTradeLabels(Color color, JLabel... labels) {
        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setForeground(color);
        }
    }
    
    private void styleTickerLabels(JLabel label) {
        label.setFont(new Font("Segoe UI", 12, Font.BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private TradeStats computeTradeStatistics(OrderSide side) {
        return tradeBuffer.stream()
                .filter(trade -> trade.getSide() == side)
                .collect(Collectors.teeing(
                        Collectors.summarizingDouble(trade -> trade.getPrice().doubleValue()),
                        Collectors.summingDouble(trade -> trade.getQuantity().doubleValue()),
                        TradeStats::new
                ));
    }

    private static class TradeStats {
        private final long count;
        private final double averagePrice;
        private final double minPrice;
        private final double maxPrice;
        private final double totalQty;

        TradeStats(DoubleSummaryStatistics priceStats, double totalQty) {
            this.count = priceStats.getCount();
            this.averagePrice = priceStats.getAverage();
            this.minPrice = priceStats.getMin();
            this.maxPrice = priceStats.getMax();
            this.totalQty = totalQty;
        }
    }
}


