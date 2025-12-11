package com.github.mohabouje.jtradingview.protocol;

public record Currency(String value) {
    public Currency {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }

    public static Currency of(String value) {
        return new Currency(value);
    }
}
