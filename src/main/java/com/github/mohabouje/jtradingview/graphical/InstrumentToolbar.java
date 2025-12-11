package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.Currency;
import com.github.mohabouje.jtradingview.protocol.ExchangeId;
import com.github.mohabouje.jtradingview.protocol.Instrument;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InstrumentToolbar extends JToolBar {
    private final JTextField baseField;
    private final JTextField quoteField;
    private final JComboBox<ExchangeId> exchangeComboBox;
    private final JButton subscribeButton;
    private final List<Consumer<Instrument>> listeners;

    public InstrumentToolbar() {
        this.baseField = new JTextField(8);
        this.quoteField = new JTextField(8);
        this.exchangeComboBox = new JComboBox<>(ExchangeId.values());
        this.subscribeButton = new JButton("Subscribe");
        this.listeners = new ArrayList<>();

        initializeToolbar();
        attachListeners();
    }

    private void initializeToolbar() {
        setFloatable(false);
        setRollover(true);

        add(new JLabel("Base: "));
        add(baseField);
        add(Box.createHorizontalStrut(5));
        
        add(new JLabel("Quote: "));
        add(quoteField);
        add(Box.createHorizontalStrut(10));
        
        add(new JLabel("Exchange: "));
        add(exchangeComboBox);
        add(Box.createHorizontalStrut(10));
        
        add(subscribeButton);
        
        baseField.setMaximumSize(new Dimension(100, 30));
        quoteField.setMaximumSize(new Dimension(100, 30));
        exchangeComboBox.setMaximumSize(new Dimension(120, 30));
    }

    private void attachListeners() {
        subscribeButton.addActionListener(e -> handleSubscribe());
        baseField.addActionListener(e -> handleSubscribe());
        quoteField.addActionListener(e -> handleSubscribe());
    }

    private void handleSubscribe() {
        String base = baseField.getText().trim();
        String quote = quoteField.getText().trim();
        
        if (base.isEmpty() || quote.isEmpty()) {
            return;
        }

        ExchangeId exchange = (ExchangeId) exchangeComboBox.getSelectedItem();
        if (exchange == null) {
            return;
        }
        Instrument instrument = Instrument.builder()
                                                    .exchangeId(exchange)
                                                    .base(Currency.of(base))
                                                    .quote(Currency.of(quote))
                                                    .build();

        notifyListeners(instrument);
        baseField.setText("");
        quoteField.setText("");
    }

    public void addSubscriptionListener(Consumer<Instrument> listener) {
        listeners.add(listener);
    }

    public void removeSubscriptionListener(Consumer<Instrument> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(Instrument instrument) {
        listeners.forEach(listener -> listener.accept(instrument));
    }

    public void setBase(String base) {
        baseField.setText(base);
    }

    public void setQuote(String quote) {
        quoteField.setText(quote);
    }

    public void setExchange(ExchangeId exchange) {
        exchangeComboBox.setSelectedItem(exchange);
    }
}
