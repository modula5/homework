package io.fourfinanceit.beans;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class ClientBean {

	private String login;
	
	private String password;
	
	private List<LoanApplicationBean> loanApplications = newArrayList();

	private List<LoanApplicationBean> loans = newArrayList();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<LoanApplicationBean> getLoanApplications() {
		return loanApplications;
	}

	public void setLoanApplications(List<LoanApplicationBean> loanApplications) {
		this.loanApplications = loanApplications;
	}

	public List<LoanApplicationBean> getLoans() {
		return loans;
	}

	public void setLoans(List<LoanApplicationBean> loans) {
		this.loans = loans;
	}

}
