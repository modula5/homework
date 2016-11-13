package io.fourfinanceit.domain;


import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.fourfinanceit.enums.LoanApplicationStatus;
import io.fourfinanceit.enums.LoanStatus;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity {
	
	@Column(name = "login", unique = true, nullable = false)
	private String login;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
	private List<LoanApplication> loanApplications = newArrayList();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
	private List<Loan> loans = newArrayList();

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

	public List<LoanApplication> getLoanApplications() {
		return copyOf(loanApplications);
	}

	public void setLoanApplications(List<LoanApplication> loanApplications) {
		this.loanApplications = loanApplications;
	}

	public List<Loan> getLoans() {
		return copyOf(loans);
	}

	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}
	
	public boolean hasLoans() {
		return !getLoans().isEmpty();
	}
	
	public boolean hasOpenLoans() {
		return getLoans().stream().anyMatch(loan -> loan.getStatus() == LoanStatus.OPEN);
	}
	
	public boolean hasOpenApplications() {
		return getLoanApplications().stream().anyMatch(application -> application.getStatus() == LoanApplicationStatus.OPEN);
	}
	
}
