package io.fourfinanceit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.fourfinanceit.api.ClientApi;
import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;

public class ClientTests extends BaseIntegrationTest {
	
	private static final String PASSWORD = "test12345";

	private static final String LOGIN = "modula5";

	@Autowired
	private ClientApi clientApi;

	@Test
	public void registerClient() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
		clientApi.register(clientRegisterBean);
	}
	
	@Test
	public void registerClientThenLogin() {
		ClientRegisterBean clientRegisterBean = new ClientRegisterBean();
		clientRegisterBean.setLogin(LOGIN);
		clientRegisterBean.setPassword(PASSWORD);
		clientApi.register(clientRegisterBean);
		
		ClientLoginBean clientLoginBean = new ClientLoginBean();
		clientLoginBean.setLogin(LOGIN);
		clientLoginBean.setPassword(PASSWORD);
		clientApi.login(clientLoginBean);
	}
}
