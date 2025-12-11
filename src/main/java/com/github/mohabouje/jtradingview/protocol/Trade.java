package com.github.mohabouje.jtradingview.protocol;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class Trade {
    private final TradeId tradeId;
    private final ExchangeId exchangeId;
    private final InternalSymbolId internalSymbolId;
    private final BigDecimal price;
    private final BigDecimal quantity;
    private final OrderSide side;
    private final Instant timestamp;
    private final Instant receivedAt;
    private final Liquidity liquidity;
    
    private Trade(Builder builder) {
        this.tradeId = builder.tradeId;
        this.exchangeId = builder.exchangeId;
        this.internalSymbolId = builder.internalSymbolId;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.side = builder.side;
        this.timestamp = builder.timestamp;
        this.receivedAt = builder.receivedAt != null ? builder.receivedAt : Instant.now();
        this.liquidity = builder.liquidity;
    }
    
    public TradeId getTradeId() { return tradeId; }
    public ExchangeId getExchangeId() { return exchangeId; }
    public InternalSymbolId getInternalSymbolId() { return internalSymbolId; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getQuantity() { return quantity; }
    public OrderSide getSide() { return side; }
    public Instant getTimestamp() { return timestamp; }
    public Instant getReceivedAt() { return receivedAt; }
    public Liquidity getLiquidity() { return liquidity; }
    
    public long getLatencyMs() {
        return java.time.temporal.ChronoUnit.MILLIS.between(timestamp, receivedAt);
    }
    
    public BigDecimal getNotionalValue() {
        return price.multiply(quantity);
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
        private TradeId tradeId;
        private ExchangeId exchangeId;
        private InternalSymbolId internalSymbolId;
        private BigDecimal price;
        private BigDecimal quantity;
        private OrderSide side;
        private Instant timestamp;
        private Instant receivedAt;
        private Liquidity liquidity;
        
        public Builder tradeId(TradeId tradeId) {
            this.tradeId = tradeId;
            return this;
        }
        
        public Builder exchangeId(ExchangeId exchangeId) {
            this.exchangeId = exchangeId;
            return this;
        }
        
        public Builder internalSymbolId(InternalSymbolId internalSymbolId) {
            this.internalSymbolId = internalSymbolId;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        
        public Builder quantity(BigDecimal quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public Builder side(OrderSide side) {
            this.side = side;
            return this;
        }
        
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder receivedAt(Instant receivedAt) {
            this.receivedAt = receivedAt;
            return this;
        }
        
        public Builder liquidity(Liquidity liquidity) {
            this.liquidity = liquidity;
            return this;
        }
        
        public Trade build() {
            Objects.requireNonNull(tradeId, "tradeId is required");
            Objects.requireNonNull(exchangeId, "exchangeId is required");
            Objects.requireNonNull(internalSymbolId, "internalSymbolId is required");
            Objects.requireNonNull(price, "price is required");
            Objects.requireNonNull(quantity, "quantity is required");
            Objects.requireNonNull(side, "side is required");
            Objects.requireNonNull(timestamp, "timestamp is required");
            return new Trade(this);
        }
    }
    
    @Override
    public String toString() {
        return "Trade{" +
                "tradeId=" + tradeId +
                ", exchangeId=" + exchangeId +
                ", internalSymbolId=" + internalSymbolId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side=" + side +
                ", timestamp=" + timestamp +
                ", liquidity=" + liquidity +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(internalSymbolId, trade.internalSymbolId) &&
                Objects.equals(tradeId, trade.tradeId) &&
                exchangeId == trade.exchangeId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tradeId, exchangeId, internalSymbolId);
    }
}
