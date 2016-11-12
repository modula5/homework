package io.fourfinanceit.api;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;

@Component
public class LoanApi extends BaseApi {
	
	public LoanApplicationBean applyForLoan(ApplyForLoanBean applyForLoanBean) {
		HttpEntity<ApplyForLoanBean> entity = new HttpEntity<ApplyForLoanBean>(applyForLoanBean);
		ResponseEntity<String> applyResponse = this.restTemplate.exchange(getBasePath() +"/loans/apply", HttpMethod.POST, entity, String.class);
		return convertResponse(applyResponse, LoanApplicationBean.class);
	}
	
	public void payLoan() {
		ResponseEntity<String> payResponse = this.restTemplate.exchange(getBasePath() +"/loans/pay", HttpMethod.POST, null, String.class);
		checkResponse(payResponse);
	}
	
	public List<LoanBean> listLoans() {
		ResponseEntity<String> registeredLoansResponse = this.restTemplate.exchange(getBasePath() +"/loans/list", HttpMethod.GET, null, String.class);
		return convertResponse(registeredLoansResponse, new TypeReference<List<LoanBean>>() {});
	}
	
	public LoanExtensionBean createExtension(CreateExtensionBean createExtensionBean) {
		HttpEntity<CreateExtensionBean> entity = new HttpEntity<CreateExtensionBean>(createExtensionBean);
		ResponseEntity<String> extensionResponse = this.restTemplate.exchange(getBasePath() +"/loans/extension", HttpMethod.POST, entity, String.class);
		return convertResponse(extensionResponse, LoanExtensionBean.class);
	}

}
