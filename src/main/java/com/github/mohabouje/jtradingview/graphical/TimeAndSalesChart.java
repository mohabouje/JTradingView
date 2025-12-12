package com.github.mohabouje.jtradingview.graphical;

import com.github.mohabouje.jtradingview.protocol.OrderSide;
import com.github.mohabouje.jtradingview.protocol.Ticker;
import com.github.mohabouje.jtradingview.protocol.Trade;
import com.github.mohabouje.jtradingview.utility.CircularBuffer;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TimeAndSalesChart extends JPanel {

    private static final String SERIES_BID  = "Bid";
    private static final String SERIES_ASK  = "Ask";
    private static final String SERIES_BUY  = "Buy ↑";
    private static final String SERIES_SELL = "Sell ↓";

    private record DataPair(List<Date> x, List<Double> y) {
    }

    private final CircularBuffer<Trade> tradeBuffer;
    private final CircularBuffer<Ticker> tickerBuffer;
    private final XYChart chart;
    private final XChartPanel<XYChart> chartPanel;

    public TimeAndSalesChart(CircularBuffer<Trade> tradeBuffer,
                             CircularBuffer<Ticker> tickerBuffer) {

        this.tradeBuffer = tradeBuffer;
        this.tickerBuffer = tickerBuffer;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        this.chart = new XYChartBuilder()
                .title("Price & Trades")
                .xAxisTitle("Time")
                .yAxisTitle("Price")
                .width(600)
                .height(240)
                .build();

        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setPlotMargin(8);
        chart.getStyler().setPlotContentSize(0.95);
        chart.getStyler().setMarkerSize(10);

        chart.getStyler().setDatePattern("HH:mm:ss");

        List<Date> placeholderX = Collections.singletonList(new Date());
        List<Double> placeholderY = Collections.singletonList(Double.NaN);

        chart.addSeries(SERIES_BID,  placeholderX, placeholderY)
             .setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
        chart.addSeries(SERIES_ASK,  placeholderX, placeholderY)
             .setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
        chart.addSeries(SERIES_BUY,  placeholderX, placeholderY)
             .setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
        chart.addSeries(SERIES_SELL, placeholderX, placeholderY)
             .setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter);

        chart.getSeriesMap().get(SERIES_BID).setLineColor(new Color(0, 150, 0));
        chart.getSeriesMap().get(SERIES_BID).setLineWidth(2f);
        chart.getSeriesMap().get(SERIES_BID).setMarker(SeriesMarkers.NONE);
        chart.getSeriesMap().get(SERIES_BID).setShowInLegend(true);
        chart.getSeriesMap().get(SERIES_ASK).setLineColor(new Color(200, 0, 0));
        chart.getSeriesMap().get(SERIES_ASK).setLineWidth(2f);
        chart.getSeriesMap().get(SERIES_ASK).setMarker(SeriesMarkers.NONE);
        chart.getSeriesMap().get(SERIES_ASK).setShowInLegend(true);
        chart.getSeriesMap().get(SERIES_BUY).setMarkerColor(new Color(0, 150, 0));
        chart.getSeriesMap().get(SERIES_BUY).setMarker(SeriesMarkers.TRIANGLE_UP);
        chart.getSeriesMap().get(SERIES_SELL).setMarkerColor(new Color(200, 0, 0));
        chart.getSeriesMap().get(SERIES_SELL).setMarker(SeriesMarkers.TRIANGLE_DOWN);
        
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setPlotMargin(8);
        chart.getStyler().setPlotContentSize(0.95);
        chart.getStyler().setMarkerSize(10);
        chart.getStyler().setDatePattern("HH:mm:ss");

        this.chartPanel = new XChartPanel<>(chart);
        this.chartPanel.setPreferredSize(new Dimension(0, 220));

        add(chartPanel, BorderLayout.CENTER);
    }


    public void refresh() {
        if (tickerBuffer.isEmpty() && tradeBuffer.isEmpty()) {
            return;
        }

        DataPair bid = getBidData();
        DataPair ask = getAskData();
        DataPair buy = getBuyTradeData();
        DataPair sell = getSellTradeData();

        chart.updateXYSeries(SERIES_BID,  bid.x,  bid.y,  null);
        chart.updateXYSeries(SERIES_ASK,  ask.x,  ask.y,  null);
        chart.updateXYSeries(SERIES_BUY,  buy.x,  buy.y,  null);
        chart.updateXYSeries(SERIES_SELL, sell.x, sell.y, null);

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private DataPair getBidData() {
        int tickerSize = tickerBuffer.size();
        List<Date> xBid = new ArrayList<>(tickerSize);
        List<Double> yBid = new ArrayList<>(tickerSize);

        tickerBuffer.forEach(t -> {
            if (t == null || t.getBid() == null || t.getTimestamp() == null) {
                return;
            }
            xBid.add(new Date(t.getTimestamp().toEpochMilli()));
            yBid.add(t.getBid().doubleValue());
        });

        return new DataPair(xBid, yBid);
    }

    private DataPair getAskData() {
        int tickerSize = tickerBuffer.size();
        List<Date> xAsk = new ArrayList<>(tickerSize);
        List<Double> yAsk = new ArrayList<>(tickerSize);

        tickerBuffer.forEach(t -> {
            if (t == null || t.getAsk() == null || t.getTimestamp() == null) {
                return;
            }
            xAsk.add(new Date(t.getTimestamp().toEpochMilli()));
            yAsk.add(t.getAsk().doubleValue());
        });

        return new DataPair(xAsk, yAsk);
    }

    private DataPair getBuyTradeData() {
        int tradeSize = tradeBuffer.size();
        List<Date> xBuy = new ArrayList<>(tradeSize);
        List<Double> yBuy = new ArrayList<>(tradeSize);

        tradeBuffer.forEach(tr -> {
            if (tr == null || tr.getPrice() == null || tr.getTimestamp() == null) {
                return;
            }
            if (tr.getSide() != OrderSide.BUY) {
                return;
            }
            xBuy.add(new Date(tr.getTimestamp().toEpochMilli()));
            yBuy.add(tr.getPrice().doubleValue());
        });

        return new DataPair(xBuy, yBuy);
    }

    private DataPair getSellTradeData() {
        int tradeSize = tradeBuffer.size();
        List<Date> xSell = new ArrayList<>(tradeSize);
        List<Double> ySell = new ArrayList<>(tradeSize);

        tradeBuffer.forEach(tr -> {
            if (tr == null || tr.getPrice() == null || tr.getTimestamp() == null) {
                return;
            }
            if (tr.getSide() != OrderSide.SELL) {
                return;
            }
            xSell.add(new Date(tr.getTimestamp().toEpochMilli()));
            ySell.add(tr.getPrice().doubleValue());
        });

        return new DataPair(xSell, ySell);
    }
}
