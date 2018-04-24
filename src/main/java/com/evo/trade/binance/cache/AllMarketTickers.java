package com.evo.trade.binance.cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.evo.trade.binance.cache.AllMarketTickersCache;

public class AllMarketTickers {
	
	Map<String, AllMarketTickersCache> map;
	Closeable ws;
	

	public AllMarketTickers() {
		map = new TreeMap();
		startAllMarketTickersEventStreaming();
	}
	
	private void startAllMarketTickersEventStreaming() {
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
		BinanceApiWebSocketClient client = factory.newWebSocketClient();

		ws = client.onAllMarketTickersEvent(event -> {
//			 System.out.println(event);
			 String str = event.toString();
			 int i=0, c;
			 for (int x = 0; x < event.size(); ++x) {
				 AllMarketTickersCache allMarketTickersCache = new AllMarketTickersCache();
				 i = str.indexOf('s', i) + 2;
				 c = str.indexOf(',', i);
				 allMarketTickersCache.setSymbol(str.substring(i, c));
				 
				 i = str.indexOf('c', i) + 2;
				 c = str.indexOf(',', i);
				 allMarketTickersCache.setCurrentDaysClosePrice(str.substring(i, c));
				 
				 i = str.indexOf('v', i) + 2;
				 c = str.indexOf(',', i);
				 allMarketTickersCache.setTotalTradedBaseAssetVolume(str.substring(i, c));
				 
				 i = str.indexOf('q', i) + 2;
				 c = str.indexOf(',', i);
				 allMarketTickersCache.setTotalTradedQuoteAssetVolume(str.substring(i, c));
				 
				 map.put(allMarketTickersCache.getSymbol(), allMarketTickersCache);
			 }
//			 System.out.println("map.size(): " + map.size());
		});
	}
	
	public AllMarketTickersCache getMarketTickersEvent(String symbol) {
		return map.get(symbol);
	}
	
	public void close() throws IOException {
		if (ws == null)
			return;
		ws.close();
	}
}
