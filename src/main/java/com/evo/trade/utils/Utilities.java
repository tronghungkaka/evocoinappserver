package com.evo.trade.utils;

import java.util.List;

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
}
