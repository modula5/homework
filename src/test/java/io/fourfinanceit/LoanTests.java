package io.fourfinanceit;

import static io.fourfinanceit.util.Utils.amount;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import io.fourfinanceit.api.ClientApi;
import io.fourfinanceit.api.LoanApi;
import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.ClientRegisterBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;
import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.enums.LoanApplicationStatus;

public class LoanTests extends BaseIntegrationTest {

	private static final String MAX_APP_MANUAL = "Reached max applications (e.g. 3) per day from a single IP";

	private static final String AFTER_MIDNIGHT_MANUAL = "The attempt to take loan is made after 00:00 with max possible amount";

	private static final String PASSWORD = "test12345";

	private static final String LOGIN = "modula5";

	@Autowired
	private ClientApi clientApi;
	
	@Autowired
	private LoanApi loanApi;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void applyForLoan() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
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
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("29.25")));
	}
	
	@Test
	public void applyForLoanMoreThanMax() {
		expectedEx.expect(RestClientException.class);
		expectedEx.expectMessage("Attempt to take 700.00 with max amount permitted 500.00");
		
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(700));
		applyForLoanBean.setTerm(12);
		loanApi.applyForLoan(applyForLoanBean);
	}
	
	@Test
	public void applyForLoanSecondTime() {
		expectedEx.expect(RestClientException.class);
		expectedEx.expectMessage("Client has already one open loan");
		
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
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
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("29.25")));
		
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
	}
	
	@Test
	public void applyForLoanMoreThan3TimesInARow() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
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
		assertThat(loanApplication.getManualWarnings(), is(MAX_APP_MANUAL));
	}
	
	@Test
	public void applyForLoanAfterMidnightWithMaximumAmountAvailable() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
		clientApi.register(clientRegisterBean);

		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(500));
		applyForLoanBean.setTerm(12);
		applyForLoanBean.setWhen(LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(1));
		LoanApplicationBean loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.OPEN));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.MANUAL));
		assertThat(loanApplication.getManualWarnings(), is(AFTER_MIDNIGHT_MANUAL));
	}
	
	@Test
	public void applyForLoanMoreThan3TimesInARowAndLastAfterMidnightWithMaxAmount() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
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
		
		applyForLoanBean.setAmount(BigDecimal.valueOf(500));
		applyForLoanBean.setWhen(LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(1));
		loanApplication = loanApi.applyForLoan(applyForLoanBean);
		assertThat(loanApplication.getStatus(), is(LoanApplicationStatus.OPEN));
		assertThat(loanApplication.getResolution(), is(LoanApplicationResolution.MANUAL));
		assertThat(loanApplication.getManualWarnings(), is(Stream.of(AFTER_MIDNIGHT_MANUAL, MAX_APP_MANUAL).collect(joining("\n"))));
	}
	
	@Test
	public void applyForLoanAndCreateExtension2Extensions() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
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
		assertThat(loans.get(0).getMonthlyPayment(), is(amount("29.25")));
		
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
