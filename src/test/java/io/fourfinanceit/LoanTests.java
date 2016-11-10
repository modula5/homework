package io.fourfinanceit;

import static io.fourfinanceit.util.Utils.amount;
import static java.util.Comparator.comparing;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;

import io.fourfinanceit.api.ClientApi;
import io.fourfinanceit.api.LoanApi;
import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;
import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.enums.LoanApplicationStatus;

public class LoanTests extends BaseIntegrationTest {

	@Autowired
	private ClientApi clientApi;
	
	@Autowired
	private LoanApi loanApi;
	
	@Test
	public void registerClient() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);
	}
	
	@Test
	public void registerClientThenLogin() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);
		
		ClientLoginBean clientLoginBean = new ClientLoginBean();
		clientLoginBean.setLogin("modula5");
		clientLoginBean.setPassword("test12345");
		clientApi.login(clientLoginBean);
	}
	
	@Test
	public void applyForLoan() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		
		List<LoanBean> loans = loanApi.listLoans();
		assertThat(loans, hasSize(1));
		assertThat(loans.get(0).getDueDate(), is(LocalDate.now().plusMonths(12)));
		assertThat(loans.get(0).getPrincipal(), is(amount("300")));
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("30.14")));
	}
	
	@Test(expected = HttpServerErrorException.class)
	public void applyForLoanMoreThanMax() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(700));
		applyForLoanBean.setTerm(12);
		loanApi.applyForLoan(applyForLoanBean);
	}
	
	@Test(expected = HttpServerErrorException.class)
	public void applyForLoanSecondTime() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		
		List<LoanBean> loans = loanApi.listLoans();
		assertThat(loans, hasSize(1));
		assertThat(loans.get(0).getDueDate(), is(LocalDate.now().plusMonths(12)));
		assertThat(loans.get(0).getPrincipal(), is(amount("300")));
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("30.14")));
		
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
	}
	
	@Test
	public void applyForLoanMoreThan3TimesInARow() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		loanApi.payLoan();
		
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		loanApi.payLoan();
		
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		loanApi.payLoan();
		
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.OPEN));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.MANUAL));
		assertThat(loanApplication.getManualWarnings(), is("Reached max applications (e.g. 3) per day from a single IP"));
	}
	
	@Test
	public void applyForLoanAfterMidnightWithMaximumAmountAvailable() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(500));
		applyForLoanBean.setTerm(12);
		applyForLoanBean.setWhen(LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(1));
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.OPEN));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.MANUAL));
		assertThat(loanApplication.getManualWarnings(), is("The attempt to take loan is made after 00:00 with max possible amount"));
	}
	
	@Test
	public void applyForLoanAndCreateExtension2Extensions() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin("modula5");
		clientRegisterBean.setPassword("test12345");
		clientApi.register(clientRegisterBean);
		
		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.APPROVED));
		
		List<LoanBean> loans = loanApi.listLoans();
		assertThat(loans, hasSize(1));
		assertThat(loans.get(0).getDueDate(), is(LocalDate.now().plusMonths(12)));
		assertThat(loans.get(0).getPrincipal(), is(amount("300")));
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("30.14")));
		
		CreateExtensionBean createExtensionBean = new CreateExtensionBean();
		createExtensionBean.setTerm(2);
		loanApi.createExtension(createExtensionBean);
		
		loans = loanApi.listLoans();
		assertThat(loans, hasSize(1));
		assertThat(loans.get(0).getDueDate(), is(LocalDate.now().plusMonths(14)));
		assertThat(loans.get(0).getLoanExtensions(), hasSize(1));
		assertThat(loans.get(0).getLoanExtensions().get(0).getTerm(), is(2));
		assertThat(loans.get(0).getExtensionPayment(), is(amount("36.00")));
		
		createExtensionBean = new CreateExtensionBean();
		createExtensionBean.setTerm(3);
		loanApi.createExtension(createExtensionBean);
		
		loans = loanApi.listLoans();
		assertThat(loans, hasSize(1));
		assertThat(loans.get(0).getDueDate(), is(LocalDate.now().plusMonths(17)));
		assertThat(loans.get(0).getLoanExtensions(), hasSize(2));
		assertThat(loans.get(0).getLoanExtensions().stream().min(comparing(LoanExtensionBean::getCreated)).get().getTerm(), is(2));
		assertThat(loans.get(0).getLoanExtensions().stream().max(comparing(LoanExtensionBean::getCreated)).get().getTerm(), is(3));
		assertThat(loans.get(0).getExtensionPayment(), is(amount("90.00")));
		
	}

}
