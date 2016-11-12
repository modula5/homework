package io.fourfinanceit.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;

@Component
public class ClientApi extends BaseApi {
	
	public void register(ClientRegisterBean clientRegisterBean) {
		ResponseEntity<String> clientRegisterResponse = this.restTemplate.postForEntity(getBasePath() + "/public/clients/register", clientRegisterBean, String.class);
		checkResponse(clientRegisterResponse);
	}
	
	public void login(ClientLoginBean clientLoginBean) {
		ResponseEntity<String> clientLoginResponse = this.restTemplate.postForEntity(getBasePath() +"/public/clients/login", clientLoginBean, String.class);
		checkResponse(clientLoginResponse);
	}

}
