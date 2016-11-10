package io.fourfinanceit.risk.rules;

import java.time.LocalDateTime;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;

public interface RiskRule {

	void apply(LocalDateTime when, ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication);
}
