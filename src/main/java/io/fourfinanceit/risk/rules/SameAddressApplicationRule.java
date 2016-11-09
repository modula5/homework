package io.fourfinanceit.risk.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;
import io.fourfinanceit.repository.LoanApplicationRepository;

@Component
public class SameAddressApplicationRule implements RiskRule {

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Override
	public void apply(ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication) {
		Long applicationsCount = loanApplicationRepository.getApplicationsCountOnThisDay(applyForLoanBean.getAddress());
		if (applicationsCount == 3) {
			loanApplication.manual("Reached max applications (e.g. 3) per day from a single IP");
		}

	}

}
