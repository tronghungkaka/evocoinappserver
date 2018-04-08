package com.evo.trade.objects;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BollingerBand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("exchange") String exchange;
	@JsonProperty("symbol") String symbol;
	@JsonProperty("currencySymbol") String currencySymbol;
	@JsonProperty("baseCurrencySymbol") String baseCurrencySymbol;
	@JsonProperty("lastPrice") Double lastPrice;
	@JsonProperty("upperBB") Double upperBB;
	@JsonProperty("sma") Double sma;
	@JsonProperty("lowerBB") Double lowerBB;
	@JsonProperty("percentage") Double percentage;
	@JsonProperty("comparativePercentage") Double comparativePercentage;
	@JsonProperty("interval") String interval;
	@JsonProperty("timestamp") Long timestamp;
	@JsonProperty("price_usd") Double usdPrice;
	@JsonProperty("price_base_currency") Double baseCurrencyPrice;
//	@JsonProperty("1w_high_price") 
	Double _1wHighPrice;
//	@JsonProperty("1w_low_price") 
	Double _1wLowPrice;
//	@JsonProperty("1M_high_price") 
	Double _1MHighPrice;
//	@JsonProperty("1M_low_pirce") 
	Double _1MLowPrice;
//	@JsonProperty("10_period_avg_volume") 
	Double _10PeriodAVGVolume;
	
	Double _24hr_volume;
	Double _24hr_quote_volume;


	public static String[] CANDLESTICK_INTERVALS = {
			"5m",
			"30m",
			"1h",
			"4h",
			"1d",
			"1w"
	};
	
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	@JsonGetter("_1w_high_price")
	public Double get_1wHighPrice() {
		return _1wHighPrice;
	}

	public void set_1wHighPrice(Double _1wHighPrice) {
		this._1wHighPrice = _1wHighPrice;
	}

	@JsonGetter("_1w_low_price")
	public Double get_1wLowPrice() {
		return _1wLowPrice;
	}

	public void set_1wLowPrice(Double _1wLowPrice) {
		this._1wLowPrice = _1wLowPrice;
	}

	@JsonGetter("_1M_high_price")
	public Double get_1MHighPrice() {
		return _1MHighPrice;
	}

	public void set_1MHighPrice(Double _1mHighPrice) {
		_1MHighPrice = _1mHighPrice;
	}

	@JsonGetter("_1M_low_price")
	public Double get_1MLowPrice() {
		return _1MLowPrice;
	}

	public void set_1MLowPrice(Double _1mLowPrice) {
		_1MLowPrice = _1mLowPrice;
	}

	public static Long NUMBER_OF_CANDLESTICK_BARS = 20L;
	public static Long FACTOR = 2L;
	
	private Long SHATOSHI = 100000000L; // 1 btc = 10^8 sts
	
	private Long stsLastPrice;
	private Long stsUpperBB;
	private Long stsSma;
	private Long stsLowerBB;
	
	public BollingerBand() {
		
	}
	
	public BollingerBand(String exchange, Double lastPrice) {
//		setInterval(interval);
		setExchange(exchange);
//		setSymbol(symbol);
		setLastPrice(lastPrice);
	}
	
	public BollingerBand(String exchange, String symbol, String interval, Double lastPrice) {
		setInterval(interval);
		setExchange(exchange);
		setSymbol(symbol);
		setLastPrice(lastPrice);
	}
	
	public boolean isOutOfUpperBollingerBand() {
		return lastPrice > upperBB;
	}
	
	public boolean isOutOfLowerBollingerBand() {
		return lastPrice < lowerBB;
	}
	
	public boolean isOutOfBands() {
		return isOutOfLowerBollingerBand() || isOutOfUpperBollingerBand();
	}
	
	public Double getPercentage() {
		if(percentage != null)
			return percentage;
		else if(isOutOfLowerBollingerBand())
			percentage = (double)(stsLowerBB - stsLastPrice) / stsLowerBB;
		else if(isOutOfUpperBollingerBand())
			percentage = (double)(stsLastPrice - stsUpperBB) / stsUpperBB;
		percentage *= 100.;
		return percentage;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getBaseCurrencySymbol() {
		return baseCurrencySymbol;
	}

	public void setBaseCurrencySymbol(String baseCurrencySymbol) {
		this.baseCurrencySymbol = baseCurrencySymbol;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
		this.stsLastPrice = (long)(lastPrice * SHATOSHI);
		this.percentage = null;
		this.comparativePercentage = null;
	}

	public Double getSma() {
		return sma;
	}

	public void setSma(Double sma) {
		this.sma = sma;
		this.stsSma = (long)(sma * SHATOSHI);
		this.percentage = null;
		this.comparativePercentage = null;
	}

	public Double getLowerBB() {
		return lowerBB;
	}

	public void setLowerBB(Double lowerBB) {
		this.lowerBB = lowerBB;
		this.stsLowerBB = (long)(lowerBB * SHATOSHI);
		this.percentage = null;
		this.comparativePercentage = null;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
		if(symbol.endsWith("BTC")) {
			baseCurrencySymbol = "BTC";
			currencySymbol = symbol.substring(0, symbol.length()-baseCurrencySymbol.length());
		}
		else if(symbol.endsWith("ETH")) {
			baseCurrencySymbol = "ETH";
			currencySymbol = symbol.substring(0, symbol.length()-baseCurrencySymbol.length());
		}
		else if(symbol.endsWith("BNB")) {
			baseCurrencySymbol = "BNB";
			currencySymbol = symbol.substring(0, symbol.length()-baseCurrencySymbol.length());
		}
		else if(symbol.endsWith("USDT")) {
			baseCurrencySymbol = "USDT";
			currencySymbol = symbol.substring(0, symbol.length()-baseCurrencySymbol.length());
		}
	}

	public Double getUpperBB() {
		return upperBB;
	}

	public void setUpperBB(Double upperBB) {
		this.upperBB = upperBB;
		this.stsUpperBB = (long)(upperBB * SHATOSHI);
		this.percentage = null;
		this.comparativePercentage = null;
	}

	public Double getComparativePercentage() {
		if(comparativePercentage != null) {
			return comparativePercentage;
		}
		if(isOutOfLowerBollingerBand()) {
			comparativePercentage = (double)(stsLowerBB - stsLastPrice) / (stsSma - stsLowerBB);
		}
		if(isOutOfUpperBollingerBand()) {
			comparativePercentage = (double)(stsLastPrice - stsUpperBB) / (stsUpperBB - stsSma);
		}
		comparativePercentage *= 100;
		return comparativePercentage;
		
	}

	public void setComparativePercentage(Double comparativePercentage) {
		this.comparativePercentage = comparativePercentage;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public Double getUsdPrice() {
		return usdPrice;
	}

	public void setUsdPrice(Double usdPrice) {
		this.usdPrice = usdPrice;
	}

	public Double getBaseCurrencyPrice() {
		return baseCurrencyPrice;
	}

	public void setBaseCurrencyPrice(Double baseCurrencyPrice) {
		this.baseCurrencyPrice = baseCurrencyPrice;
	}

	@JsonGetter("_10_period_AVG_volume")
	public Double get_10PeriodAVGVolume() {
		return _10PeriodAVGVolume;
	}

	public void set_10PeriodAVGVolume(Double _10PeriodAVGVolume) {
		this._10PeriodAVGVolume = _10PeriodAVGVolume;
	}

	@JsonGetter("_24hr_volume")
	public Double get_24hr_volume() {
		return _24hr_volume;
	}

	public void set_24hr_volume(Double _24hr_volume) {
		this._24hr_volume = _24hr_volume;
	}
	
	@JsonGetter("_24hr_quote_volume")
	public Double get_24hr_quote_volume() {
		return _24hr_quote_volume;
	}

	public void set_24hr_quote_volume(Double _24hr_quote_volume) {
		this._24hr_quote_volume = _24hr_quote_volume;
	}
}
