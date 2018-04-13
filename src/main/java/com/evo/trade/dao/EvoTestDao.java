package com.evo.trade.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class EvoTestDao extends ConfigDao {

	// Singleton design pattern
	private static EvoTestDao instance = null;
	private EvoTestDao() {
		super();
	}
	public static EvoTestDao getInstance() {
		if (instance == null) {
			instance = new EvoTestDao();
		}
		return instance;
	}
	
	
	public Timestamp getTick() throws SQLException, ClassNotFoundException {
        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        Timestamp tick = null;
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
            tick = rs.getTimestamp("tick");
        }
        return tick;
    }
}
