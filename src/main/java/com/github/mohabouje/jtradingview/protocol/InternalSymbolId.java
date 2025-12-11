package com.github.mohabouje.jtradingview.protocol;

public class InternalSymbolId {
    private final String value;
    
    private InternalSymbolId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Internal symbol ID cannot be null or empty");
        }
        this.value = value;
    }
    
    public static InternalSymbolId of(String value) {
        return new InternalSymbolId(value);
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
        InternalSymbolId that = (InternalSymbolId) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
