package com.evo.trade.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.evo.trade.utils.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@JsonProperty("id") private int id;
	@JsonProperty("username") private String username;
	@JsonProperty("password") private String password;
	@JsonProperty("firstName") private String firstName;
	@JsonProperty("lastName") private String lastName;
	@JsonProperty("fullName") private String fullName;
	@JsonProperty("email") private String email;
	@JsonProperty("phone") private String phone;
	@JsonProperty("role") private int role;
	@JsonProperty("isExpired") private boolean isExpired;
	@JsonProperty("createdTimestamp") private String createdTimestamp;
	@JsonProperty("isFreeTrial") private boolean isFreeTrial;
	@JsonProperty("isPaymentPending") private boolean isPaymentPending;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public boolean isFreeTrial() {
		return isFreeTrial;
	}
	public void setFreeTrial(boolean isFreeTrial) {
		this.isFreeTrial = isFreeTrial;
	}
	public boolean isPaymentPending() {
		return isPaymentPending;
	}
	public void setPaymentPending(boolean isPaymentPending) {
		this.isPaymentPending = isPaymentPending;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, Utilities.TO_STRING_BUILDER_STYLE)
				.append("id", id)
				.append("username", username)
				.append("password", password)
				.append("firstName", firstName)
				.append("lastName", lastName)
				.append("fullName", fullName)
				.append("email", email)
				.append("phone", phone)
				.append("role", role)
				.append("isExpired", isExpired)
				.append("createdTimestamp", createdTimestamp)
				.append("isFreeTrial", isFreeTrial)
				.append("isPaymentPending", isPaymentPending)
				
				.toString();
	}
}
