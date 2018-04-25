package com.evo.trade.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.binance.api.client.domain.market.Candlestick;
import com.evo.trade.objects.BollingerBand;

public class Utilities {
	public static Double average(List<Double> arr) {
		if(arr.isEmpty())
			return null;
		Double average = null;
		Double sum = 0.;
		for(Double obj : arr) {
			sum += obj;
		}
		average = sum / arr.size();
		return average;
	}
	
	public static Double stdDeviation(List<Double> arr) {
		if(arr.isEmpty())
			return null;
		
		Double average = Utilities.average(arr);
		Double tmp = 0.;
		for(Double obj : arr) {
			tmp += (obj - average) * (obj - average);
		}
		tmp /= arr.size();
		return Math.sqrt(tmp);
	}
	
	public static BollingerBand calcBollingerBand(List<Candlestick> candlestickBars) {
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
	
	public static boolean isTimestampOfTheDay(long timestamp, Calendar calendar) {
		Calendar timeToCheck = Calendar.getInstance();
		timeToCheck.setTimeInMillis(timestamp);
		
		if (calendar.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}
	
	public static List<Double> calcRSI(List<Double> closePrices) {
		List<Double> rsis = new ArrayList<>();
		int j=0;
		Double avgGain = 0.;
		Double avgLoss = 0.;
		for (int i=closePrices.size()-2; i>=0; --i) {
			Double change = closePrices.get(i) - closePrices.get(i+1);
			
			if (j<14) {
				if (change > 0) {
					avgGain += change;
				}
				else {
					avgLoss += -change;
				}
				++j;
				continue;
			}
			
			if (j==14) {
				avgGain = avgGain / 14.;
				avgLoss = avgLoss / 14.;
				++j;
			}
			else {
				Double currentGain = change > 0. ? change : 0.;
				Double currentLoss = change > 0. ? 0. : -change;
				avgGain = ((avgGain * 13.) + currentGain) / 14.;
				avgLoss = ((avgLoss * 13.) + currentLoss) / 14.;
			}
			
			if (avgLoss == 0.) {
				rsis.add(100.);
				continue;
			}

			Double rs = avgGain / avgLoss;
			Double rsi = 100. - 100. / (1. + rs);
			
			rsis.add(rsi);
		}
		return rsis;
	}
}
