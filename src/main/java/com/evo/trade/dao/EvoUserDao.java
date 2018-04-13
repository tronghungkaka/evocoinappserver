package com.evo.trade.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.evo.trade.objects.User;

public class EvoUserDao extends ConfigDao {
	// Singleton design pattern
	private static EvoUserDao instance = null;
	private EvoUserDao() {
		super();
	}
	public static EvoUserDao getInstance() {
		if (instance == null) {
			instance = new EvoUserDao();
		}
		return instance;
	}
	
	public boolean createUserTable() throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "
				+ "users (id SERIAL NOT NULL PRIMARY KEY,"
				+ "username varchar(225) NOT NULL UNIQUE,"
				+ "password varchar(225),"
				+ "firstName varchar(225),"
				+ "lastName varchar(225),"
				+ "role INT NOT NULL);");
		
		stmt.close();
		conn.close();
		return true;
	}
	
	public List<User> getAllUsers() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM users");
		List<User> users = new ArrayList<>();
		while(rs.next()) {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("username"));
//			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setRole(rs.getInt("role"));
			users.add( user );
		}
		rs.close();
		stmt.close();
		return users;
	}
	
	public User getUser(int id) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?;");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		User user = new User();
		while(rs.next()) {
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("username"));
//			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setRole(rs.getInt("role"));
			break;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return user;
	}
	
	public boolean createUser(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE username = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (user.getUsername().equals(rs.getString("username"))) {
				rs.close();
				pstmt.close();
				conn.close();
				return false;
			}
		}
		
		sql = "INSERT INTO users (username, password, firstName, lastName, role) "
				+ "VALUES (?, ?, ?, ?, ?);";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPassword());
		pstmt.setString(3, user.getFirstName());
		pstmt.setString(4, user.getLastName());
		pstmt.setInt(5, user.getRole());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}
	
	public boolean updateUser(int id, User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET password = ?, firstName = ?, lastName = ?, role = ?"
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getPassword());
		pstmt.setString(2, user.getFirstName());
		pstmt.setString(3, user.getLastName());
		pstmt.setInt(4, user.getRole());
		pstmt.setInt(5, id);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}
	
	public boolean deleteUser(int id) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "DELETE FROM users WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}
	
	public User authenticateUser(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPassword());
		ResultSet rs = pstmt.executeQuery();
		
		User newUser = new User();
		while (rs.next()) {
			newUser.setId(rs.getInt("id"));
			newUser.setUsername(rs.getString("username"));
			newUser.setPassword(rs.getString("password"));
			newUser.setFirstName(rs.getString("firstName"));
			newUser.setLastName(rs.getString("lastName"));
			newUser.setRole(rs.getInt("role"));
		}
		return newUser;
	}
}
