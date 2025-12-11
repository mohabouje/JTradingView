package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Instrument;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.streaming.StreamService;
import com.github.mohabouje.jtradingview.streaming.EventListener;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final InstrumentToolbar toolbar;
    private final StreamService streamService;
    private final TimeAndSalesTabbedPane tabbedPane;

    public MainWindow() {
        super("JTradingView - Time and Sales");
        
        this.streamService = new StreamService();
        this.toolbar = new InstrumentToolbar();
        this.tabbedPane = new TimeAndSalesTabbedPane();

        initializeWindow();
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

    public TimeAndSalesTab tabFor(Instrument instrument) {
        return tabbedPane.getTab(instrument.getInternalSymbolId());
    }

    private void initializeWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(toolbar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

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
        streamService.shutdown();
    }
}
