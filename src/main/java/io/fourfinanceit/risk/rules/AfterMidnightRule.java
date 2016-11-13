package io.fourfinanceit.risk.rules;

import static io.fourfinanceit.util.Utils.isE;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;

@Component
public class AfterMidnightRule implements RiskRule {
	
	@Value("#{T(io.fourfinanceit.util.Utils).amount('${loan.max.amount}')}")
	private BigDecimal maxAmount;	

	@Override
	public void apply(LocalDateTime when, ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication) {
		LocalDateTime midnight = LocalDateTime.of(when.toLocalDate(), LocalTime.MIDNIGHT);
		LocalDateTime midnightPlus8Hours = midnight.plusHours(7);

		if (isE(applyForLoanBean.getAmount(), maxAmount)
				&& when.isAfter(midnight) && when.isBefore(midnightPlus8Hours)) {
			loanApplication.manual("The attempt to take loan is made after 00:00 with max possible amount");
		}		

	}

}
