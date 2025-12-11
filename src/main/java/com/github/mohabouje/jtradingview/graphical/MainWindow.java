package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Instrument;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.streaming.StreamService;
import com.github.mohabouje.jtradingview.streaming.EventListener;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final int REFRESH_RATE_HZ = 20;
    private static final int REFRESH_INTERVAL_MS = 1000 / REFRESH_RATE_HZ;
    
    private final InstrumentToolbar toolbar;
    private final StreamService streamService;
    private final TimeAndSalesTabbedPane tabbedPane;
    private Timer refreshTimer;

    public MainWindow() {
        super("JTradingView - Time and Sales");
        
        this.streamService = new StreamService();
        this.toolbar = new InstrumentToolbar();
        this.tabbedPane = new TimeAndSalesTabbedPane();

        initializeWindow();
        startRefreshTimer();
        
        toolbar.addSubscriptionListener(instrument -> {
            var symbolId = instrument.getInternalSymbolId();
            var tab = tabbedPane.getOrCreateTab(symbolId);
            tabbedPane.selectTab(symbolId);
            toolbar.setEnabled(false);
            new Thread(() -> {
                try {
                    streamService.subscribe(instrument, tab);
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

    private void startRefreshTimer() {
        refreshTimer = new Timer(REFRESH_INTERVAL_MS, e -> tabbedPane.refresh());
        refreshTimer.start();
    }

    public TimeAndSalesTab tabFor(Instrument instrument) {
        return tabbedPane.getTab(instrument.getInternalSymbolId());
    }

    private void initializeWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    public InstrumentToolbar getToolbar() {
        return toolbar;
    }

    public StreamService getStreamService() {
        return streamService;
    }

    public void shutdown() {
        refreshTimer.stop();
        streamService.shutdown();
    }
}
