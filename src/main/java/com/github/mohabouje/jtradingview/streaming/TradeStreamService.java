package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.*;
import info.bitrich.xchangestream.core.StreamingExchange;
import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TradeStreamService {
    private static final Logger logger = LoggerFactory.getLogger(TradeStreamService.class);

    private final Map<Instrument, Disposable> activeSubscriptions = new ConcurrentHashMap<>();
    private final ExchangeConnectionManager exchangeConnectionManager = new ExchangeConnectionManager();

    public void subscribe(Instrument instrument, TradeListener listener) {
        Objects.requireNonNull(instrument, "instrument cannot be null");
        Objects.requireNonNull(listener, "listener cannot be null");

        if (activeSubscriptions.containsKey(instrument)) {
            throw new IllegalStateException("Already subscribed to instrument: " + instrument);
        }

        try {
            StreamingExchange exchange = exchangeConnectionManager.getOrCreateExchange(instrument.getExchangeId());

            logger.info("Subscribing to trades for {} on {}", instrument.getInternalSymbolId(), instrument.getExchangeId());

            Disposable subscription = exchange.getStreamingMarketDataService()
                    .getTrades(instrument.toXChangeCurrencyPair())
                    .subscribe(
                            trade -> {
                                try {
                                    Trade protocolTrade = Trade.from(trade, instrument);
                                    listener.onTrade(protocolTrade);
                                } catch (Exception e) {
                                    logger.error("Error processing trade for {}: {}", instrument, e.getMessage(), e);
                                    listener.onError(e);
                                }
                            },
                            throwable -> {
                                logger.error("Error in trade stream for {}: {}", instrument, throwable.getMessage(), throwable);
                                listener.onError(throwable);
                                activeSubscriptions.remove(instrument);
                            },
                            () -> {
                                logger.info("Trade stream completed for {}", instrument);
                                listener.onDisconnect();
                                activeSubscriptions.remove(instrument);
                            }
                    );

            activeSubscriptions.put(instrument, subscription);
            listener.onConnect();

        } catch (Exception e) {
            logger.error("Failed to subscribe to {}: {}", instrument, e.getMessage(), e);
            listener.onError(e);
            throw new RuntimeException("Failed to subscribe to instrument: " + instrument, e);
        }
    }

    public boolean unsubscribe(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument cannot be null");

        Disposable subscription = activeSubscriptions.remove(instrument);
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
        logger.info("Shutting down TradeStreamService");

        activeSubscriptions.forEach((instrument, subscription) -> {
            logger.info("Disposing subscription for {}", instrument);
            subscription.dispose();
        });
        activeSubscriptions.clear();

        exchangeConnectionManager.disconnectAll();
    }
}