package io.fourfinanceit.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;

@Component
public class LoanApi extends BaseApi {
	
	public LoanApplicationBean applyForLoan(ApplyForLoanBean applyForLoanBean) {
		HttpEntity<ApplyForLoanBean> entity = new HttpEntity<ApplyForLoanBean>(applyForLoanBean);
		ResponseEntity<LoanApplicationBean> applyResponse = this.restTemplate.exchange(getBasePath() +"/loans/apply", HttpMethod.POST, entity, LoanApplicationBean.class);
		assertThat(applyResponse.getStatusCodeValue(), is(200));
		return applyResponse.getBody();
	}
	
	public void payLoan() {
		ResponseEntity<Void> applyResponse = this.restTemplate.exchange(getBasePath() +"/loans/pay", HttpMethod.POST, null, Void.class);
		assertThat(applyResponse.getStatusCodeValue(), is(200));
	}
	
	public List<LoanBean> listLoans() {
		ResponseEntity<List<LoanBean>> registeredLoansResponse = this.restTemplate.exchange(getBasePath() +"/loans/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<LoanBean>>() {});
		assertThat(registeredLoansResponse.getStatusCodeValue(), is(200));
		return registeredLoansResponse.getBody();
	}
	
	public LoanExtensionBean createExtension(CreateExtensionBean createExtensionBean) {
		HttpEntity<CreateExtensionBean> entity = new HttpEntity<CreateExtensionBean>(createExtensionBean);
		ResponseEntity<LoanExtensionBean> extensionResponse = this.restTemplate.exchange(getBasePath() +"/loans/extension", HttpMethod.POST, entity, LoanExtensionBean.class);
		assertThat(extensionResponse.getStatusCodeValue(), is(200));
		return extensionResponse.getBody();
	}

}
