package io.fourfinanceit.beans;

import java.math.BigDecimal;

import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.enums.LoanApplicationStatus;

public class LoanApplicationBean {

	private BigDecimal amount;

	private int term;
	
	private String address;
	
	private LoanApplicationStatus status;
	
	private LoanApplicationResolution resolution;
	
	private String rejectReason;
	
	private String manualWarnings;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LoanApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(LoanApplicationStatus status) {
		this.status = status;
	}

	public LoanApplicationResolution getResolution() {
		return resolution;
	}

	public void setResolution(LoanApplicationResolution resolution) {
		this.resolution = resolution;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getManualWarnings() {
		return manualWarnings;
	}

	public void setManualWarnings(String manualWarnings) {
		this.manualWarnings = manualWarnings;
	}
	
	
}
