package com.github.mohabouje.jtradingview;

import com.github.mohabouje.jtradingview.graphical.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.warn("Failed to set system look and feel, using default", e);
            }

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                mainWindow.shutdown();
            }));
        });
    }
}
