package com.evo.trade;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinmarketcap.api.client.impl.CoinmarketcapApiService;
import com.evo.trade.HomeController.BollingerBandResponse;
import com.evo.trade.HomeController.Sortbypercentage;
import com.evo.trade.dao.EvoBollingerBandDao;
import com.evo.trade.dao.EvoTestDao;
import com.evo.trade.dao.EvoUserDao;
import com.evo.trade.objects.BollingerBand;
import com.evo.trade.objects.Dominance;
import com.evo.trade.objects.User;
import com.evo.trade.service.EvoDominanceService;
import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://evocoinapp.herokuapp.com", "https://coin.evommo.com", "https://app.evommo.com"})
public class HomeController {

	@RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
	
	/**
	 * EVOlution
	 */
	@RequestMapping(value = "/api/evo/tick", method = RequestMethod.GET, produces = "application/json")
	public Timestamp getTick() throws SQLException, ClassNotFoundException {
		return EvoTestDao.getInstance().getTick();
	}
	
	@RequestMapping(value = "/api/evo/timestamp", method = RequestMethod.GET, produces = "application/json")
	public Timestamp getTimestamp() {
		Timestamp timestamp = new Timestamp( System.currentTimeMillis() );
		return timestamp;
	}
	
	@RequestMapping(value = "/api/evo/users", method = RequestMethod.GET, produces = "application/json")
	public List<User> getAllUsers() throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getAllUsers();
	}

	@RequestMapping(value = "/api/evo/users/{id}", method = RequestMethod.GET, produces = "application/json")
	public User getUser(@PathVariable("id") int id) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getUser(id);
	}
	
	@RequestMapping(value = "/api/evo/users", method = RequestMethod.POST, produces = "application/json")
	public boolean createUser(@RequestBody User user) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().createUser(user);
	}
	
	@RequestMapping(value = "/api/evo/users/{id}", method = RequestMethod.PUT, produces = "application/json")
	public boolean updateUser(@PathVariable("id") int id,
			@RequestBody User user) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().updateUser(id, user);
	}
	
	@RequestMapping(value = "/api/evo/users/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public boolean deleteUser(@PathVariable("id") int id) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().deleteUser(id);
	}
	
	@RequestMapping(value = "/api/evo/authenticate", method = RequestMethod.POST, produces = "application/json")
	public User authenticateUser(@RequestBody  User user) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().authenticateUser(user);
	}
	
	class BollingerBandResponse {
		public @JsonProperty("LastUpdated") Long lastUpdated;
    	public @JsonProperty("OutOfUpperBB") List<BollingerBand> ooUpperBBs;
    	public @JsonProperty("OutOfLowerBB") List<BollingerBand> ooLowerBBs;
    	
    	public BollingerBandResponse() {
    		ooUpperBBs = new ArrayList<>();
    		ooLowerBBs = new ArrayList<>();
    	}
    }

    class Sortbypercentage implements Comparator<BollingerBand>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(BollingerBand a, BollingerBand b)
        {
        	Double avalue = a.getPercentage();
        	Double bvalue = b.getPercentage();
            if(avalue > bvalue)
            	return -1;
            if(avalue < bvalue)
            	return 1;
            return 0;
        }
    }
    
	@RequestMapping(value = "/api/evo/bollingerband", method = RequestMethod.GET, produces = "application/json")
	public BollingerBandResponse getEvoBollingerBands(
			@RequestParam("exchange") String exchange,
			@RequestParam("interval") String interval) {
		BollingerBandResponse bbres = new BollingerBandResponse();
		List<BollingerBand> bollingerbands = EvoBollingerBandDao.getInstance().getBollingerBands(exchange, interval);
		if(bollingerbands == null)
			return bbres;
		if(bollingerbands != null && !bollingerbands.isEmpty()) {
			bbres.lastUpdated = bollingerbands.get(0).getTimestamp();
		}
		else {
			bbres.lastUpdated = System.currentTimeMillis();
		}
//		if(!exchange.equalsIgnoreCase("all")) {
//			bollingerbands = bollingerbands.stream()
//					.filter( bb -> bb.getExchange().equalsIgnoreCase(exchange))
//					.collect(Collectors.toList());
//		}
//		bollingerbands = bollingerbands.stream()
//				.filter( bb -> bb.getInterval().equals(interval))
//				.collect(Collectors.toList());
		bbres.ooLowerBBs = bollingerbands.stream()
					.filter( bb -> bb.isOutOfLowerBollingerBand())
					.collect(Collectors.toList());
		bbres.ooUpperBBs = bollingerbands.stream()
					.filter( bb -> bb.isOutOfUpperBollingerBand())
					.collect(Collectors.toList());
		
//		for(BollingerBand bb : bollingerbands) {
//    		if(bb.isOutOfLowerBollingerBand())
//    			bbres.ooLowerBBs.add(bb);
//    		else if(bb.isOutOfUpperBollingerBand())
//    			bbres.ooUpperBBs.add(bb);
//		}
    	Collections.sort(bbres.ooLowerBBs, new Sortbypercentage());
    	Collections.sort(bbres.ooUpperBBs, new Sortbypercentage());
		return bbres;
		
	}
	
	@RequestMapping(value = "/api/evo/dominance", method = RequestMethod.GET, produces = "application/json")
	public List<Dominance> getDominances(
			@RequestParam("exchange") String exchange) {
		List<Dominance> dominances = EvoDominanceService.getInstance().getDominances();
		
		return dominances;
	}
	
	/**
     * Coinmarketcap
     */
    @RequestMapping(value = "/api/coinmarketcap/v1/ticker", method = RequestMethod.GET, produces = "application/json")
    public String getCoinmarketcapTicker(@RequestParam(value="start", defaultValue="0") Integer start,
    		@RequestParam(value="limit", defaultValue="100") Integer limit,
    		@RequestParam(value="convert", defaultValue="") String convert) {
    	return CoinmarketcapApiService.getTicker(start, limit, convert);
    }
    
    @RequestMapping(value = "/api/coinmarketcap/v1/ticker/{id}", method = RequestMethod.GET, produces = "application/json")
    public String getCoinmarketcapTicker(@PathVariable("id") String id,
    		@RequestParam(value="convert", defaultValue="") String convert) {
    	return CoinmarketcapApiService.getTicker(id, convert);
    }
    
    @RequestMapping(value = "/api/coinmarketcap/v1/global", method = RequestMethod.GET, produces = "application/json")
    public String getCoinmarketcapGlobalData(@RequestParam(value = "convert", defaultValue = "") String convert) {
    	return CoinmarketcapApiService.getGlobalData(convert);
    }
    
    /** End: Coinmarketcap */
}
