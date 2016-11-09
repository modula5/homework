package scripts

import io.fourfinanceit.util.Utils
import spock.lang.Specification
import spock.lang.Unroll

class HomeworkTests extends Specification {

	@Unroll("Where loan amount : #loanAmount, term in month : #termInMonths, monthly rate : #monthlyRate")
	def "Monthly payment calculation"() {
		given:

		expect:
		Utils.calculateMonthlyPayment(loanAmount, termInMonths, monthlyRate) == result
		where:
		loanAmount | termInMonths | monthlyRate | result
		500 		| 12		  | 30			| 156.73
		300 		| 12		  | 30			| 94.04
		100 		| 12		  | 30			| 31.35
		500 		| 6		  		| 30			| 189.20
		500 		| 3		 		 | 30			| 275.31
	}
   }