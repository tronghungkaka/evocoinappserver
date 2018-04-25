package com.evo.trade.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dominance {
	@JsonProperty("symbol") private String symbol;
	@JsonProperty("usdPrice") private String usdPrice;
	@JsonProperty("baseCurrencyPrice") private String baseCurrencyPrice;
	@JsonProperty("_24hr_quote_volume") private String _24hr_quote_volume;
	@JsonProperty("excess") private List<Excess> excess;
	@JsonProperty("rsi") private List<Double> rsi;
	
	@JsonIgnore private static String rsiCandlesticks = "4h"; 
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	public String get_24hr_quote_volume() {
		return _24hr_quote_volume;
	}

	public void set_24hr_quote_volume(String _24hr_quote_volume) {
		this._24hr_quote_volume = _24hr_quote_volume;
	}

	public List<Excess> getExcess() {
		return excess;
	}

	public void setExcess(List<Excess> excess) {
		this.excess = excess;
	}

	public List<Double> getRsi() {
		return rsi;
	}

	public void setRsi(List<Double> rsi) {
		this.rsi = rsi;
	}

	public static class Excess {
		@JsonProperty("date") private String date;
		@JsonProperty("value") private String value;
		@JsonIgnore private static int dayNumber = 15;
		@JsonIgnore private static String excessCandlestick = "1h";
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
