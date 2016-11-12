
package scripts

import io.fourfinanceit.beans.ApplyForLoanBean
import io.fourfinanceit.domain.LoanApplication
import io.fourfinanceit.repository.LoanApplicationRepository
import io.fourfinanceit.risk.rules.AfterMidnightRule
import io.fourfinanceit.risk.rules.SameAddressApplicationRule

import java.time.LocalDateTime

import spock.lang.Specification

class RiskRuleTest extends Specification {
	
	def "Same address application"() {
		given: 
			LocalDateTime now = LocalDateTime.now()
			LoanApplicationRepository loanApplicationRepositoryMock = Mock(LoanApplicationRepository)
			LoanApplication loanApplication = new LoanApplication()
			ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean()
			SameAddressApplicationRule sameAddressApplicationRule = new SameAddressApplicationRule()
			sameAddressApplicationRule.loanApplicationRepository = loanApplicationRepositoryMock
			
		when:
			sameAddressApplicationRule.apply(now, applyForLoanBean, loanApplication)
		then: 
			loanApplication.manualWarnings == "Reached max applications (e.g. 3) per day from a single IP"
			1 * loanApplicationRepositoryMock.getApplicationsCountOnThisDay(_) >> 3
	}
	
	def "After midnight"() {
		given:
			LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(1)
			LoanApplication loanApplication = new LoanApplication()
			ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean(amount : 500, term : 12)
			AfterMidnightRule afterMidnightRule = new AfterMidnightRule()
			afterMidnightRule.maxAmount = 500.00
			
		when:
			afterMidnightRule.apply(now, applyForLoanBean, loanApplication)
		then:
			loanApplication.manualWarnings == "The attempt to take loan is made after 00:00 with max possible amount"
	}
	
}