package com.github.mohabouje.jtradingview;

import com.github.mohabouje.jtradingview.graphical.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                mainWindow.shutdown();
            }));
        });
    }
}
