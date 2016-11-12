package io.fourfinanceit.beans;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.fourfinanceit.enums.LoanStatus;

public class LoanBean extends BaseBean {

	private Long id;
	
	private BigDecimal principal;
	
	private BigDecimal interest;
	
	private BigDecimal monthlyPayment;
	
	private BigDecimal extensionPayment;
	
	private LocalDate dueDate;
	
	private LoanStatus status;
	
	private LoanApplicationBean loanApplicationBean;
	
	private List<LoanExtensionBean> loanExtensions = newArrayList();
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public List<LoanExtensionBean> getLoanExtensions() {
		return loanExtensions;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public BigDecimal getExtensionPayment() {
		return extensionPayment;
	}

	public void setExtensionPayment(BigDecimal extensionPayment) {
		this.extensionPayment = extensionPayment;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	public void setLoanExtensions(List<LoanExtensionBean> loanExtensions) {
		this.loanExtensions = loanExtensions;
	}
	
	public void setLoanApplicationBean(LoanApplicationBean loanApplicationBean) {
		this.loanApplicationBean = loanApplicationBean;
	}
	
	public LoanApplicationBean getLoanApplicationBean() {
		return loanApplicationBean;
	}
	
}
