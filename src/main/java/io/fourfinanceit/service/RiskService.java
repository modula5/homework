package io.fourfinanceit.service;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;
import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.risk.rules.RiskRule;

@Service
public class RiskService {
	
	@Autowired
	private List<RiskRule> riskRules = newArrayList();
	
	public boolean evaluate(ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication) {
		riskRules.forEach(riskRule -> {
			riskRule.apply(applyForLoanBean, loanApplication);
		});
		return loanApplication.getResolution() != LoanApplicationResolution.MANUAL;
	}
}
