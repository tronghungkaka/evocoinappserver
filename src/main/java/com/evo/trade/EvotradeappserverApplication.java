package com.evo.trade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.binance.api.client.domain.market.CandlestickInterval;
import com.evo.trade.binance.cache.AllMarketTickers;
import com.evo.trade.service.BinanceAllMarketTickersService;
import com.evo.trade.service.BinanceCandlesticksService;
import com.evo.trade.service.EvoBollingerBandService;
import com.evo.trade.service.EvoCoinmarketcapService;
import com.evo.trade.service.EvoPostgresqlService;

@SpringBootApplication
public class EvotradeappserverApplication {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SpringApplication.run(EvotradeappserverApplication.class, args);
		
		EvoPostgresqlService.getInstance().createPostgresqlDb();
		
//		EvoCoinmarketcapService evoCmcService = new EvoCoinmarketcapService();
//		evoCmcService.start();
		
		BinanceAllMarketTickersService bnbAllMarketTickersService = new BinanceAllMarketTickersService();
		bnbAllMarketTickersService.start();
		
		List<CandlestickInterval> candlestickIntervals = new ArrayList<>();
		candlestickIntervals.add(CandlestickInterval.FIVE_MINUTES);
		candlestickIntervals.add(CandlestickInterval.HALF_HOURLY);
		candlestickIntervals.add(CandlestickInterval.HOURLY);
		candlestickIntervals.add(CandlestickInterval.FOUR_HOURLY);
		candlestickIntervals.add(CandlestickInterval.DAILY);
		candlestickIntervals.add(CandlestickInterval.WEEKLY);
		BinanceCandlesticksService bnbCandlesticksService = new BinanceCandlesticksService(candlestickIntervals);
		bnbCandlesticksService.start();
	}
}
