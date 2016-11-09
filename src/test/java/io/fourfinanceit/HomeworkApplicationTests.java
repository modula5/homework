package io.fourfinanceit;

import static io.fourfinanceit.util.Utils.amount;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;
import io.fourfinanceit.enums.LoanApplicationResolution;
import io.fourfinanceit.enums.LoanApplicationStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class HomeworkApplicationTests {

	@Autowired
    private TestRestTemplate restTemplate;
	
	@Test
	public void registerClient() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(RandomStringUtils.randomAlphanumeric(20));
		clientRegisterBean.setPassword("test12345");
		ResponseEntity<String> response = this.restTemplate.postForEntity("/public/clients/register", clientRegisterBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
	}
	
	@Test
	public void registerClientThenLogin() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		String randomLogin = RandomStringUtils.randomAlphanumeric(20);
		clientRegisterBean.setLogin(randomLogin);
		clientRegisterBean.setPassword("test12345");
		ResponseEntity<String> response = this.restTemplate.postForEntity("/public/clients/register", clientRegisterBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
		
		ClientLoginBean clientLoginBean = new ClientLoginBean();
		clientLoginBean.setLogin(randomLogin);
		clientLoginBean.setPassword("test12345");
		response = this.restTemplate.postForEntity("/public/clients/login", clientLoginBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
	}
	
	@Test
	public void applyForLoan() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		String randomLogin = RandomStringUtils.randomAlphanumeric(20);
		clientRegisterBean.setLogin(randomLogin);
		clientRegisterBean.setPassword("test12345");
		ResponseEntity<String> response = this.restTemplate.postForEntity("/public/clients/register", clientRegisterBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
		
		String cookie = response.getHeaders().get("Set-Cookie").get(0);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);
		
		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		ResponseEntity<LoanApplicationBean> applyResponse = this.restTemplate.exchange("/loans/apply", HttpMethod.POST, new HttpEntity<ApplyForLoanBean>(applyForLoanBean, headers), LoanApplicationBean.class);
		assertThat(applyResponse.getStatusCodeValue(), is(200));
		
		ResponseEntity<List<LoanBean>> registeredLoansResponse = this.restTemplate.exchange("/loans/list", HttpMethod.GET, new HttpEntity<Void>(headers), new ParameterizedTypeReference<List<LoanBean>>() {});
		assertThat(registeredLoansResponse.getStatusCodeValue(), is(200));
		assertThat(registeredLoansResponse.getBody(), hasSize(1));
		assertThat(registeredLoansResponse.getBody().get(0).getDueDate(), is(LocalDate.now().plusMonths(12)));
		assertThat(registeredLoansResponse.getBody().get(0).getPrincipal(), is(amount("300")));
		assertThat(registeredLoansResponse.getBody().get(0).getMonthlyPayment(), is(amount("94.04")));
	}
	
	@Test
	public void applyForLoanAndCreateExtension() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		String randomLogin = RandomStringUtils.randomAlphanumeric(20);
		clientRegisterBean.setLogin(randomLogin);
		clientRegisterBean.setPassword("test12345");
		ResponseEntity<String> response = this.restTemplate.postForEntity("/public/clients/register", clientRegisterBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
		
		String cookie = response.getHeaders().get("Set-Cookie").get(0);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);
		
		ApplyForLoanBean applyForLoanBean = new ApplyForLoanBean();
		applyForLoanBean.setAmount(BigDecimal.valueOf(300));
		applyForLoanBean.setTerm(12);
		ResponseEntity<LoanApplicationBean> applyResponse = this.restTemplate.exchange("/loans/apply", HttpMethod.POST, new HttpEntity<ApplyForLoanBean>(applyForLoanBean, headers), LoanApplicationBean.class);
		assertThat(applyResponse.getStatusCodeValue(), is(200));
		assertThat(applyResponse.getBody().getStatus(), is(LoanApplicationStatus.CLOSED));
		assertThat(applyResponse.getBody().getResolution(), is(LoanApplicationResolution.APPROVED));
		
		ResponseEntity<List<LoanBean>> registeredLoansResponse = this.restTemplate.exchange("/loans/list", HttpMethod.GET, new HttpEntity<Void>(headers), new ParameterizedTypeReference<List<LoanBean>>() {});
		assertThat(registeredLoansResponse.getStatusCodeValue(), is(200));
		assertThat(registeredLoansResponse.getBody(), hasSize(1));
		assertThat(registeredLoansResponse.getBody().get(0).getDueDate(), is(LocalDate.now().plusMonths(12)));
		assertThat(registeredLoansResponse.getBody().get(0).getPrincipal(), is(amount("300")));
		assertThat(registeredLoansResponse.getBody().get(0).getMonthlyPayment(), is(amount("94.04")));
		
		CreateExtensionBean createExtensionBean = new CreateExtensionBean();
		createExtensionBean.setTerm(2);
		ResponseEntity<LoanExtensionBean> extensionResponse = this.restTemplate.exchange("/loans/extension", HttpMethod.POST, new HttpEntity<CreateExtensionBean>(createExtensionBean, headers), LoanExtensionBean.class);
		assertThat(extensionResponse.getStatusCodeValue(), is(200));
		
		registeredLoansResponse = this.restTemplate.exchange("/loans/list", HttpMethod.GET, new HttpEntity<Void>(headers), new ParameterizedTypeReference<List<LoanBean>>() {});
		assertThat(registeredLoansResponse.getStatusCodeValue(), is(200));
		assertThat(registeredLoansResponse.getBody(), hasSize(1));
		assertThat(registeredLoansResponse.getBody().get(0).getDueDate(), is(LocalDate.now().plusMonths(14)));
		assertThat(registeredLoansResponse.getBody().get(0).getLoanExtensions(), hasSize(1));
		assertThat(registeredLoansResponse.getBody().get(0).getLoanExtensions().get(0).getTerm(), is(2));
		assertThat(registeredLoansResponse.getBody().get(0).getExtensionPayment(), is(amount("36.00")));
		
		createExtensionBean = new CreateExtensionBean();
		createExtensionBean.setTerm(3);
		extensionResponse = this.restTemplate.exchange("/loans/extension", HttpMethod.POST, new HttpEntity<CreateExtensionBean>(createExtensionBean, headers), LoanExtensionBean.class);
		assertThat(extensionResponse.getStatusCodeValue(), is(200));
		
		registeredLoansResponse = this.restTemplate.exchange("/loans/list", HttpMethod.GET, new HttpEntity<Void>(headers), new ParameterizedTypeReference<List<LoanBean>>() {});
		assertThat(registeredLoansResponse.getStatusCodeValue(), is(200));
		assertThat(registeredLoansResponse.getBody(), hasSize(1));
		assertThat(registeredLoansResponse.getBody().get(0).getDueDate(), is(LocalDate.now().plusMonths(17)));
		assertThat(registeredLoansResponse.getBody().get(0).getLoanExtensions(), hasSize(2));
		assertThat(registeredLoansResponse.getBody().get(0).getLoanExtensions().stream().min(Comparator.comparing(LoanExtensionBean::getCreated)).get().getTerm(), is(2));
		assertThat(registeredLoansResponse.getBody().get(0).getLoanExtensions().stream().max(Comparator.comparing(LoanExtensionBean::getCreated)).get().getTerm(), is(3));
		assertThat(registeredLoansResponse.getBody().get(0).getExtensionPayment(), is(amount("90.00")));
		
	}

}
