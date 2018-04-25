package com.evo.trade.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.evo.trade.binance.cache.CandlesticksCache;
import com.evo.trade.objects.Dominance;
import com.evo.trade.objects.Dominance.Excess;
import com.evo.trade.utils.Utilities;

public class EvoDominanceService {
	// singleton pattern
	private static EvoDominanceService instance = null;
	private EvoDominanceService() {
		
	}
	public static EvoDominanceService getInstance() {
		if (instance == null) {
			instance = new EvoDominanceService();
		}
		return instance;
	}
	
	public List<Dominance> getDominances() {
		List<Dominance> dominances = new ArrayList<>();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		
		List<Long> dayOpenTimes = new ArrayList<>();
		Map<Long, Double> _15daysAfterToday = new TreeMap<>();
		for (int i=0; i<15; ++i) {
			today.add(Calendar.DAY_OF_MONTH, -1);
			dayOpenTimes.add(today.getTimeInMillis());
			_15daysAfterToday.put( today.getTimeInMillis(), 0. );
		}
		List<CandlesticksCache> _1hCandlesticksCaches = BinanceCandlesticksService.getCandlesticksCaches(CandlestickInterval.getCandlestickInterval("1h"));
		for (CandlesticksCache _1hCandlesticksCache : _1hCandlesticksCaches) {
			
			// Only check for btc or eth market
			if (!(_1hCandlesticksCache.getSymbol().endsWith("BTC") || _1hCandlesticksCache.getSymbol().endsWith("ETH"))) {
				continue;
			}
			
			// reninitialize the map
			for (int i=0; i<dayOpenTimes.size(); ++i) {
				_15daysAfterToday.put(dayOpenTimes.get(i), 0.);
			}
			
			List<Candlestick> candlesticks = _1hCandlesticksCache.getAllLatestCandlesticksCacheSet();
			for (Candlestick candlestick : candlesticks) {
				Long openTime = candlestick.getOpenTime();
				for (Long dayOpenTime : dayOpenTimes) {
					if (!(openTime >= dayOpenTime)) {
						continue;
					}
					Double quoteAssetVolume = Double.valueOf( candlestick.getQuoteAssetVolume());
					Double sell = Double.valueOf( candlestick.getTakerBuyQuoteAssetVolume());
					Double buy = quoteAssetVolume - sell;
					Double buy_sell = _15daysAfterToday.get(dayOpenTime) + buy - sell;
					
					_15daysAfterToday.put(dayOpenTime, buy_sell);
				}
			}
			
			boolean is5dayNegativeExcess = true;
			for (int j=0; j<5; ++j) {
				if (_15daysAfterToday.get(dayOpenTimes.get(j)) > 0) {
					is5dayNegativeExcess = false;
				}
			}
			if (is5dayNegativeExcess == false) {
				continue;
			}
			
			// dominance
			Dominance dominance = new Dominance();
			dominance.setSymbol(_1hCandlesticksCache.getSymbol());
			dominance.setBaseCurrencyPrice(candlesticks.get(0).getClose());
			Double usdPrice = Double.valueOf(BinanceAllMarketTickersService.getAllMarketTickers().getMarketTickersEvent("BTCUSDT").getCurrentDaysClosePrice())
					* Double.valueOf(dominance.getBaseCurrencyPrice());
			dominance.setUsdPrice("" + usdPrice);
			
			dominance.set_24hr_quote_volume(BinanceAllMarketTickersService.getAllMarketTickers().getMarketTickersEvent(dominance.getSymbol()).getTotalTradedQuoteAssetVolume());
			
			// excess
			List<Excess> excess = new ArrayList<>();
			for (int j=dayOpenTimes.size()-1; j>=0; --j) {
				Excess ex = new Excess();
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(dayOpenTimes.get(j));
				ex.setDate("" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR));
				ex.setValue("" + _15daysAfterToday.get(dayOpenTimes.get(j)));
				excess.add(ex);
			}
			dominance.setExcess(excess);
			
			// rsi with 4h candlesticks
			List<CandlesticksCache> _4hCandlesticksCaches = BinanceCandlesticksService.getCandlesticksCaches(CandlestickInterval.getCandlestickInterval("4h"));
			List<Double> closePrices = new ArrayList<>();
			for (CandlesticksCache candlesticksCache : _4hCandlesticksCaches) {
				if (candlesticksCache.getSymbol().equals(dominance.getSymbol())) {
					for (Candlestick candlestick : candlesticksCache.getLatestCandlesticksCacheSet(100)) {
						closePrices.add(Double.valueOf(candlestick.getClose()));
					}
					break;
				}
			}
			dominance.setRsi(Utilities.calcRSI(closePrices));
			
			dominances.add(dominance);
		}
		return dominances;
	}
}
