package io.fourfinanceit.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;

@Component
public class ClientApi extends BaseApi {
	
	public void register(ClientRegisterBean clientRegisterBean) {
		ResponseEntity<String> response = this.restTemplate.postForEntity(getBasePath() + "/public/clients/register", clientRegisterBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
	}
	
	public void login(ClientLoginBean clientLoginBean) {
		ResponseEntity<String> response = this.restTemplate.postForEntity(getBasePath() +"/public/clients/login", clientLoginBean, String.class);
		assertThat(response.getStatusCodeValue(), is(200));
	}

}
