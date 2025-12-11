package com.github.mohabouje.jtradingview.protocol;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class Trade {
    private final TradeId mTradeId;
    private final ExchangeId mExchangeId;
    private final InternalSymbolId mInternalSymbolId;
    private final BigDecimal mPrice;
    private final BigDecimal mQuantity;
    private final OrderSide mSide;
    private final Instant mTimestamp;
    private final Instant mReceivedAt; 
    private final Liquidity mLiquidity;
    
    private Trade(Builder builder) {
        this.mTradeId = builder.mTradeId;
        this.mExchangeId = builder.mExchangeId;
        this.mInternalSymbolId = builder.mInternalSymbolId;
        this.mPrice = builder.mPrice;
        this.mQuantity = builder.mQuantity;
        this.mSide = builder.mSide;
        this.mTimestamp = builder.mTimestamp;
        this.mReceivedAt = builder.mReceivedAt != null ? builder.mReceivedAt : Instant.now();
        this.mLiquidity = builder.mLiquidity;
    }
    
    public TradeId getTradeId() { return mTradeId; }
    public ExchangeId getExchangeId() { return mExchangeId; }
    public InternalSymbolId getInternalSymbolId() { return mInternalSymbolId; }
    public BigDecimal getPrice() { return mPrice; }
    public BigDecimal getQuantity() { return mQuantity; }
    public OrderSide getSide() { return mSide; }
    public Instant getTimestamp() { return mTimestamp; }
    public Instant getReceivedAt() { return mReceivedAt; }
    public Liquidity getLiquidity() { return mLiquidity; }
    
    public long getLatencyMs() {
        return java.time.temporal.ChronoUnit.MILLIS.between(mTimestamp, mReceivedAt);
    }
    
    public BigDecimal getNotionalValue() {
        return mPrice.multiply(mQuantity);
    }
    
    public static Trade from(org.knowm.xchange.dto.marketdata.Trade xchangeTrade, Instrument instrument) {
        TradeId tradeId = Optional.ofNullable(xchangeTrade.getId())
                .map(TradeId::of)
                .orElseGet(() -> TradeId.of(String.valueOf(System.nanoTime())));

        OrderSide side = switch (xchangeTrade.getType()) {
            case BID -> OrderSide.BUY;
            case ASK -> OrderSide.SELL;
            default -> throw new IllegalArgumentException("Unknown order type: " + xchangeTrade.getType());
        };

        BigDecimal price = xchangeTrade.getPrice();
        BigDecimal quantity = xchangeTrade.getOriginalAmount();
        
        Instant timestamp = Optional.ofNullable(xchangeTrade.getTimestamp())
                .map(date -> date.toInstant())
                .orElseGet(Instant::now);

        return Trade.builder()
                .tradeId(tradeId)
                .exchangeId(instrument.getExchangeId())
                .internalSymbolId(instrument.getInternalSymbolId())
                .price(price)
                .quantity(quantity)
                .side(side)
                .timestamp(timestamp)
                .build();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private TradeId mTradeId;
        private ExchangeId mExchangeId;
        private InternalSymbolId mInternalSymbolId;
        private BigDecimal mPrice;
        private BigDecimal mQuantity;
        private OrderSide mSide;
        private Instant mTimestamp;
        private Instant mReceivedAt;
        private Liquidity mLiquidity;
        
        public Builder tradeId(TradeId tradeId) {
            this.mTradeId = tradeId;
            return this;
        }
        
        public Builder exchangeId(ExchangeId exchangeId) {
            this.mExchangeId = exchangeId;
            return this;
        }
        
        public Builder internalSymbolId(InternalSymbolId internalSymbolId) {
            this.mInternalSymbolId = internalSymbolId;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            this.mPrice = price;
            return this;
        }
        
        public Builder quantity(BigDecimal quantity) {
            this.mQuantity = quantity;
            return this;
        }
        
        public Builder side(OrderSide side) {
            this.mSide = side;
            return this;
        }
        
        public Builder timestamp(Instant timestamp) {
            this.mTimestamp = timestamp;
            return this;
        }
        
        public Builder receivedAt(Instant receivedAt) {
            this.mReceivedAt = receivedAt;
            return this;
        }
        
        public Builder liquidity(Liquidity liquidity) {
            this.mLiquidity = liquidity;
            return this;
        }
        
        public Trade build() {
            Objects.requireNonNull(mTradeId, "tradeId is required");
            Objects.requireNonNull(mExchangeId, "exchangeId is required");
            Objects.requireNonNull(mInternalSymbolId, "internalSymbolId is required");
            Objects.requireNonNull(mPrice, "price is required");
            Objects.requireNonNull(mQuantity, "quantity is required");
            Objects.requireNonNull(mSide, "side is required");
            Objects.requireNonNull(mTimestamp, "timestamp is required");
            return new Trade(this);
        }
    }
    
    @Override
    public String toString() {
        return "Trade{" +
                "TradeId=" + mTradeId +
                ", ExchangeId=" + mExchangeId +
                ", InternalSymbolId=" + mInternalSymbolId +
                ", Price=" + mPrice +
                ", Quantity=" + mQuantity +
                ", Side=" + mSide +
                ", Timestamp=" + mTimestamp +
                ", Liquidity=" + mLiquidity +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(mInternalSymbolId, trade.mInternalSymbolId) &&
                Objects.equals(mTradeId, trade.mTradeId) &&
                mExchangeId == trade.mExchangeId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mTradeId, mExchangeId, mInternalSymbolId);
    }
}
