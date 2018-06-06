package com.evo.trade.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.evo.trade.objects.Payment;

public class EvoPaymentDao extends ConfigDao {
	// Singleton Design Pattern
	private static EvoPaymentDao instance = null;
	private EvoPaymentDao() { }
	public static EvoPaymentDao getInstance() {
		if (instance == null) {
			instance = new EvoPaymentDao();
		}
		return instance;
	}
	
	public boolean createPaymentTable() throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ "payments ("
				+ "id SERIAL NOT NULL PRIMARY KEY,"
				+ "createdUserId INT NOT NULL,"
				+ "confirmedUserId INT,"
				+ "createdTimestamp varchar(225) NOT NULL,"
				+ "confirmedTimestamp varchar(225),"
//				+ "sendMoney varchar(225) NOT NULL,"
//				+ "receivedMoney varchar(225),"
				+ "transactionId varchar(225),"
				+ "paymentPackage varchar(225) NOT NULL"
				+ ");";
		stmt.executeUpdate(sql);
		
		System.out.println("payments table is created");
		
		stmt.close();
		conn.close();
		return true;
	}
	
	public List<Payment> getAllUserPaymentByCreater(int userId) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments WHERE createdUserId = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		ResultSet rs = pstmt.executeQuery();
		List<Payment> payments = new ArrayList<>();
		while(rs.next()) {
			Payment payment = setPayment(rs);
			payments.add(payment);
		}
		rs.close();
		pstmt.close();
		conn.close();
		return payments;
	}
	
	public List<Payment> getAllUserUnconfirmedPaymentByCreater(int userId) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments WHERE createdUserId = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		ResultSet rs = pstmt.executeQuery();
		List<Payment> payments = new ArrayList<>();
		while(rs.next()) {
			Payment payment = setPayment(rs);
			if (payment.getConfirmedUserId() == 0) // confirmer id is zero
				payments.add(payment);
		}
		rs.close();
		pstmt.close();
		conn.close();
		return payments;
	}
	
	public List<Payment> getAllUserPaymentByConfirmer(int userId) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments WHERE confirmedUserId = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		ResultSet rs = pstmt.executeQuery();
		List<Payment> payments = new ArrayList<>();
		while(rs.next()) {
			Payment payment = setPayment(rs);
			payments.add(payment);;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return payments;
	}
	
	public Payment getPayment(Payment payment) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments WHERE createdUserId = ? AND createdTimestamp = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, payment.getCreatedUserId());
		pstmt.setString(2, payment.getCreatedTimestamp());
		ResultSet rs = pstmt.executeQuery();
		Payment newPayment = new Payment();
		while (rs.next()) {
			newPayment = setPayment(rs);
			break;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return newPayment;
	}

	public Payment getPaymentByTransactionId(String transactionId) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "SELECT * FROM payments WHERE transactionId = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, transactionId);
		ResultSet rs = pstmt.executeQuery();
		Payment newPayment = new Payment();
		while (rs.next()) {
			newPayment = setPayment(rs);
			break;
		}
		rs.close();
		pstmt.close();
		conn.close();
		return newPayment;
	}
	
	public Payment createPayment(Payment payment) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "INSERT INTO payments ("
				+ "createdUserId, "
				+ "confirmedUserId, "
				+ "createdTimestamp, "
				+ "confirmedTimestamp, "
//				+ "sendMoney, "
//				+ "receivedMoney, "
				+ "transactionId, "
				+ "paymentPackage) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, payment.getCreatedUserId());
		pstmt.setInt(2, payment.getConfirmedUserId());
		
		payment.setCreatedTimestamp("" + System.currentTimeMillis());
		pstmt.setString(3, payment.getCreatedTimestamp());
		pstmt.setString(4, payment.getConfirmedTimestamp());
//		pstmt.setString(5, payment.getSendMoney());
//		pstmt.setString(6, payment.getReceivedMoney());
		pstmt.setString(5, payment.getTransactionId());
		pstmt.setString(6, payment.getPaymentPackage());
		
		pstmt.executeUpdate();
		
		sql = "SELECT * FROM payments WHERE createdUserId = ? AND createdTimestamp = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, payment.getCreatedUserId());
		pstmt.setString(2, payment.getCreatedTimestamp());
		ResultSet rs = pstmt.executeQuery();
		Payment newPayment = new Payment();
		while (rs.next()) {
			newPayment = setPayment(rs);
			break;
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		return newPayment;
	}
	
	public boolean confirmPayment(Payment payment) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String sql = "UPDATE payments "
				+ "SET confirmedUserId = ?, "
				+ "confirmedTimestamp = ? "
//				+ ", receivedMoney = ? "
				+ "WHERE id = ?;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, payment.getConfirmedUserId());
		
		payment.setConfirmedTimestamp("" + System.currentTimeMillis());
		pstmt.setString(2, payment.getConfirmedTimestamp());
//		pstmt.setString(3, payment.getReceivedMoney());
		pstmt.setInt(3, payment.getId());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		return true;
	}
	
	private Payment setPayment(ResultSet rs) throws SQLException {
		Payment payment = new Payment();
		payment.setId(rs.getInt("id"));
		payment.setCreatedUserId(rs.getInt("createdUserId"));
		payment.setConfirmedUserId(rs.getInt("confirmedUserId"));
		payment.setCreatedTimestamp(rs.getString("createdTimestamp"));
		payment.setConfirmedTimestamp(rs.getString("confirmedTimestamp"));
//		payment.setSendMoney(rs.getString("sendMoney"));
//		payment.setReceivedMoney(rs.getString("receivedMoney"));
		payment.setTransactionId(rs.getString("transactionId"));
		payment.setPaymentPackage(rs.getString("paymentPackage"));
		
		return payment;
	}
}
