package com.github.mohabouje.jtradingview.protocol;


public record TradeId(String value) {
    public TradeId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Trade ID cannot be null or empty");
        }
    }
    
    public static TradeId of(String value) {
        return new TradeId(value);
    }
}
