package scripts

import io.fourfinanceit.util.Utils
import spock.lang.Specification
import spock.lang.Unroll

class RepaymentTest extends Specification {

	@Unroll("Where loan amount : #loanAmount, term in month : #termInMonths, monthly rate : #monthlyRate")
	def "Monthly payment calculation"() {
		given:

		expect:
		Utils.calculateMonthlyPayment(loanAmount, termInMonths, monthlyRate) == result
		where:
		loanAmount | termInMonths | monthlyRate | result
		500 		| 12		  | 2.5			| 48.74
		300 		| 12		  | 2.5				| 29.25
		100 		| 12		  | 2.5				| 9.75
		500 		| 6		  		| 2.5			| 90.77
		500 		| 3		 		 | 2.5			| 175.07
	}
   }