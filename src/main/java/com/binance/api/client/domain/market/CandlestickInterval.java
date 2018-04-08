package com.binance.api.client.domain.market;

/**
 * Kline/Candlestick intervals.
 * m -> minutes; h -> hours; d -> days; w -> weeks; M -> months
 */
public enum CandlestickInterval {
  ONE_MINUTE("1m"),
  THREE_MINUTES("3m"),
  FIVE_MINUTES("5m"),
  FIFTEEN_MINUTES("15m"),
  HALF_HOURLY("30m"),
  HOURLY("1h"),
  TWO_HOURLY("2h"),
  FOUR_HOURLY("4h"),
  SIX_HOURLY("6h"),
  EIGHT_HOURLY("8h"),
  TWELVE_HOURLY("12h"),
  DAILY("1d"),
  THREE_DAILY("3d"),
  WEEKLY("1w"),
  MONTHLY("1M");

  private final String intervalId;

  CandlestickInterval(String intervalId) {
    this.intervalId = intervalId;
  }

  public String getIntervalId() {
    return intervalId;
  }
  
  public static CandlestickInterval getCandlestickInterval(String interval) {
	  CandlestickInterval intervalId = CandlestickInterval.FIVE_MINUTES;
		if (interval.equals(CandlestickInterval.ONE_MINUTE.toString()))
			intervalId = CandlestickInterval.ONE_MINUTE;
		else if (interval.equals(CandlestickInterval.THREE_MINUTES.toString()))
			intervalId = CandlestickInterval.THREE_MINUTES;
		else if (interval.equals(CandlestickInterval.THREE_MINUTES.toString()))
			intervalId = CandlestickInterval.THREE_MINUTES;
		else if (interval.equals(CandlestickInterval.FIFTEEN_MINUTES.toString()))
			intervalId = CandlestickInterval.FIFTEEN_MINUTES;
		else if (interval.equals(CandlestickInterval.HALF_HOURLY.toString()))
			intervalId = CandlestickInterval.HALF_HOURLY;
		else if (interval.equals(CandlestickInterval.HOURLY.toString()))
			intervalId = CandlestickInterval.HOURLY;
		else if (interval.equals(CandlestickInterval.TWO_HOURLY.toString()))
			intervalId = CandlestickInterval.TWO_HOURLY;
		else if (interval.equals(CandlestickInterval.FOUR_HOURLY.toString()))
			intervalId = CandlestickInterval.FOUR_HOURLY;
		else if (interval.equals(CandlestickInterval.SIX_HOURLY.toString()))
			intervalId = CandlestickInterval.SIX_HOURLY;
		else if (interval.equals(CandlestickInterval.EIGHT_HOURLY.toString()))
			intervalId = CandlestickInterval.EIGHT_HOURLY;
		else if (interval.equals(CandlestickInterval.TWELVE_HOURLY.toString()))
			intervalId = CandlestickInterval.TWELVE_HOURLY;
		else if (interval.equals(CandlestickInterval.DAILY.toString()))
			intervalId = CandlestickInterval.DAILY;
		else if (interval.equals(CandlestickInterval.THREE_DAILY.toString()))
			intervalId = CandlestickInterval.THREE_DAILY;
		else if (interval.equals(CandlestickInterval.WEEKLY.toString()))
			intervalId = CandlestickInterval.WEEKLY;
		else if (interval.equals(CandlestickInterval.MONTHLY.toString()))
			intervalId = CandlestickInterval.MONTHLY;
		return intervalId;
  }
}
