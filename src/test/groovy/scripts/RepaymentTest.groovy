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
		500 		| 12		  | 2.5			| 50.23
		300 		| 12		  | 2.5				| 30.14
		100 		| 12		  | 2.5				| 10.05
		500 		| 6		  		| 2.5			| 92.30
		500 		| 3		 		 | 2.5			| 176.77
	}
   }