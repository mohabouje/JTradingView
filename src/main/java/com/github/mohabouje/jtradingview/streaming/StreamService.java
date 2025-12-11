package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.*;
import info.bitrich.xchangestream.core.StreamingExchange;
import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StreamService {
    private static final Logger logger = LoggerFactory.getLogger(StreamService.class);

    private final Map<Instrument, Subscription> activeSubscriptions = new ConcurrentHashMap<>();
    private final ExchangeConnectionManager exchangeConnectionManager = new ExchangeConnectionManager();

    public void subscribe(Instrument instrument, EventListener listener) {
        Objects.requireNonNull(instrument, "instrument cannot be null");
        Objects.requireNonNull(listener, "listener cannot be null");

        if (activeSubscriptions.containsKey(instrument)) {
            throw new IllegalStateException("Already subscribed to instrument: " + instrument);
        }

        try {
            StreamingExchange exchange = exchangeConnectionManager.getOrCreateExchange(instrument.getExchangeId());

            logger.info("Subscribing to trades for {} on {}", instrument.getInternalSymbolId(), instrument.getExchangeId());

            Disposable tradeStream = exchange.getStreamingMarketDataService()
                    .getTrades(instrument.toXChangeCurrencyPair())
                    .subscribe(
                            item -> listener.onTrade(Trade.from(item, instrument)),
                            listener::onError,
                            () -> {}
                    );

            Disposable tickerStream = exchange.getStreamingMarketDataService()
                    .getTicker(instrument.toXChangeCurrencyPair())
                    .subscribe(
                            item -> listener.onTicker(Ticker.from(item, instrument)),
                            listener::onError,
                            () -> {}
                    );

            Subscription subscription = new Subscription(tradeStream, tickerStream);
            activeSubscriptions.put(instrument, subscription);

        } catch (Exception e) {
            logger.error("Failed to subscribe to {}: {}", instrument, e.getMessage(), e);
        }
    }

    public boolean unsubscribe(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument cannot be null");

        Subscription subscription = activeSubscriptions.remove(instrument);
        if (subscription != null) {
            logger.info("Unsubscribing from trades for {}", instrument);
            subscription.dispose();
            return true;
        }
        return false;
    }

    public boolean isSubscribed(Instrument instrument) {
        return activeSubscriptions.containsKey(instrument);
    }

    public void shutdown() {
        logger.info("Shutting down StreamService");

        activeSubscriptions.forEach((instrument, subscription) -> {
            logger.info("Disposing subscription for {}", instrument);
            subscription.dispose();
        });
        activeSubscriptions.clear();

        exchangeConnectionManager.disconnectAll();
    }

    private static class Subscription {
        private final Disposable tradeStream;
        private final Disposable tickerStream;

        Subscription(Disposable tradeStream, Disposable tickerStream) {
            this.tradeStream = tradeStream;
            this.tickerStream = tickerStream;
        }

        void dispose() {
            if (tradeStream != null) {
                tradeStream.dispose();
            }
            if (tickerStream != null) {
                tickerStream.dispose();
            }
        }
    }
}
