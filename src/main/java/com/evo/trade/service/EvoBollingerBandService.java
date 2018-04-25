package com.evo.trade.service;

import java.util.ArrayList;
import java.util.List;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.coinmarketcap.api.client.impl.CoinmarketcapApiService;
import com.coinmarketcap.api.objects.CoinmarketcapTickerPrice;
import com.evo.trade.binance.cache.AllMarketTickersCache;
import com.evo.trade.binance.cache.CandlesticksCache;
import com.evo.trade.objects.BollingerBand;
import com.evo.trade.utils.Utilities;

public class EvoBollingerBandService{
	
	// design pattern: singleton
	private static EvoBollingerBandService instance = null;
	private EvoBollingerBandService() {
		
	}
	public static EvoBollingerBandService getInstance() {
		if (instance == null) {
			instance = new EvoBollingerBandService();
		}
		return instance;
	}
	
	public List<BollingerBand> getBollingerBands(CandlestickInterval interval) {
		List<BollingerBand> bollingerBands = new ArrayList<>();
		List<CandlesticksCache> candlesticksCaches = BinanceCandlesticksService.getCandlesticksCaches(interval);
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
				
				AllMarketTickersCache allMarketTickersCache = BinanceAllMarketTickersService.getAllMarketTickers().getMarketTickersEvent(candlesticksCache.getSymbol());
				bb.set_24hr_volume( Double.valueOf( allMarketTickersCache.getTotalTradedBaseAssetVolume() ) );
				bb.set_24hr_quote_volume( Double.valueOf( allMarketTickersCache.getTotalTradedQuoteAssetVolume() ) );
				
				bollingerBands.add(bb);
			}
		}
		return bollingerBands;
	}
	
	private BollingerBand calcBollingerBand(List<Candlestick> candlestickBars) {
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
