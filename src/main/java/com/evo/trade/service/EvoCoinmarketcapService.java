package com.evo.trade.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.coinmarketcap.api.client.impl.CoinmarketcapApiService;
import com.coinmarketcap.api.objects.CoinmarketcapTickerPrice;
import com.evo.trade.objects.EvoTimeIntervalMillis;

public class EvoCoinmarketcapService implements Runnable{
	
	// Thread implements
	protected Thread thread;
	protected String threadName;
	protected boolean threadSuspended = false;
	protected long interval; // milliseconds
	
	public void suspend() {
		threadSuspended = true;
		System.out.println(threadName + " suspended");
	}
	
	public synchronized void resume() {
		threadSuspended = false;
		notify();
		System.out.println(threadName + " resumed");
	}
	
	public synchronized void stop() {
		thread = null;
		notify();
		System.out.println(threadName + " stoped");
	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
		System.out.println(threadName + " started");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thisThread = Thread.currentThread();
		while (thread == thisThread) {
			try {
				List<CoinmarketcapTickerPrice> cmcTickerPrices = CoinmarketcapApiService.getCoinmarketcapTickerPrices(0, this.coinCnt, null);
				for (int i=0; i<cmcTickerPrices.size(); ++i) {
					map.put(cmcTickerPrices.get(i).getSymbol(), cmcTickerPrices.get(i));
				}
				
				Thread.sleep(interval);
				synchronized (this) {
					while (threadSuspended && thread == thisThread)
						wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// END: Thread implements
	
	private static Map<String, CoinmarketcapTickerPrice> map;
	private int coinCnt;
	
	public EvoCoinmarketcapService() {
		this.threadName = "Evo Coinmarket Cap Service";
		this.interval = EvoTimeIntervalMillis.FIVE_MINUTES_MILLIS.getIntervalIdMillis();
		this.coinCnt = 50; // coin market cap top 50
		map = new TreeMap<>();
	}
	
	public EvoCoinmarketcapService(int coinCnt, String threadName, long interval) {
		this.threadName = threadName;
		this.interval = interval;
		this.coinCnt = coinCnt;
		map = new TreeMap<>();
	}
	
	public static CoinmarketcapTickerPrice getCmcTickerPrice(String cmcCoinId) {
		return map.get(cmcCoinId);
	}
}
