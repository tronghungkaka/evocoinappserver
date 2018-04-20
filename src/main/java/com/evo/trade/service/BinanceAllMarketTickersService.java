package com.evo.trade.service;

import java.io.IOException;

import com.evo.trade.binance.cache.AllMarketTickers;
import com.evo.trade.objects.EvoTimeIntervalMillis;

public class BinanceAllMarketTickersService implements Runnable{
	// thread implements
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
			System.out.println(threadName + " started");
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thisThread = Thread.currentThread();
		while (thread == thisThread) {
			try {
				
				if (allMarketTickers != null) {
					allMarketTickers.close();
				}

				allMarketTickers = new AllMarketTickers();
				
				Thread.sleep(interval);
				synchronized (this) {
					while (threadSuspended && thread == thisThread)
						wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	// END: thread implements
	
	static AllMarketTickers allMarketTickers;
	
	public BinanceAllMarketTickersService() {
		this.threadName = "Binance All Market Tickers Service";
		this.interval = EvoTimeIntervalMillis.DAILY_MILLIS.getIntervalIdMillis();
	}
	
	public BinanceAllMarketTickersService(String threadName, long interval) {
		this.threadName = threadName;
		this.interval = interval;
	}
	
	public static AllMarketTickers getAllMarketTickers() {
		return allMarketTickers;
	}
}
