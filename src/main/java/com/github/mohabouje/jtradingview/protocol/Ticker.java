package com.github.mohabouje.jtradingview.protocol;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;


public class Ticker {
    private final InternalSymbolId internalSymbolId;
    private final ExchangeId exchangeId;
    private final BigDecimal lastPrice;
    private final BigDecimal change;
    private final BigDecimal percentageChange;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal volume;
    private final BigDecimal volumeQuote;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final Instant timestamp;

    private Ticker(Builder builder) {
        this.internalSymbolId = Objects.requireNonNull(builder.internalSymbolId, "internalSymbolId cannot be null");
        this.exchangeId = Objects.requireNonNull(builder.exchangeId, "exchangeId cannot be null");
        this.lastPrice = Objects.requireNonNull(builder.lastPrice, "lastPrice cannot be null");
        this.change = builder.change;
        this.percentageChange = builder.percentageChange;
        this.high = builder.high;
        this.low = builder.low;
        this.volume = builder.volume;
        this.volumeQuote = builder.volumeQuote;
        this.bid = builder.bid;
        this.ask = builder.ask;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
    }

    public InternalSymbolId getInternalSymbolId() {
        return internalSymbolId;
    }

    public ExchangeId getExchangeId() {
        return exchangeId;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public BigDecimal getChange() {
        return change;
    }

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getVolumeQuote() {
        return volumeQuote;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public BigDecimal getSpread() {
        if (bid != null && ask != null) {
            return ask.subtract(bid);
        }
        return null;
    }

    public BigDecimal getSpreadPercent() {
        if (bid != null && ask != null && lastPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getSpread().divide(lastPrice, 10, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "internalSymbolId=" + internalSymbolId +
                ", exchangeId=" + exchangeId +
                ", lastPrice=" + lastPrice +
                ", change=" + change +
                ", percentageChange=" + percentageChange +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", volumeQuote=" + volumeQuote +
                ", bid=" + bid +
                ", ask=" + ask +
                ", timestamp=" + timestamp +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InternalSymbolId internalSymbolId;
        private ExchangeId exchangeId;
        private BigDecimal lastPrice;
        private BigDecimal change;
        private BigDecimal percentageChange;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal volume;
        private BigDecimal volumeQuote;
        private BigDecimal bid;
        private BigDecimal ask;
        private Instant timestamp;

        public Builder internalSymbolId(InternalSymbolId internalSymbolId) {
            this.internalSymbolId = internalSymbolId;
            return this;
        }

        public Builder exchangeId(ExchangeId exchangeId) {
            this.exchangeId = exchangeId;
            return this;
        }

        public Builder lastPrice(BigDecimal lastPrice) {
            this.lastPrice = lastPrice;
            return this;
        }

        public Builder change(BigDecimal change) {
            this.change = change;
            return this;
        }

        public Builder percentageChange(BigDecimal percentageChange) {
            this.percentageChange = percentageChange;
            return this;
        }

        public Builder high(BigDecimal high) {
            this.high = high;
            return this;
        }

        public Builder low(BigDecimal low) {
            this.low = low;
            return this;
        }

        public Builder volume(BigDecimal volume) {
            this.volume = volume;
            return this;
        }

        public Builder volumeQuote(BigDecimal volumeQuote) {
            this.volumeQuote = volumeQuote;
            return this;
        }

        public Builder bid(BigDecimal bid) {
            this.bid = bid;
            return this;
        }

        public Builder ask(BigDecimal ask) {
            this.ask = ask;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Ticker build() {
            return new Ticker(this);
        }
    }

    public static Ticker from(org.knowm.xchange.dto.marketdata.Ticker xchangeTicker, Instrument instrument) {
        return Ticker.builder()
                .internalSymbolId(instrument.getInternalSymbolId())
                .exchangeId(instrument.getExchangeId())
                .lastPrice(xchangeTicker.getLast())
                .percentageChange(xchangeTicker.getPercentageChange())
                .high(xchangeTicker.getHigh())
                .low(xchangeTicker.getLow())
                .volume(xchangeTicker.getVolume())
                .volumeQuote(xchangeTicker.getQuoteVolume())
                .bid(xchangeTicker.getBid())
                .ask(xchangeTicker.getAsk())
                .timestamp(xchangeTicker.getTimestamp() != null ? xchangeTicker.getTimestamp().toInstant() : Instant.now())
                .build();
    }
}
