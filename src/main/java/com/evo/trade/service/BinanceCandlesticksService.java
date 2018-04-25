package com.evo.trade.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.evo.trade.binance.cache.CandlesticksCache;
import com.evo.trade.objects.EvoTimeIntervalMillis;

public class BinanceCandlesticksService implements Runnable {
	// Thread implements
	protected Thread thread;
	protected String threadName;
	protected boolean threadSuspended = false;
	protected long interval; // milliseconds

	public void suspend() {
		threadSuspended = true;
		System.out.println(threadName + " suspended");
	}

	public synchronized void resume() {
		threadSuspended = false;
		notify();
		System.out.println(threadName + " resumed");
	}

	public synchronized void stop() {
		thread = null;
		notify();
		System.out.println(threadName + " stoped");
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
			System.out.println(threadName + " started");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thisThread = Thread.currentThread();
		while (thread == thisThread) {
			try {
				// Please place your codes here.
				// ...
				Long startTimeMillis = System.currentTimeMillis();
				
				if(map != null && !map.isEmpty()) {
					for (CandlestickInterval cdInterval : candlestickIntervals) {
						List<CandlesticksCache> candlesticksCaches = map.get(cdInterval);
						if (candlesticksCaches == null || candlesticksCaches.isEmpty())
							continue;
						
						for (CandlesticksCache candlesticksCache : candlesticksCaches) {
							candlesticksCache.close();
						}
					}
				}
				
				map = new TreeMap<>();
				for (CandlestickInterval cdInterval : candlestickIntervals) {
					List<CandlesticksCache> candlesticksCaches = new ArrayList<>();
					map.put(cdInterval, candlesticksCaches);
				}

				BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
			    BinanceApiRestClient client = factory.newRestClient();
				List<TickerPrice> tickerPrices = client.getAllPrices();
				
				int threadCount = 0;
				for (CandlestickInterval cdInterval : candlestickIntervals) {
					List<CandlesticksCache> candlesticksCaches = new ArrayList<>();
					for (TickerPrice tickerPrice : tickerPrices) {
						candlesticksCaches.add( new CandlesticksCache(tickerPrice.getSymbol(), cdInterval) );
						++threadCount;
					}
					map.put(cdInterval, candlesticksCaches);
				}
				System.out.println("threadCount=" + threadCount);
				
				Long endTimeMillis = System.currentTimeMillis();
				
				System.out.println(threadName + " running: endTimeMillis - startTimeMillis = " + (endTimeMillis - startTimeMillis));
				// END your codes
				
				Thread.sleep(interval - (endTimeMillis - startTimeMillis));
				synchronized (this) {
					while (threadSuspended && thread == thisThread)
						wait();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// END: Thread implements
	
	static Map<CandlestickInterval, List<CandlesticksCache>> map;
	private List<CandlestickInterval> candlestickIntervals;
	
	public BinanceCandlesticksService(List<CandlestickInterval> candlestickIntervals) {
		this.threadName = "Binance Candlesticks Service";
		this.interval = EvoTimeIntervalMillis.DAILY_MILLIS.getIntervalIdMillis();
		this.candlestickIntervals = candlestickIntervals;
	}
	
	public BinanceCandlesticksService(String threadName, Long interval, List<CandlestickInterval> candlestickIntervals) {
		this.threadName = threadName;
		this.interval = interval;
		this.candlestickIntervals = candlestickIntervals;
	}
	
	public static Map<CandlestickInterval, List<CandlesticksCache>> getAllCandlesticksCaches() {
		return map;
	}
	
	public static List<CandlesticksCache> getCandlesticksCaches(CandlestickInterval interval) {
//		System.out.println("BinanceCandlesticksService.java: interval=" + interval);
//		System.out.println("BinanceCandlesticksService.java: interval.getIntervalId()=" + interval.getIntervalId());
//		System.out.println("BinanceCandlesticksService.java: map.get(interval).size()=" + map.get(interval).size());
		return map.get(interval);
	}
}
