package io.fourfinanceit.risk.rules;

import static io.fourfinanceit.util.Utils.amount;
import static io.fourfinanceit.util.Utils.isE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.domain.LoanApplication;

@Component
public class AfterMidnightRule implements RiskRule {
	
	@Autowired
	private Environment environment;
	
	private BigDecimal maxAmount;
	
	@PostConstruct
	public void init() {
		maxAmount = amount(environment.getProperty("loan.max.amount"));
	}

	@Override
	public void apply(ApplyForLoanBean applyForLoanBean, LoanApplication loanApplication) {
		LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime midnightPlus8Hours = midnight.plusHours(7);
		LocalDateTime now = LocalDateTime.now();
		
		if (isE(applyForLoanBean.getAmount(), maxAmount)
				&& now.isAfter(midnight) && now.isBefore(midnightPlus8Hours)) {
			loanApplication.manual("The attempt to take loan is made after 00:00 with max possible amount.");
		}		

	}

}
