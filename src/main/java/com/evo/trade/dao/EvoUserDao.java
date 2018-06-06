package com.evo.trade.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.evo.trade.objects.Payment;
import com.evo.trade.objects.User;
import com.evo.trade.utils.Utilities;

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
	
	public boolean dropUserTable() throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		String sql = "DROP TABLE IF EXISTS users;";
		stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
		return true;
	}
	
	public boolean createUserTable() throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ "users ("
				+ "id SERIAL NOT NULL PRIMARY KEY,"
				+ "username varchar(225) NOT NULL UNIQUE,"
				+ "password varchar(225),"
				+ "firstName varchar(225),"
				+ "lastName varchar(225),"
				+ "fullName varchar(225),"
				+ "email varchar(225),"
				+ "phone varchar(25),"
				+ "role INT NOT NULL,"
//				+ "isExpired BOOLEAN NOT NULL,"
				+ "createdTimestamp varchar(25),"
//				+ "isFreeTrial BOOLEAN NOT NULL,"
				+ "isPaymentPending BOOLEAN NOT NULL"
				+ ");";
		stmt.executeUpdate(sql);
		System.out.println("users table is created");
		stmt.close();
		conn.close();
		return true;
	}
	
	public boolean createSuperRoot() throws ClassNotFoundException, SQLException {
		User root = new User();
		root.setUsername("evosuperroot1");
		root.setPassword("Evosuperroot1");
		root.setFullName("Evo super root 1");
		root.setEmail("evocointeam@gmail.com");
		root.setPhone("");
		root.setRole(Utilities.ROOT_ROLE);
		root.setExpired(false);
		root.setCreatedTimestamp("" + System.currentTimeMillis());
		root.setFreeTrial(false);
		root.setPaymentPending(false);
		
		createUser(root);
		
		System.out.println("super root is created");
		
		return true;
	}
	
	public List<User> getAllUsers() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM users");
		List<User> users = new ArrayList<>();
		while(rs.next()) {
			User user = setUser(rs);
			users.add( user );
		}
		rs.close();
		stmt.close();
		return users;
	}

	public List<User> getAllUsersAccount() throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = getConnection().prepareStatement("SELECT * FROM users WHERE role < ?");
		pstmt.setInt(1, Utilities.ADMIN_ROLE);
		ResultSet rs = pstmt.executeQuery();
		List<User> users = new ArrayList<>();
		while(rs.next()) {
			User user = setUser(rs);
			users.add( user );
		}
		rs.close();
		pstmt.close();
		return users;
	}
	
	public User getUserById(int id) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?;");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		User user = new User();
		while(rs.next()) {
			user = setUser(rs);
			break;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return user;
	}

	public User getUserByUsername(String username) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?;");
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		User user = new User();
		while(rs.next()) {
			user = setUser(rs);
			break;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return user;
	}

	public User getUserByTransactionId(String transactionId) throws ClassNotFoundException, SQLException {
		Payment payment = EvoPaymentDao.getInstance().getPaymentByTransactionId(transactionId);
		User user = getUserById(payment.getCreatedUserId());
		return user;
	}
	
	public User createUser(User user) throws ClassNotFoundException, SQLException {
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
				return null;
			}
		}
		
		sql = "INSERT INTO users ("
				+ "username, password, firstName, lastName, fullName, "
				+ "email, phone, role, "
//				+ "isExpired, "
				+ "createdTimestamp, "
//				+ "isFreeTrial, "
				+ "isPaymentPending) "
				+ "VALUES ("
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, "
//				+ "?, "
				+ "?, "
//				+ "?, "
				+ "?);";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPassword());
		pstmt.setString(3, user.getFirstName());
		pstmt.setString(4, user.getLastName());
		pstmt.setString(5, user.getFullName());
		pstmt.setString(6, user.getEmail());
		pstmt.setString(7, user.getPhone());
		pstmt.setInt(8, user.getRole());
//		pstmt.setBoolean(9, user.isExpired());
		
		user.setCreatedTimestamp("" + System.currentTimeMillis());
		pstmt.setString(9, user.getCreatedTimestamp());
//		pstmt.setBoolean(11, user.isFreeTrial());
		pstmt.setBoolean(10, user.isPaymentPending());
		
		pstmt.executeUpdate();
		
		user = getUserByUsername(user.getUsername());
		
		pstmt.close();
		conn.close();
		return user;
	}
	
	public boolean updateUser(int id, User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET password = ?, firstName = ?, lastName = ?, fullName = ?, email = ?, phone = ?, role = ?, "
//				+ "isExpired = ?, isFreeTrial = ?, "
				+ "isPaymentPending = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getPassword());
		pstmt.setString(2, user.getFirstName());
		pstmt.setString(3, user.getLastName());
		pstmt.setString(4, user.getFullName());
		pstmt.setString(5, user.getEmail());
		pstmt.setString(6, user.getPhone());
		pstmt.setInt(7, user.getRole());
//		pstmt.setBoolean(8, user.isExpired());
//		pstmt.setBoolean(9, user.isFreeTrial());
		pstmt.setBoolean(8, user.isPaymentPending());
		pstmt.setInt(9, id);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}

	public boolean createPaymentUser(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET isPaymentPending = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setBoolean(1, true);
		pstmt.setInt(2, user.getId());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}

	public boolean confirmPaymentUser(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET isPaymentPending = ?, role = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setBoolean(1, false);
		pstmt.setInt(2, user.getRole());
		pstmt.setInt(3, user.getId());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}
	
	public boolean changeRole(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET role = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, user.getRole());
		pstmt.setInt(2, user.getId());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}

	public boolean changePassword(User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET password = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getPassword());
		pstmt.setInt(2, user.getId());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}

	public boolean updateUser(String username, User user) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE users "
				+ "SET password = ?, firstName = ?, lastName = ?, fullName = ?, email = ?, phone = ?, role = ?, "
//				+ "isExpired = ?, isFreeTrial = ?, "
				+ "isPaymentPending = ? "
				+ "WHERE username = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user.getPassword());
		pstmt.setString(2, user.getFirstName());
		pstmt.setString(3, user.getLastName());
		pstmt.setString(4, user.getFullName());
		pstmt.setString(5, user.getEmail());
		pstmt.setString(6, user.getPhone());
		pstmt.setInt(7, user.getRole());
//		pstmt.setBoolean(8, user.isExpired());
//		pstmt.setBoolean(9, user.isFreeTrial());
		pstmt.setBoolean(8, user.isPaymentPending());
		pstmt.setString(9, username);
		
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
		
		User newUser = null;
		while (rs.next()) {
			newUser = setUser(rs);
			break;
		}
		if (newUser != null)
			System.out.println(newUser.toString());
		return newUser;
	}

	public boolean validateUsername(String username) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE username = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		
		User newUser = null;
		while (rs.next()) {
			newUser = setUser(rs);
		}
		return newUser != null ? true : false;
	}

	public boolean validateEmail(String email) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE email = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		
		User newUser = null;
		while (rs.next()) {
			newUser = setUser(rs);
		}
		return newUser != null ? true : false;
	}

	public boolean validatePhone(String phone) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE phone = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, phone);
		ResultSet rs = pstmt.executeQuery();
		
		User newUser = null;
		while (rs.next()) {
			newUser = setUser(rs);
		}
		return newUser != null ? true : false;
	}
	
	private User setUser(ResultSet rs) throws SQLException, ClassNotFoundException {
		User user = new User();

		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
//		user.setPassword(rs.getString("password"));
		user.setFirstName(rs.getString("firstName"));
		user.setLastName(rs.getString("lastName"));
		user.setFullName(rs.getString("fullName"));
		user.setEmail(rs.getString("email"));
		user.setPhone(rs.getString("phone"));
		user.setRole(rs.getInt("role"));
		
//		user.setExpired(rs.getBoolean("isExpired"));
		if (user.getId() >= Utilities.ADMIN_ROLE) // admin and root is never expired.
			user.setExpired(false);
		else
			user.setExpired(isUserExpired(user.getId()));
		
		user.setCreatedTimestamp(rs.getString("createdTimestamp"));
		
//		user.setFreeTrial(rs.getBoolean("isFreeTrial"));
		user.setFreeTrial(isFreeTrial(Long.parseLong(user.getCreatedTimestamp())));
		
		user.setPaymentPending(rs.getBoolean("isPaymentPending"));
		
		return user;
	}
	
	public boolean isUserExpired(int userId) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments "
				+ "WHERE createdUserId = ? AND confirmedUserId > 0 "
				+ "ORDER BY confirmedTimestamp DESC "
				+ "LIMIT 1;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		ResultSet rs = pstmt.executeQuery();
		String createdTimestamp = "";
		while (rs.next()) {
			createdTimestamp = rs.getString("createdTimestamp");
			break;
		}
		if (createdTimestamp.isEmpty())
			return true;
		return Utilities.calcTimeframeBetweenTimestamps(Long.parseLong(createdTimestamp), System.currentTimeMillis(), Utilities.DAY) > 31;
	}
	
	public boolean isFreeTrial(long createdTimestamp) {
		if (Utilities.calcTimeframeBetweenTimestamps(createdTimestamp, System.currentTimeMillis(), Utilities.DAY) > 7) {
			return false;
		}
		return true;
	}
}
