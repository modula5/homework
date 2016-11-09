package io.fourfinanceit.beans;

import static io.fourfinanceit.util.Utils.amount;

import java.math.BigDecimal;

public class ApplyForLoanBean {

	private BigDecimal amount;
	private int term;
	private Long clientId;
	private String address;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount(amount);
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	
	
}
