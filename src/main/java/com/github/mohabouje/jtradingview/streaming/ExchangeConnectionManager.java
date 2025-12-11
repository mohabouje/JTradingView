package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.ExchangeId;
import info.bitrich.xchangestream.binance.BinanceStreamingExchange;
import info.bitrich.xchangestream.bitstamp.v2.BitstampStreamingExchange;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.kraken.KrakenStreamingExchange;
import info.bitrich.xchangestream.bybit.BybitStreamingExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ExchangeConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeConnectionManager.class);

    private final Map<ExchangeId, StreamingExchange> exchangeConnections = new ConcurrentHashMap<>();

    public StreamingExchange getOrCreateExchange(ExchangeId exchangeId, org.knowm.xchange.currency.CurrencyPair initialPair) {
        return exchangeConnections.computeIfAbsent(exchangeId, id -> {
            try {
                logger.info("Connecting to exchange: {}", id);

                StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(getExchangeClass(id));

                ProductSubscription.ProductSubscriptionBuilder builder = ProductSubscription.create();
                if (initialPair != null) {
                    builder.addTicker(initialPair);
                }
                ProductSubscription subscription = builder.build();

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

    private Class<? extends StreamingExchange> getExchangeClass(ExchangeId exchangeId) {
        return switch (exchangeId) {
            case KRAKEN -> KrakenStreamingExchange.class;
            case BITSTAMP -> BitstampStreamingExchange.class;
            case BINANCE -> BinanceStreamingExchange.class;
            case BYBIT -> BybitStreamingExchange.class;
        };
    }
}
