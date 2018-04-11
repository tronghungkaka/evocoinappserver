package com.evo.trade.objects;

/**
 * Time Interval
 * @author EVOTEAM
 * m -> minutes; h -> hours; d -> days; w -> weeks; M -> months
 */
public enum EvoTimeInterval {
	ONE_SECOND("1s"),
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
	
	EvoTimeInterval(String intervalId) {
		this.intervalId = intervalId;
	}
	
  	public String getIntervalId() {
		return intervalId;
	}
  	
	
	public static EvoTimeInterval getEvoTimeInterval(String interval) {
		EvoTimeInterval intervalId = EvoTimeInterval.ONE_SECOND;

		if (interval.equals(EvoTimeInterval.ONE_SECOND.intervalId))
			intervalId = EvoTimeInterval.ONE_SECOND;
		else if (interval.equals(EvoTimeInterval.ONE_MINUTE.intervalId))
			intervalId = EvoTimeInterval.ONE_MINUTE;
		else if (interval.equals(EvoTimeInterval.THREE_MINUTES.intervalId))
			intervalId = EvoTimeInterval.THREE_MINUTES;
		else if (interval.equals(EvoTimeInterval.FIVE_MINUTES.intervalId))
			intervalId = EvoTimeInterval.FIVE_MINUTES;
		else if (interval.equals(EvoTimeInterval.FIFTEEN_MINUTES.intervalId))
			intervalId = EvoTimeInterval.FIFTEEN_MINUTES;
		else if (interval.equals(EvoTimeInterval.HALF_HOURLY.intervalId))
			intervalId = EvoTimeInterval.HALF_HOURLY;
		else if (interval.equals(EvoTimeInterval.HOURLY.intervalId))
			intervalId = EvoTimeInterval.HOURLY;
		else if (interval.equals(EvoTimeInterval.TWO_HOURLY.intervalId))
			intervalId = EvoTimeInterval.TWO_HOURLY;
		else if (interval.equals(EvoTimeInterval.FOUR_HOURLY.intervalId))
			intervalId = EvoTimeInterval.FOUR_HOURLY;
		else if (interval.equals(EvoTimeInterval.SIX_HOURLY.intervalId))
			intervalId = EvoTimeInterval.SIX_HOURLY;
		else if (interval.equals(EvoTimeInterval.EIGHT_HOURLY.intervalId))
			intervalId = EvoTimeInterval.EIGHT_HOURLY;
		else if (interval.equals(EvoTimeInterval.TWELVE_HOURLY.intervalId))
			intervalId = EvoTimeInterval.TWELVE_HOURLY;
		else if (interval.equals(EvoTimeInterval.DAILY.intervalId))
			intervalId = EvoTimeInterval.DAILY;
		else if (interval.equals(EvoTimeInterval.THREE_DAILY.intervalId))
			intervalId = EvoTimeInterval.THREE_DAILY;
		else if (interval.equals(EvoTimeInterval.WEEKLY.intervalId))
			intervalId = EvoTimeInterval.WEEKLY;
		else if (interval.equals(EvoTimeInterval.MONTHLY.intervalId))
			intervalId = EvoTimeInterval.MONTHLY;
		return intervalId;
	}
}
