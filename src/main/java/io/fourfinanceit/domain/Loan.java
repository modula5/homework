package io.fourfinanceit.domain;

import static com.google.common.collect.Lists.newArrayList;
import static io.fourfinanceit.enums.LoanStatus.OPEN;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.enums.LoanStatus;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {
	
	@Column(name = "principal", nullable = false, precision = 10, scale = 2)
	private BigDecimal principal;
	
	@Column(name = "interest", nullable = false, precision = 10, scale = 2)
	private BigDecimal interest;
	
	@Column(name = "monthly_payment", nullable = false, precision = 10, scale = 2)
	private BigDecimal monthlyPayment;
	
	@Column(name = "extension_payment")
	private BigDecimal extensionPayment;
	
	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private LoanStatus status = OPEN;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loan", cascade = CascadeType.ALL)
	private List<LoanExtension> loanExtensions = newArrayList();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="client", nullable=false)
	private Client client;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	private LoanApplication loanApplication;
	
	public void setClient(Client client) {
		this.client = client;
	}
	public Client getClient() {
		return client;
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
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public void setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public BigDecimal getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setLoanExtensions(List<LoanExtension> loanExtensions) {
		this.loanExtensions = loanExtensions;
	}
	public List<LoanExtension> getLoanExtensions() {
		return loanExtensions;
	}
	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	public LoanStatus getStatus() {
		return status;
	}
	public void setExtensionPayment(BigDecimal extensionPayment) {
		this.extensionPayment = extensionPayment;
	}
	public BigDecimal getExtensionPayment() {
		return extensionPayment;
	}
	
	public void setLoanApplication(LoanApplication loanApplication) {
		this.loanApplication = loanApplication;
	}
	
	public LoanApplication getLoanApplication() {
		return loanApplication;
	}
	
	public LoanBean toBean() {
		LoanBean loanBean = new LoanBean();
		BeanUtils.copyProperties(this, loanBean, "loanExtensions");
		loanBean.getLoanExtensions().addAll(getLoanExtensions().stream().map(LoanExtension::toBean).collect(toList()));
		loanBean.setLoanApplicationBean(getLoanApplication().toBean());
		return loanBean;
	}
}
