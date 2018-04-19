package com.evo.trade.objects;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamingBollingerBand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("e") String exchange;
	@JsonProperty("s") String symbol;
	@JsonProperty("p") String percentage;
	@JsonProperty("u") String usdPrice;
	@JsonProperty("b") String baseCurrencyPrice;
	@JsonProperty("v") String _24hVolume;
	@JsonProperty("q") String _24hQuoteVolume;
	@JsonProperty("U") String isOutOfUpperBB;
	@JsonProperty("L") String isOutOfLowerBB;
	
	public StreamingBollingerBand() {
		
	}
	
	public String get_24hQuoteVolume() {
		return _24hQuoteVolume;
	}

	public void set_24hQuoteVolume(String _24hQuoteVolume) {
		this._24hQuoteVolume = _24hQuoteVolume;
	}

	public String get_24hVolume() {
		return _24hVolume;
	}

	public void set_24hVolume(String _24hVolume) {
		this._24hVolume = _24hVolume;
	}

	public String getIsOutOfUpperBB() {
		return isOutOfUpperBB;
	}

	public void setIsOutOfUpperBB(String isOutOfUpperBB) {
		this.isOutOfUpperBB = isOutOfUpperBB;
	}

	public String getIsOutOfLowerBB() {
		return isOutOfLowerBB;
	}

	public void setIsOutOfLowerBB(String isOutOfLowerBB) {
		this.isOutOfLowerBB = isOutOfLowerBB;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getUsdPrice() {
		return usdPrice;
	}
	public void setUsdPrice(String usdPrice) {
		this.usdPrice = usdPrice;
	}
	public String getBaseCurrencyPrice() {
		return baseCurrencyPrice;
	}
	public void setBaseCurrencyPrice(String baseCurrencyPrice) {
		this.baseCurrencyPrice = baseCurrencyPrice;
	}
	
	
}
