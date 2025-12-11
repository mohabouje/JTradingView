package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.Trade;

public interface TradeListener {
    void onTrade(Trade trade);

    void onError(Throwable throwable);

    void onConnect();

    void onDisconnect();
}

