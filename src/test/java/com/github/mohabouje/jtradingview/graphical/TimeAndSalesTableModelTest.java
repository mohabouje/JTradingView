package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.*;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeAndSalesTableModelTest {

    private CircularBuffer<Trade> buffer;
    private TimeAndSalesTableModel model;

    @BeforeEach
    void setUp() {
        buffer = new CircularBuffer<>(10);
        model = new TimeAndSalesTableModel(buffer);
    }

    @Test
    void emptyBufferReturnsZeroRows() {
        assertThat(model.getRowCount()).isZero();
    }

    @Test
    void rowCountMatchesBufferSize() {
        buffer.add(createTrade("BTC/USD", OrderSide.BUY, 50000.0, 0.5));
        buffer.add(createTrade("ETH/USD", OrderSide.SELL, 3000.0, 1.2));
        model.refresh();
        assertThat(model.getRowCount()).isEqualTo(2);
    }

    @Test
    void columnCountIsCorrect() {
        assertThat(model.getColumnCount()).isEqualTo(7);
    }

    @Test
    void getValueAtReturnsCorrectData() {
        Trade trade = createTrade("BTC/USD", OrderSide.BUY, 50000.0, 0.5);
        buffer.add(trade);
        model.refresh();

        assertThat(model.getValueAt(0, 1)).isEqualTo(ExchangeId.KRAKEN); // Exchange
        assertThat(model.getValueAt(0, 2)).asString().contains("BTC/USD"); // Symbol
        assertThat(model.getValueAt(0, 3)).isEqualTo(OrderSide.BUY); // Side
        assertThat(model.getValueAt(0, 4)).asString().contains("50000"); // Price
        assertThat(model.getValueAt(0, 5)).asString().contains("0.5"); // Quantity
    }

    @Test
    void getTradeAtRowReturnsCorrectTrade() {
        Trade trade1 = createTrade("BTC/USD", OrderSide.BUY, 50000.0, 0.5);
        Trade trade2 = createTrade("ETH/USD", OrderSide.SELL, 3000.0, 1.0);
        buffer.add(trade1);
        buffer.add(trade2);
        
        assertThat(model.getTradeAtRow(0)).isEqualTo(trade2); // Most recent first
        assertThat(model.getTradeAtRow(1)).isEqualTo(trade1);
    }

    private Trade createTrade(String symbol, OrderSide side, double price, double quantity) {
        return Trade.builder()
                .internalSymbolId(InternalSymbolId.of(symbol))
                .exchangeId(ExchangeId.KRAKEN)
                .side(side)
                .price(BigDecimal.valueOf(price))
                .quantity(BigDecimal.valueOf(quantity))
                .timestamp(Instant.now())
                .tradeId(TradeId.of("test-" + System.nanoTime()))
                .build();
    }
}
