package com.github.mohabouje.jtradingview.streaming;

import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.protocol.Ticker;

public interface EventListener {
    void onTrade(Trade trade);

    void onTicker(Ticker ticker);

    void onError(Throwable throwable);
}
