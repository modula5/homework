package io.fourfinanceit.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	public static BigDecimal amount(BigDecimal amount) {
		return amount(amount.toPlainString());
	}
	
	public static BigDecimal amount(String amount) {
		return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
	}
	
	public static boolean isE(BigDecimal amount1, BigDecimal amount2) {
		return amount1.compareTo(amount2) == 0;
	}
	
	public static boolean isLe(BigDecimal amount1, BigDecimal amount2) {
		return amount1.compareTo(amount2) <= 0;
	}
	
	public static boolean isL(BigDecimal amount1, BigDecimal amount2) {
		return amount1.compareTo(amount2) < 0;
	}
	
	public static boolean isGe(BigDecimal amount1, BigDecimal amount2) {
		return amount1.compareTo(amount2) >= 0;
	}
	
	public static boolean isG(BigDecimal amount1, BigDecimal amount2) {
		return amount1.compareTo(amount2) > 0;
	}
	
	public static BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, int termInMonths, BigDecimal monthlyRate) {
		monthlyRate = monthlyRate.divide(Constants.ONE_HUNDRED, 8, RoundingMode.HALF_UP);
		BigDecimal divider = BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyRate).pow(-termInMonths, new MathContext(8)));
		BigDecimal monthlyPayment = loanAmount.multiply(monthlyRate).divide(divider, 2, RoundingMode.HALF_UP);
		return monthlyPayment;
	}
	
	public static BigDecimal percentValueOf(BigDecimal amount, BigDecimal percentage) {
		BigDecimal percentValueOf = amount.multiply(percentage).divide(Constants.ONE_HUNDRED, 2, RoundingMode.HALF_UP);
		return percentValueOf;
	}
	
	public static String getRemoteAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = request.getHeader("X_FORWARDED_FOR");
            if (ipAddress == null){
                ipAddress = request.getRemoteAddr();
            }
        }
        return ipAddress;
	}
}
