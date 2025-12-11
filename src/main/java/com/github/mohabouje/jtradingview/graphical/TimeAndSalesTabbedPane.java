package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.ExchangeId;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

public class TimeAndSalesTabbedPane extends JTabbedPane {
    private final Map<ExchangeId, TimeAndSalesTab> tabs;

    public TimeAndSalesTabbedPane() {
        this.tabs = new EnumMap<>(ExchangeId.class);
        initializeTabs();
    }

    private void initializeTabs() {
        for (ExchangeId exchange : ExchangeId.values()) {
            TimeAndSalesTab tab = new TimeAndSalesTab();
            tabs.put(exchange, tab);
            addTab(exchange.toString(), tab);
        }
    }

    public TimeAndSalesTab getTab(ExchangeId exchange) {
        return tabs.get(exchange);
    }

    public void refreshAll() {
        tabs.values().forEach(TimeAndSalesTab::refresh);
    }

    public void refresh(ExchangeId exchange) {
        TimeAndSalesTab tab = tabs.get(exchange);
        if (tab != null) {
            tab.refresh();
        }
    }

    public void selectExchange(ExchangeId exchange) {
        setSelectedIndex(exchange.ordinal());
    }

    public ExchangeId getSelectedExchange() {
        return ExchangeId.values()[getSelectedIndex()];
    }
}
