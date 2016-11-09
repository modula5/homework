package io.fourfinanceit.risk.rules;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;

public interface RiskRule {

	void apply(ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication);
}
