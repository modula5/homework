package io.fourfinanceit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import io.fourfinanceit.beans.LoanExtensionBean;

@Entity
@Table(name = "loan_extensions")
public class LoanExtension extends BaseEntity {

	@Column(name = "term")
	private int term;
	
	@ManyToOne
	@JoinColumn(name="loan", nullable=false)
	private Loan loan;
	
	public void setTerm(int term) {
		this.term = term;
	}
	public int getTerm() {
		return term;
	}
	
	public void setLoan(Loan loan) {
		this.loan = loan;
	}
	public Loan getLoan() {
		return loan;
	}
	
	public LoanExtensionBean toBean() {
		LoanExtensionBean loanExtensionBean = new LoanExtensionBean();
		BeanUtils.copyProperties(this, loanExtensionBean);
		return loanExtensionBean;
	}
}
