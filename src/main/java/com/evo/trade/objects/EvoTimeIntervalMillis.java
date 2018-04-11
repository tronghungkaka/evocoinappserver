package com.evo.trade.objects;

public enum EvoTimeIntervalMillis {
	
	ONE_SECOND_MILLIS(1000L),
	ONE_MINUTE_MILLIS(60 * EvoTimeIntervalMillis.ONE_SECOND_MILLIS.intervalIdMillis),
	THREE_MINUTES_MILLIS(3 * EvoTimeIntervalMillis.ONE_MINUTE_MILLIS.intervalIdMillis),
	FIVE_MINUTES_MILLIS(5 * EvoTimeIntervalMillis.ONE_MINUTE_MILLIS.intervalIdMillis),
	FIFTEEN_MINUTES_MILLIS(3 * EvoTimeIntervalMillis.FIVE_MINUTES_MILLIS.intervalIdMillis),
	HALF_HOURLY_MILLIS(2 * EvoTimeIntervalMillis.FIFTEEN_MINUTES_MILLIS.intervalIdMillis),
	HOURLY_MILLIS(2 * EvoTimeIntervalMillis.HALF_HOURLY_MILLIS.intervalIdMillis),
	TWO_HOURLY_MILLIS(2 * EvoTimeIntervalMillis.HOURLY_MILLIS.intervalIdMillis),
	FOUR_HOURLY_MILLIS(2 * EvoTimeIntervalMillis.TWO_HOURLY_MILLIS.intervalIdMillis),
	SIX_HOURLY_MILLIS(3 * EvoTimeIntervalMillis.TWO_HOURLY_MILLIS.intervalIdMillis),
	EIGHT_HOURLY_MILLIS(2 * EvoTimeIntervalMillis.FOUR_HOURLY_MILLIS.intervalIdMillis),
	TWELVE_HOURLY_MILLIS(2 * EvoTimeIntervalMillis.SIX_HOURLY_MILLIS.intervalIdMillis),
	DAILY_MILLIS(2 * EvoTimeIntervalMillis.TWELVE_HOURLY_MILLIS.intervalIdMillis),
	THREE_DAILY_MILLIS(3 * EvoTimeIntervalMillis.DAILY_MILLIS.intervalIdMillis),
	WEEKLY_MILLIS(7 * EvoTimeIntervalMillis.DAILY_MILLIS.intervalIdMillis),
	MONTHLY_MILLIS(4 * EvoTimeIntervalMillis.WEEKLY_MILLIS.intervalIdMillis);
	

	private final Long intervalIdMillis;

	EvoTimeIntervalMillis(Long intervalIdMillis) {
		this.intervalIdMillis = intervalIdMillis;
	}

	public Long getIntervalIdMillis() {
		return intervalIdMillis;
	}

	public static EvoTimeIntervalMillis getEvoTimeInterval(Long intervalMillis) {
		EvoTimeIntervalMillis intervalId = EvoTimeIntervalMillis.ONE_SECOND_MILLIS;
		
		if (intervalMillis == EvoTimeIntervalMillis.ONE_SECOND_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.ONE_SECOND_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.ONE_MINUTE_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.ONE_MINUTE_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.THREE_MINUTES_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.THREE_MINUTES_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.FIVE_MINUTES_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.FIVE_MINUTES_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.FIFTEEN_MINUTES_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.FIFTEEN_MINUTES_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.HALF_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.HALF_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.TWO_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.TWO_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.FOUR_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.FOUR_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.SIX_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.SIX_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.EIGHT_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.EIGHT_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.TWELVE_HOURLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.TWELVE_HOURLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.DAILY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.DAILY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.THREE_DAILY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.THREE_DAILY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.WEEKLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.WEEKLY_MILLIS;
		else if (intervalMillis == EvoTimeIntervalMillis.MONTHLY_MILLIS.intervalIdMillis)
			intervalId = EvoTimeIntervalMillis.MONTHLY_MILLIS;
		return intervalId;
	}
}
