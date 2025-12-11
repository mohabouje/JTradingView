package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.ExchangeId;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ExchangeConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeConnectionManager.class);

    private final Map<ExchangeId, StreamingExchange> exchangeConnections = new ConcurrentHashMap<>();

    public StreamingExchange getOrCreateExchange(ExchangeId exchangeId) {
        return exchangeConnections.computeIfAbsent(exchangeId, id -> {
            try {
                String exchangeClassName = getExchangeClassName(id);
                logger.info("Connecting to exchange: {}", id);

                StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(exchangeClassName);

                ProductSubscription subscription = ProductSubscription.create()
                        .build();

                exchange.connect(subscription).blockingAwait();
                logger.info("Successfully connected to {}", id);

                return exchange;
            } catch (Exception e) {
                logger.error("Failed to connect to {}: {}", id, e.getMessage(), e);
                throw new RuntimeException("Failed to connect to exchange: " + id, e);
            }
        });
    }

    public void disconnectAll() {
        exchangeConnections.forEach((exchangeId, exchange) -> {
            try {
                logger.info("Disconnecting from {}", exchangeId);
                exchange.disconnect().blockingAwait();
            } catch (Exception e) {
                logger.error("Error disconnecting from {}: {}", exchangeId, e.getMessage(), e);
            }
        });
        exchangeConnections.clear();
    }

    private String getExchangeClassName(ExchangeId exchangeId) {
        switch (exchangeId) {
            case BINANCE:
                return "info.bitrich.xchangestream.binance.BinanceStreamingExchange";
            case COINBASE:
                return "info.bitrich.xchangestream.coinbasepro.CoinbaseProStreamingExchange";
            case KRAKEN:
                return "info.bitrich.xchangestream.kraken.KrakenStreamingExchange";
            default:
                throw new IllegalArgumentException("Unsupported exchange: " + exchangeId);
        }
    }
}
