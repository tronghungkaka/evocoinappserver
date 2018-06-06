package com.evo.trade.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.evo.trade.utils.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment {
	@JsonProperty("id") private int id;
	@JsonProperty("createdUserId") private int createdUserId;
	@JsonProperty("confirmedUserId") int confirmedUserId;
	@JsonProperty("createdTimestamp") String createdTimestamp;
	@JsonProperty("confirmedTimestamp") String confirmedTimestamp;
//	@JsonProperty("sendMoney") String sendMoney;
//	@JsonProperty("receivedMoney") String receivedMoney;
	@JsonProperty("transactionId") String transactionId;
	@JsonProperty("paymentPackage") String paymentPackage;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}
	public int getConfirmedUserId() {
		return confirmedUserId;
	}
	public void setConfirmedUserId(int confirmedUserId) {
		this.confirmedUserId = confirmedUserId;
	}
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getConfirmedTimestamp() {
		return confirmedTimestamp;
	}
	public void setConfirmedTimestamp(String confirmedTimestamp) {
		this.confirmedTimestamp = confirmedTimestamp;
	}
//	public String getSendMoney() {
//		return sendMoney;
//	}
//	public void setSendMoney(String sendMoney) {
//		this.sendMoney = sendMoney;
//	}
//	public String getReceivedMoney() {
//		return receivedMoney;
//	}
//	public void setReceivedMoney(String receivedMoney) {
//		this.receivedMoney = receivedMoney;
//	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPaymentPackage() {
		return paymentPackage;
	}
	public void setPaymentPackage(String paymentPackage) {
		this.paymentPackage = paymentPackage;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, Utilities.TO_STRING_BUILDER_STYLE)
				.append("id", id)
				.append("createdUserId", createdUserId)
				.append("confirmedUserId", confirmedUserId)
				.append("createdTimestamp", createdTimestamp)
				.append("confirmedTimestamp", confirmedTimestamp)
//				.append("sendMoney", sendMoney)
//				.append("receivedMoney", receivedMoney)
				.append("transactionId", transactionId)
				.append("paymentPackage", paymentPackage)
				
				.toString();
	}
}
