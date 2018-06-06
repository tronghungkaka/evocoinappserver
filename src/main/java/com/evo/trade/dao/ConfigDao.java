package com.evo.trade.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDao {
	public static final int PROPERTY = 0;
	public static final int FILE = 1;
	public static final int MYSQL = 2;
	
	public static int database = PROPERTY;
	
	public ConfigDao() {
		// TODO Auto-generated constructor stub
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
		
		// only for local test
//		Class.forName("com.mysql.jdbc.Driver");
//		return DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
	}
}
