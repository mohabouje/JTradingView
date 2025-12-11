package com.github.mohabouje.jtradingview.protocol;


public record InternalSymbolId(String value) {
    public InternalSymbolId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Internal symbol ID cannot be null or empty");
        }
    }
    
    public static InternalSymbolId of(String value) {
        return new InternalSymbolId(value);
    }
}
