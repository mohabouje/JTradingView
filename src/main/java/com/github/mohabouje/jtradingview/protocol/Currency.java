package com.github.mohabouje.jtradingview.protocol;

import java.util.Objects;

public class Currency {
    private final String value;

    private Currency(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.value = value.toUpperCase();
    }

    public static Currency of(String value) {
        return new Currency(value);
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
        Currency currency = (Currency) o;
        return value.equals(currency.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
