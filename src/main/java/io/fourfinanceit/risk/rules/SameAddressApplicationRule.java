package io.fourfinanceit.risk.rules;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;
import io.fourfinanceit.repository.LoanApplicationRepository;

@Component
@Order(20)
public class SameAddressApplicationRule implements RiskRule {

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Override
	public void apply(LocalDateTime when, ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication) {
		Long applicationsCount = loanApplicationRepository.getApplicationsCountOnThisDay(applyForLoanBean.getAddress());
		if (applicationsCount == 3) {
			loanApplication.manual("Reached max applications (e.g. 3) per day from a single IP");
		}

	}

}
