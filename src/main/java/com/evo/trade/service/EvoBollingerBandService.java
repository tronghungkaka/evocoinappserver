package com.evo.trade.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.coinmarketcap.api.client.impl.CoinmarketcapApiService;
import com.coinmarketcap.api.objects.CoinmarketcapTickerPrice;
import com.evo.trade.binance.cache.AllMarketTickers;
import com.evo.trade.binance.cache.AllMarketTickersCache;
import com.evo.trade.binance.cache.CandlesticksCache;
import com.evo.trade.objects.BollingerBand;
import com.evo.trade.objects.EvoTimeInterval;
import com.evo.trade.objects.EvoTimeIntervalMillis;
import com.evo.trade.utils.Utilities;

public class EvoBollingerBandService implements Runnable{
	
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
	
	public EvoBollingerBandService(List<CandlestickInterval> candlestickIntervals) {
		this.threadName = "Evo Bollinger Band Service";
		this.interval = EvoTimeIntervalMillis.DAILY_MILLIS.getIntervalIdMillis();
		this.candlestickIntervals = candlestickIntervals;
	}
	
	public EvoBollingerBandService(String threadName, Long interval, List<CandlestickInterval> candlestickIntervals) {
		this.threadName = threadName;
		this.interval = interval;
		this.candlestickIntervals = candlestickIntervals;
	}
	
	public static Map<CandlestickInterval, List<CandlesticksCache>> getAllCandlesticksCaches() {
		return map;
	}
	
	public static List<CandlesticksCache> getCandlesticksCaches(CandlestickInterval interval) {
		System.out.println("EvobollingerBandService.java: interval=" + interval);
		System.out.println("EvobollingerBandService.java: interval.getIntervalId()=" + interval.getIntervalId());
		System.out.println("EvoBollingerBandService.java: map.get(interval).size()=" + map.get(interval).size());
		return map.get(interval);
	}
	
	public static List<BollingerBand> getBollingerBands(CandlestickInterval interval) {
		List<BollingerBand> bollingerBands = new ArrayList<>();
		List<CandlesticksCache> candlesticksCaches = getCandlesticksCaches(interval);
		CoinmarketcapTickerPrice cmcBTCTickerPrice = CoinmarketcapApiService.getCoinmarketcapTickerPrice("bitcoin", null);
		CoinmarketcapTickerPrice cmcETHTickerPrice = CoinmarketcapApiService.getCoinmarketcapTickerPrice("ethereum", null);
		CoinmarketcapTickerPrice cmcBNBTickerPrice = CoinmarketcapApiService.getCoinmarketcapTickerPrice("binance-coin", null);
		CoinmarketcapTickerPrice cmcUSDTTickerPrice = CoinmarketcapApiService.getCoinmarketcapTickerPrice("tether", null);
		
//		System.out.println("EvoBollingerBandService.java: updateCount=" + CandlesticksCache.updateCount);
		
		for (CandlesticksCache candlesticksCache : candlesticksCaches) {
			List<Candlestick> candlesticks = candlesticksCache.getLatestCandlesticksCacheSet(20);
			BollingerBand bb = calcBollingerBand(candlesticks);
			if (bb == null) {
				continue;
			}
			if (bb.isOutOfBands()) {
				bb.setSymbol(candlesticksCache.getSymbol());
				bb.setInterval(candlesticksCache.getInterval().toString());
				
				CoinmarketcapTickerPrice cmcTickerPrice = null;
				if(bb.getBaseCurrencySymbol().equalsIgnoreCase("btc")) {
					cmcTickerPrice = cmcBTCTickerPrice;
				}
				else if(bb.getBaseCurrencySymbol().equalsIgnoreCase("eth")) {
					cmcTickerPrice = cmcETHTickerPrice;
				}
				else if(bb.getBaseCurrencySymbol().equalsIgnoreCase("bnb")) {
					cmcTickerPrice = cmcBNBTickerPrice;
				}
				else if(bb.getBaseCurrencySymbol().equalsIgnoreCase("usdt")) {
					cmcTickerPrice = cmcUSDTTickerPrice;
				}
//				bb.getComparativePercentage(); // to calc the comparative percentage
				bb.setUsdPrice( Double.valueOf( candlesticks.get(0).getClose() ) * Double.valueOf( cmcTickerPrice.getPrice_usd() ) );
				bb.setBaseCurrencyPrice( Double.valueOf( candlesticks.get(0).getClose() ) );
				
//				List<Candlestick> _1wCandlesticks = BinanceApiService.getCandlestickBars(tickerPrice.getSymbol(), "1w", 1, null, null);
//				List<Candlestick> _1MCandlesticks = BinanceApiService.getCandlestickBars(tickerPrice.getSymbol(), "1M", 1, null, null);
//				bb.set_1wHighPrice( Double.valueOf(_1wCandlesticks.get(0).getHigh()) );
//				bb.set_1wLowPrice( Double.valueOf(_1wCandlesticks.get(0).getLow()) );
//				bb.set_1MHighPrice( Double.valueOf(_1MCandlesticks.get(0).getHigh()) );
//				bb.set_1MLowPrice( Double.valueOf(_1MCandlesticks.get(0).getLow()) );
				
//				List<AllMarketTickersCache> allMarketTickersEvents = AllMarketTickers.getAllMarketTickersEvent();
//				for (AllMarketTickersCache allMarketTickersCache : allMarketTickersEvents) {
//					System.out.println("candlesticksCache.getSymbol(): " + candlesticksCache.getSymbol());
//					if (allMarketTickersCache.getSymbol().equalsIgnoreCase(candlesticksCache.getSymbol()) ) {
//						bb.set_24hr_volume( Double.valueOf( allMarketTickersCache.getTotalTradedBaseAssetVolume() ) );
//						bb.set_24hr_quote_volume( Double.valueOf( allMarketTickersCache.getTotalTradedQuoteAssetVolume() ) );
//						System.out.println("_24hr_quote_volume: " + allMarketTickersCache.getTotalTradedQuoteAssetVolume());
//					}
//				}
				
				AllMarketTickersCache allMarketTickersCache = AllMarketTickers.getMarketTickersEvent(candlesticksCache.getSymbol());
				bb.set_24hr_volume( Double.valueOf( allMarketTickersCache.getTotalTradedBaseAssetVolume() ) );
				bb.set_24hr_quote_volume( Double.valueOf( allMarketTickersCache.getTotalTradedQuoteAssetVolume() ) );
				
				bollingerBands.add(bb);
			}
		}
		return bollingerBands;
	}
	
	private static BollingerBand calcBollingerBand(List<Candlestick> candlestickBars) {
		if (candlestickBars == null)
			return null;

		List<Double> closePrices = new ArrayList<>();
		int i=0, _10_period_avg_volume_num = 10;
		Double quoteAssetVolumeSum = 0.;
		
		for (Candlestick cdstBar : candlestickBars) {
			closePrices.add(Double.valueOf(cdstBar.getClose()));
			if(i < _10_period_avg_volume_num) {
				++i;
				quoteAssetVolumeSum += Double.valueOf( cdstBar.getQuoteAssetVolume() );
			}
		}
		
		BollingerBand bb = new BollingerBand("binance", Double.valueOf(candlestickBars.get(0).getClose()));
		bb.set_10PeriodAVGVolume( quoteAssetVolumeSum / _10_period_avg_volume_num );
		bb.setTimestamp(System.currentTimeMillis());
		bb.setSma(Utilities.average(closePrices));
		Double stddev = Utilities.stdDeviation(closePrices);
		bb.setUpperBB(bb.getSma() + (stddev * BollingerBand.FACTOR));
		bb.setLowerBB(bb.getSma() - (stddev * BollingerBand.FACTOR));
		if (bb.isOutOfBands())
			return bb;

		return null;
	}
}
