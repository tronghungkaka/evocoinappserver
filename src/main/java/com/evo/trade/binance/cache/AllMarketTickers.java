package com.evo.trade.binance.cache;

import java.util.ArrayList;
import java.util.List;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AllMarketTickersEvent;
import com.binance.api.client.domain.market.TickerStatistics;

public class AllMarketTickers {
	
	static List<AllMarketTickersEvent> allMarketTickersEvents = new ArrayList<>();

	public AllMarketTickers() {
		startAllMarketTickersEventStreaming();
	}
	
	private void startAllMarketTickersEventStreaming() {
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
	    BinanceApiWebSocketClient client = factory.newWebSocketClient();

	    client.onAllMarketTickersEvent(event -> {
//	      System.out.println(event);
	    	allMarketTickersEvents = event;
	    });
	}
	
	public static List<AllMarketTickersEvent> getAllMarketTickersEvent() {
		return allMarketTickersEvents;
	}
}
