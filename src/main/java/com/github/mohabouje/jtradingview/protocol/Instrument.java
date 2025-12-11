package com.github.mohabouje.jtradingview.protocol;

import java.util.Objects;

public class Instrument {
    private final ExchangeId exchangeId;
    private final Currency base;
    private final Currency quote;
    private final InternalSymbolId internalSymbolId;

    private Instrument(Builder builder) {
        this.exchangeId = builder.exchangeId;
        this.base = builder.base;
        this.quote = builder.quote;
        this.internalSymbolId = createInternalSymbolId(builder.exchangeId, builder.base, builder.quote);
    }

    private static InternalSymbolId createInternalSymbolId(ExchangeId exchangeId, Currency base, Currency quote) {
        String internal = String.format("%s:%s/%s", exchangeId.name(), base.getValue(), quote.getValue());
        return InternalSymbolId.of(internal);
    }

    public ExchangeId getExchangeId() {
        return exchangeId;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getQuote() {
        return quote;
    }

    public InternalSymbolId getInternalSymbolId() {
        return internalSymbolId;
    }

    public org.knowm.xchange.currency.CurrencyPair toXChangeCurrencyPair() {
        return new org.knowm.xchange.currency.CurrencyPair(base.getValue(), quote.getValue());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ExchangeId exchangeId;
        private Currency base;
        private Currency quote;

        public Builder exchangeId(ExchangeId exchangeId) {
            this.exchangeId = exchangeId;
            return this;
        }

        public Builder base(Currency base) {
            this.base = base;
            return this;
        }

        public Builder quote(Currency quote) {
            this.quote = quote;
            return this;
        }

        public Instrument build() {
            Objects.requireNonNull(exchangeId, "exchangeId is required");
            Objects.requireNonNull(base, "base currency is required");
            Objects.requireNonNull(quote, "quote currency is required");
            return new Instrument(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return exchangeId == that.exchangeId &&
                Objects.equals(base, that.base) &&
                Objects.equals(quote, that.quote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangeId, base, quote);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "exchangeId=" + exchangeId +
                ", base=" + base +
                ", quote=" + quote +
                ", internalSymbolId=" + internalSymbolId +
                '}';
    }
}

