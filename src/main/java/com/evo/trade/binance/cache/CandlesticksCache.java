package com.evo.trade.binance.cache;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Illustrates how to use the klines/candlesticks event stream to create a local cache of bids/asks for a symbol.
 */
public class CandlesticksCache {

  /**
   * Key is the start/open time of the candle, and the value contains candlestick date.
   */
  private Map<Long, Candlestick> candlesticksCache;
  private int maxSize = 50;
  private String symbol;
  private CandlestickInterval interval;

  public CandlesticksCache(String symbol, CandlestickInterval interval) {
	  this.symbol = symbol;
	  this.interval = interval;
    initializeCandlestickCache(symbol, interval);
    startCandlestickEventStreaming(symbol, interval);
  }

  /**
   * Initializes the candlestick cache by using the REST API.
   */
  private void initializeCandlestickCache(String symbol, CandlestickInterval interval) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiRestClient client = factory.newRestClient();
    List<Candlestick> candlestickBars = client.getCandlestickBars(symbol.toUpperCase(), interval, maxSize, null, null);

    this.candlesticksCache = new TreeMap<>();
    for (Candlestick candlestickBar : candlestickBars) {
      candlesticksCache.put(candlestickBar.getOpenTime(), candlestickBar);
    }
  }

  /**
   * Begins streaming of depth events.
   */
  private void startCandlestickEventStreaming(String symbol, CandlestickInterval interval) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiWebSocketClient client = factory.newWebSocketClient();

    client.onCandlestickEvent(symbol.toLowerCase(), interval, response -> {
      Long openTime = response.getOpenTime();
      Candlestick updateCandlestick = candlesticksCache.get(openTime);
      if (updateCandlestick == null) {
        // new candlestick
        updateCandlestick = new Candlestick();
      }
      // update candlestick with the stream data
      updateCandlestick.setOpenTime(response.getOpenTime());
      updateCandlestick.setOpen(response.getOpen());
      updateCandlestick.setLow(response.getLow());
      updateCandlestick.setHigh(response.getHigh());
      updateCandlestick.setClose(response.getClose());
      updateCandlestick.setCloseTime(response.getCloseTime());
      updateCandlestick.setVolume(response.getVolume());
      updateCandlestick.setNumberOfTrades(response.getNumberOfTrades());
      updateCandlestick.setQuoteAssetVolume(response.getQuoteAssetVolume());
      updateCandlestick.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
      updateCandlestick.setTakerBuyBaseAssetVolume(response.getTakerBuyQuoteAssetVolume());

      // Store the updated candlestick in the cache
      candlesticksCache.put(openTime, updateCandlestick);
//      System.out.println(updateCandlestick);
      
      if (candlesticksCache.size() > maxSize) {
    	  ArrayList<Long> keys = new ArrayList<Long>(candlesticksCache.keySet());
    	  candlesticksCache.remove( keys.get( keys.size()-1 ) );
      }
    });
  }

  /**
   * @return a klines/candlestick cache, containing the open/start time of the candlestick as the key,
   * and the candlestick data as the value.
   */
  public Map<Long, Candlestick> getCandlesticksCache() {
    return candlesticksCache;
  }
  
  /**
   * 
   * @param number
   * @return a list of klines/candlestick cache from latest candlestick, a list's size is number
   */
  public List<Candlestick> getLatestCandlesticksCacheSet(int number) {
	  ArrayList<Long> keys = new ArrayList<Long>(candlesticksCache.keySet());
	  List<Candlestick> candlesticks = new ArrayList<>();
	  for (int i=keys.size()-1, j=0; i>=0 && j<number; --i, ++j) {
		  candlesticks.add( candlesticksCache.get(keys.get(i)) );
	  }
	  return candlesticks;
  }

	public int getMaxSize() {
		return maxSize;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public CandlestickInterval getInterval() {
		return interval;
	}
	
	public void setInterval(CandlestickInterval interval) {
		this.interval = interval;
	}
  
  

//  public static void main(String[] args) {
//    new CandlesticksCache("ETHBTC", CandlestickInterval.ONE_MINUTE);
//  }
}
