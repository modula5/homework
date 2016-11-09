package io.fourfinanceit.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Utils {

	public static BigDecimal amount(BigDecimal amount) {
		return amount(amount.toPlainString());
	}
	
	public static BigDecimal amount(String amount) {
		return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
	}
	
	public static boolean isE(BigDecimal amount1, BigDecimal amount2) {
		return amount(amount1).compareTo(amount(amount2)) == 0;
	}
	
	public static boolean isLe(BigDecimal amount1, BigDecimal amount2) {
		return amount(amount1).compareTo(amount(amount2)) <= 0;
	}
	
	public static boolean isL(BigDecimal amount1, BigDecimal amount2) {
		return amount(amount1).compareTo(amount(amount2)) < 0;
	}
	
	public static boolean isGe(BigDecimal amount1, BigDecimal amount2) {
		return amount(amount1).compareTo(amount(amount2)) >= 0;
	}
	
	public static boolean isG(BigDecimal amount1, BigDecimal amount2) {
		return amount(amount1).compareTo(amount(amount2)) > 0;
	}
	
	public static BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, int termInMonths, BigDecimal monthlyRate) {
		monthlyRate = monthlyRate.divide(Constants.ONE_HUNDRED, 2, RoundingMode.HALF_UP);
		BigDecimal monthlyPayment = loanAmount.multiply(monthlyRate).divide(BigDecimal.ONE.subtract(monthlyRate.add(BigDecimal.ONE).pow(-termInMonths, new MathContext(10))), 2, RoundingMode.HALF_UP);
		return monthlyPayment;
	}
}
