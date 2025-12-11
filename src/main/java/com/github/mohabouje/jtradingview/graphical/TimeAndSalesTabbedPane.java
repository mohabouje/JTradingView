package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.InternalSymbolId;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TimeAndSalesTabbedPane extends JTabbedPane {
    private final Map<InternalSymbolId, TimeAndSalesTab> tabs = new HashMap<>();

    public TimeAndSalesTab getOrCreateTab(InternalSymbolId symbolId) {
        return tabs.computeIfAbsent(symbolId, id -> {
            TimeAndSalesTab tab = new TimeAndSalesTab();
            addTab(symbolId.getValue(), tab);
            return tab;
        });
    }

    public TimeAndSalesTab getTab(InternalSymbolId symbolId) {
        return tabs.get(symbolId);
    }

    public void selectTab(InternalSymbolId symbolId) {
        TimeAndSalesTab tab = tabs.get(symbolId);
        if (tab != null) {
            setSelectedComponent(tab);
        }
    }
}

