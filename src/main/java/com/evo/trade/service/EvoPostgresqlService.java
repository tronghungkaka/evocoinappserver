package com.evo.trade.service;

import java.sql.SQLException;

import com.evo.trade.dao.EvoUserDao;

public class EvoPostgresqlService {
	// Singleton design pattern
	private static EvoPostgresqlService instance = null;
	private EvoPostgresqlService() {
		
	}
	public static EvoPostgresqlService getInstance() {
		if (instance == null)
			instance = new EvoPostgresqlService();
		
		return instance;
	}
	
	public void createPostgresqlDb() throws ClassNotFoundException, SQLException {
		EvoUserDao.getInstance().createUserTable();
	}
}
