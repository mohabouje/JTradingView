package com.github.mohabouje.protocol;

public class ExternalSymbolId {
    private final String value;
    
    private ExternalSymbolId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("External symbol ID cannot be null or empty");
        }
        this.value = value;
    }
    
    public static ExternalSymbolId of(String value) {
        return new ExternalSymbolId(value);
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
        ExternalSymbolId that = (ExternalSymbolId) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
