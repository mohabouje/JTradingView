package com.github.mohabouje.protocol;

public class TradeId {
    private final String value;
    
    private TradeId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Trade ID cannot be null or empty");
        }
        this.value = value;
    }
    
    public static TradeId of(String value) {
        return new TradeId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeId tradeId = (TradeId) o;
        return value.equals(tradeId.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
