package com.github.mohabouje.jtradingview.protocol;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;


public class Ticker {
    private final InternalSymbolId mInternalSymbolId;
    private final ExchangeId mExchangeId;
    private final BigDecimal mLastPrice;
    private final BigDecimal mChange;
    private final BigDecimal mPercentageChange;
    private final BigDecimal mHigh;
    private final BigDecimal mLow;
    private final BigDecimal mVolume;
    private final BigDecimal mVolumeQuote;
    private final BigDecimal mBid;
    private final BigDecimal mAsk;
    private final Instant mTimestamp;

    private Ticker(Builder builder) {
        this.mInternalSymbolId = Objects.requireNonNull(builder.mInternalSymbolId, "internalSymbolId cannot be null");
        this.mExchangeId = Objects.requireNonNull(builder.mExchangeId, "exchangeId cannot be null");
        this.mLastPrice = Objects.requireNonNull(builder.mLastPrice, "lastPrice cannot be null");
        this.mChange = builder.mChange;
        this.mPercentageChange = builder.mPercentageChange;
        this.mHigh = builder.mHigh;
        this.mLow = builder.mLow;
        this.mVolume = builder.mVolume;
        this.mVolumeQuote = builder.mVolumeQuote;
        this.mBid = builder.mBid;
        this.mAsk = builder.mAsk;
        this.mTimestamp = builder.mTimestamp != null ? builder.mTimestamp : Instant.now();
    }

    public InternalSymbolId getInternalSymbolId() {
        return mInternalSymbolId;
    }

    public ExchangeId getExchangeId() {
        return mExchangeId;
    }

    public BigDecimal getLastPrice() {
        return mLastPrice;
    }

    public BigDecimal getChange() {
        return mChange;
    }

    public BigDecimal getPercentageChange() {
        return mPercentageChange;
    }

    public BigDecimal getHigh() {
        return mHigh;
    }

    public BigDecimal getLow() {
        return mLow;
    }

    public BigDecimal getVolume() {
        return mVolume;
    }

    public BigDecimal getVolumeQuote() {
        return mVolumeQuote;
    }

    public BigDecimal getBid() {
        return mBid;
    }

    public BigDecimal getAsk() {
        return mAsk;
    }

    public Instant getTimestamp() {
        return mTimestamp;
    }

    public BigDecimal getSpread() {
        if (mBid != null && mAsk != null) {
            return mAsk.subtract(mBid);
        }
        return null;
    }

    public BigDecimal getSpreadPercent() {
        if (mBid != null && mAsk != null && mLastPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getSpread().divide(mLastPrice, 10, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "internalSymbolId=" + mInternalSymbolId +
                ", exchangeId=" + mExchangeId +
                ", lastPrice=" + mLastPrice +
                ", change=" + mChange +
                ", percentageChange=" + mPercentageChange +
                ", high=" + mHigh +
                ", low=" + mLow +
                ", volume=" + mVolume +
                ", volumeQuote=" + mVolumeQuote +
                ", bid=" + mBid +
                ", ask=" + mAsk +
                ", timestamp=" + mTimestamp +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InternalSymbolId mInternalSymbolId;
        private ExchangeId mExchangeId;
        private BigDecimal mLastPrice;
        private BigDecimal mChange;
        private BigDecimal mPercentageChange;
        private BigDecimal mHigh;
        private BigDecimal mLow;
        private BigDecimal mVolume;
        private BigDecimal mVolumeQuote;
        private BigDecimal mBid;
        private BigDecimal mAsk;
        private Instant mTimestamp;

        public Builder internalSymbolId(InternalSymbolId internalSymbolId) {
            this.mInternalSymbolId = internalSymbolId;
            return this;
        }

        public Builder exchangeId(ExchangeId exchangeId) {
            this.mExchangeId = exchangeId;
            return this;
        }

        public Builder lastPrice(BigDecimal lastPrice) {
            this.mLastPrice = lastPrice;
            return this;
        }


        public Builder percentageChange(BigDecimal percentageChange) {
            this.mPercentageChange = percentageChange;
            return this;
        }

        public Builder high(BigDecimal high) {
            this.mHigh = high;
            return this;
        }

        public Builder low(BigDecimal low) {
            this.mLow = low;
            return this;
        }

        public Builder volume(BigDecimal volume) {
            this.mVolume = volume;
            return this;
        }

        public Builder volumeQuote(BigDecimal volumeQuote) {
            this.mVolumeQuote = volumeQuote;
            return this;
        }

        public Builder bid(BigDecimal bid) {
            this.mBid = bid;
            return this;
        }

        public Builder ask(BigDecimal ask) {
            this.mAsk = ask;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.mTimestamp = timestamp;
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
