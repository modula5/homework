package io.fourfinanceit.beans;

public class CreateExtensionBean {

	private int term;
	private Long clientId;
	
	public void setTerm(int term) {
		this.term = term;
	}
	public int getTerm() {
		return term;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getClientId() {
		return clientId;
	}
}
