package com.evo.trade.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.binance.api.client.domain.market.CandlestickInterval;
import com.evo.trade.objects.BollingerBand;
import com.evo.trade.service.EvoBollingerBandService;

public class EvoBollingerBandDao extends ConfigDao {
	// Singleton design pattern
	private static EvoBollingerBandDao instance = null;

	private EvoBollingerBandDao() {
		// TODO Auto-generated constructor stub
	}

	public static EvoBollingerBandDao getInstance() {
		if (instance == null) {
			instance = new EvoBollingerBandDao();
		}
		return instance;
	}
	
	//
	private static String filepath = "/" + EvoBollingerBandDao.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	
	private static List<BollingerBand> binanceBollingerBand5m = new ArrayList<>();
	private static List<BollingerBand> binanceBollingerBand30m = new ArrayList<>();
	private static List<BollingerBand> binanceBollingerBand1h = new ArrayList<>();
	private static List<BollingerBand> binanceBollingerBand4h = new ArrayList<>();
	private static List<BollingerBand> binanceBollingerBand1d = new ArrayList<>();
	private static List<BollingerBand> binanceBollingerBand1w = new ArrayList<>();

	public List<BollingerBand> getBollingerBands() {
		List<BollingerBand> bollingerbands = new ArrayList<>();
		switch (database) {
		case PROPERTY:
			for (String candlestickInterval : BollingerBand.CANDLESTICK_INTERVALS) {
				bollingerbands.addAll( getBollingerBands("BinanceBollingerBand" + candlestickInterval + ".dat") );
			}
			break;
		case FILE:
			for (String candlestickInterval : BollingerBand.CANDLESTICK_INTERVALS) {
				bollingerbands.addAll( getBollingerBands("BinanceBollingerBand" + candlestickInterval + ".dat") );
			}
			break;

		case MYSQL:
			break;
		}

		return bollingerbands;
	}
	
	public List<BollingerBand> getBollingerBands(String exchange, String interval) {
		List<BollingerBand> bollingerbands = new ArrayList<>();
		switch (database) {
		case PROPERTY:
			CandlestickInterval intervalId = CandlestickInterval.getCandlestickInterval(interval);
			bollingerbands = EvoBollingerBandService.getBollingerBands(intervalId);
			break;
		case FILE:
			for (String candlestickInterval : BollingerBand.CANDLESTICK_INTERVALS) {
				bollingerbands.addAll( getBollingerBands("BinanceBollingerBand" + candlestickInterval + ".dat") );
			}
			break;

		case MYSQL:
			break;
		}

		return bollingerbands;
	}


	public List<BollingerBand> getBollingerBands(String fileName) {
		List<BollingerBand> bollingerbands = new ArrayList<>();
		switch (database) {
		case PROPERTY:
			if(fileName.equals("BinanceBollingerBand5m.dat")) {
				bollingerbands = binanceBollingerBand5m;
				System.out.println(fileName + ": " + bollingerbands);
			}
			else if(fileName.equals("BinanceBollingerBand30m.dat")) {
				bollingerbands = binanceBollingerBand30m;
				System.out.println(fileName + ": " + bollingerbands);
			}
			else if(fileName.equals("BinanceBollingerBand1h.dat")) {
				bollingerbands = binanceBollingerBand1h;
				System.out.println(fileName + ": " + bollingerbands);
			}
			else if(fileName.equals("BinanceBollingerBand4h.dat")) {
				bollingerbands = binanceBollingerBand4h;
				System.out.println(fileName + ": " + bollingerbands);
			}
			else if(fileName.equals("BinanceBollingerBand1d.dat")) {
				bollingerbands = binanceBollingerBand1d;
				System.out.println(fileName + ": " + bollingerbands);
			}
			else if(fileName.equals("BinanceBollingerBand1w.dat")) {
				bollingerbands = binanceBollingerBand1w;
				System.out.println(fileName + ": " + bollingerbands);
			}
			break;
		case FILE:
			try {
				File file = new File(filepath + fileName);
				if (!file.exists()) {
					break;
				}
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				bollingerbands = (List<BollingerBand>) ois.readObject();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case MYSQL:
			break;
		}

		return bollingerbands;
	}

	public void saveBollingerBands(List<BollingerBand> bollingerbands) {
		switch (database) {
		case PROPERTY:
			
			break;
		case FILE:
			try {
				File file = new File(filepath + "Bollingerband.dat");
				System.out.println("1 saveBollingerBans - path: " + filepath + "Bollingerband.dat");
				FileOutputStream fos;
				fos = new FileOutputStream(file);
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(fos);
				oos.writeObject(bollingerbands);
				oos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case MYSQL:
			break;
		}
	}

	public void saveBollingerBands(String fileName, List<BollingerBand> bollingerbands) {
		switch (database) {
		case PROPERTY:

			if(fileName.equals("BinanceBollingerBand5m.dat")) {
				binanceBollingerBand5m = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand5m);
			}
			else if(fileName.equals("BinanceBollingerBand30m.dat")) {
				binanceBollingerBand30m = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand30m);
			}
			else if(fileName.equals("BinanceBollingerBand1h.dat")) {
				binanceBollingerBand1h = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand1h);
			}
			else if(fileName.equals("BinanceBollingerBand4h.dat")) {
				binanceBollingerBand4h = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand4h);
			}
			else if(fileName.equals("BinanceBollingerBand1d.dat")) {
				binanceBollingerBand1d = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand1d);
			}
			else if(fileName.equals("BinanceBollingerBand1w.dat")) {
				binanceBollingerBand1w = bollingerbands;
				System.out.println("save - " + fileName + ": " + binanceBollingerBand1w);
			}
			break;
		case FILE:
			try {
				File file = new File(filepath + fileName);
				System.out.println("2 saveBollingerBands - path:" + filepath + fileName);
				FileOutputStream fos;
				fos = new FileOutputStream(file);
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(fos);
				oos.writeObject(bollingerbands);
				oos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case MYSQL:
			break;
		}
	}
}
