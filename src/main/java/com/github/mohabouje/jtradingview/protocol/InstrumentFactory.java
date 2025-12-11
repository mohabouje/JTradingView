package com.github.mohabouje.jtradingview.protocol;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InstrumentFactory {
    private static final Pattern SLASH_PATTERN = Pattern.compile("([A-Z]+)/([A-Z]+)");
    private static final Pattern DASH_PATTERN = Pattern.compile("([A-Z]+)-([A-Z]+)");
    private static final Set<String> KNOWN_BASE_CURRENCIES = Set.of(
            "BTC", "ETH", "LTC", "XRP", "BCH", "EOS", "XLM", "ADA", "TRX", "BNB",
            "DOT", "LINK", "UNI", "MATIC", "AVAX", "ATOM", "SOL", "DOGE", "SHIB"
    );

    public static Instrument binance(String symbol) {
        return create(ExchangeId.BINANCE, symbol);
    }

    public static Instrument bybit(String symbol) {
        return create(ExchangeId.BYBIT, symbol);
    }

    public static Instrument kraken(String symbol) {
        return create(ExchangeId.KRAKEN, symbol);
    }

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

        for (String baseCurrency : KNOWN_BASE_CURRENCIES) {
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

