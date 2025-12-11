package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Instrument;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.streaming.TradeStreamService;
import com.github.mohabouje.jtradingview.streaming.TradeCircularBuffer;
import com.github.mohabouje.jtradingview.streaming.TradeListener;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final InstrumentToolbar toolbar;
    private final TradeStreamService tradeStreamService;
    private final TradeCircularBuffer buffer;
    private final TimeAndSalesTable table;

    public MainWindow() {
        super("JTradingView - Time and Sales");
        
        this.tradeStreamService = new TradeStreamService();
        this.toolbar = new InstrumentToolbar();
        this.buffer = new TradeCircularBuffer();
        this.table = new TimeAndSalesTable(buffer);

        TradeListener listener = new TradeListener() {
            @Override
            public void onTrade(Trade trade) {
                buffer.onTrade(trade);
                SwingUtilities.invokeLater(table::refresh);
            }

            @Override
            public void onError(Throwable throwable) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        MainWindow.this,
                        "Error receiving trade data:\n" + throwable.getMessage(),
                        "Trade Stream Error",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        };

        initializeWindow();
        toolbar.addSubscriptionListener(instrument -> {
            toolbar.setEnabled(false);
            new Thread(() -> {
                try {
                    tradeStreamService.subscribe(instrument, listener);
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            this,
                            "Failed to subscribe to " + instrument.getInternalSymbolId() + "\n" + e.getMessage(),
                            "Subscription Error",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
                SwingUtilities.invokeLater(() -> {
                    toolbar.setEnabled(true);
                });
            }, "Subscription-Thread").start();
        });
    }

    private void initializeWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    public InstrumentToolbar getToolbar() {
        return toolbar;
    }

    public TradeStreamService getTradeStreamService() {
        return tradeStreamService;
    }

    public void shutdown() {
        tradeStreamService.shutdown();
    }
}
