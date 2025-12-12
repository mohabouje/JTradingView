package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Instrument;
import com.github.mohabouje.jtradingview.streaming.StreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    private static final int REFRESH_RATE_HZ = 20;
    private static final int REFRESH_INTERVAL_MS = 1000 / REFRESH_RATE_HZ;
    
    private final InstrumentToolbar toolbar;
    private final StreamService streamService;
    private final TimeAndSalesTabbedPane tabbedPane;
    private Timer refreshTimer;
    private final ExecutorService executorService;

    public MainWindow() {
        super("JTradingView - Time and Sales");
        
        this.streamService = new StreamService();
        this.toolbar = new InstrumentToolbar();
        this.tabbedPane = new TimeAndSalesTabbedPane();
        this.executorService = Executors.newFixedThreadPool(1);

        initializeWindow();
        startRefreshTimer();
        
        toolbar.addSubscriptionListener(instrument -> {
            var symbolId = instrument.getInternalSymbolId();
            logger.debug("Subscription request received for {}", symbolId);
            var tab = tabbedPane.getOrCreateTab(symbolId);
            tabbedPane.selectTab(symbolId);
            toolbar.setEnabled(false);
            
            executorService.submit(() -> {
                logger.debug("Background subscription task started for {}", symbolId);
                try {
                    streamService.subscribe(instrument, tab);
                    logger.debug("Subscription successful for {}", symbolId);
                } catch (Exception e) {
                    logger.error("Failed to subscribe to {}", instrument.getInternalSymbolId(), e);
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            this,
                            "Failed to subscribe to " + instrument.getInternalSymbolId() + "\n" + e.getMessage(),
                            "Subscription Error",
                            JOptionPane.ERROR_MESSAGE
                    ));
                } finally {
                    SwingUtilities.invokeLater(() -> toolbar.setEnabled(true));
                }
            });
        });
    }

    private void startRefreshTimer() {
        logger.debug("Starting refresh timer with interval {}ms ({}Hz)", REFRESH_INTERVAL_MS, REFRESH_RATE_HZ);
        refreshTimer = new Timer(REFRESH_INTERVAL_MS, e -> {
            logger.trace("Refresh tick - updating UI components");
            tabbedPane.refresh();
        });
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
        logger.info("Shutting down MainWindow");
        if (refreshTimer != null && refreshTimer.isRunning()) {
            logger.debug("Stopping refresh timer");
            refreshTimer.stop();
        }
        logger.debug("Shutting down executor service");
        executorService.shutdown();
        logger.debug("Shutting down stream service");
        streamService.shutdown();
        logger.debug("MainWindow shutdown complete");
    }
}
