package io.fourfinanceit.domain;

import static com.google.common.collect.Lists.newArrayList;
import static io.fourfinanceit.enums.LoanApplicationStatus.OPEN;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.enums.LoanApplicationStatus;

@Entity
@Table(name = "loan_applications")
public class LoanApplication extends BaseEntity {
	
	@Column(name = "amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;
	
	@Column(name = "term", nullable = false)
	private int term;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private LoanApplicationStatus status = OPEN;
	
	@Column(name = "resolution")
	@Enumerated(EnumType.STRING)
	private LoanApplicationResolution resolution;
	
	@Column(name = "reject_reason")
	private String rejectReason;
	
	@Column(name = "manual_warnings")
	private String manualWarnings;
	
	@ManyToOne
	@JoinColumn(name="client", nullable=false)
	private Client client;
	
	public void approve() {
		status = LoanApplicationStatus.CLOSED;
		resolution = LoanApplicationResolution.APPROVED;
	}
	
	public void reject(String rejectReason) {
		status = LoanApplicationStatus.CLOSED;
		resolution = LoanApplicationResolution.REJECTED;
		this.rejectReason = rejectReason;
	}
	
	public void manual(String[] manualWarnings) {
		resolution = LoanApplicationResolution.MANUAL;
		List<String> manualWarningsTmp = newArrayList(this.manualWarnings == null ? Collections.emptyList() : Splitter.on("\n").splitToList(this.manualWarnings));
		this.manualWarnings = concat(manualWarningsTmp.stream(), stream(manualWarnings)).collect(joining("\n"));
	}
	
	public void manual(String manualWarning) {
		resolution = LoanApplicationResolution.MANUAL;
		List<String> manualWarningsTmp = newArrayList(this.manualWarnings == null ? Collections.emptyList() : Splitter.on("\n").splitToList(this.manualWarnings));
		manualWarningsTmp.add(manualWarning);
		this.manualWarnings = Joiner.on("\n").join(manualWarningsTmp);
	}
	
	
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
	public void setClient(Client client) {
		this.client = client;
	}
	public Client getClient() {
		return client;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setStatus(LoanApplicationStatus status) {
		this.status = status;
	}
	public LoanApplicationStatus getStatus() {
		return status;
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
	
	public LoanApplicationBean toBean() {
		LoanApplicationBean loanApplicationBean = new LoanApplicationBean();
		BeanUtils.copyProperties(this, loanApplicationBean);
		return loanApplicationBean;
	}

}
