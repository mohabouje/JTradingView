package com.github.mohabouje.jtradingview.protocol;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InstrumentFactory {
    private static final Pattern SLASH_PATTERN = Pattern.compile("([A-Z]+)/([A-Z]+)");
    private static final Pattern DASH_PATTERN = Pattern.compile("([A-Z]+)-([A-Z]+)");
    public  static final List<String> BASE_CURRENCIES = List.of(
            "BTC", "ETH", "LTC", "XRP", "BCH", "EOS", "XLM", "ADA", "TRX", "BNB",
            "DOT", "LINK", "UNI", "MATIC", "AVAX", "ATOM", "SOL", "DOGE", "SHIB"
    );
    public static final List<String> QUOTE_CURRENCIES = List.of(
            "USD", "USDT", "USDC", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY"
    );

    public static Instrument create(ExchangeId exchangeId, String symbol) {
        String normalized = symbol.toUpperCase();

        Matcher slashMatcher = SLASH_PATTERN.matcher(normalized);
        if (slashMatcher.matches()) {
            return Instrument.builder()
                    .exchangeId(exchangeId)
                    .base(Currency.of(slashMatcher.group(1)))
                    .quote(Currency.of(slashMatcher.group(2)))
                    .build();
        }

        Matcher dashMatcher = DASH_PATTERN.matcher(normalized);
        if (dashMatcher.matches()) {
            return Instrument.builder()
                    .exchangeId(exchangeId)
                    .base(Currency.of(dashMatcher.group(1)))
                    .quote(Currency.of(dashMatcher.group(2)))
                    .build();
        }

        for (String baseCurrency : BASE_CURRENCIES) {
            if (normalized.startsWith(baseCurrency) && normalized.length() > baseCurrency.length()) {
                String quoteCurrency = normalized.substring(baseCurrency.length());
                return Instrument.builder()
                        .exchangeId(exchangeId)
                        .base(Currency.of(baseCurrency))
                        .quote(Currency.of(quoteCurrency))
                        .build();
            }
        }

        throw new IllegalArgumentException("Cannot parse currency pair from symbol: " + symbol);
    }

    public static Instrument create(ExchangeId exchangeId, Currency base, Currency quote) {
        return Instrument.builder()
                .exchangeId(exchangeId)
                .base(base)
                .quote(quote)
                .build();
    }
}

