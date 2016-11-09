package io.fourfinanceit.rest;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;
import io.fourfinanceit.service.ClientService;
import io.fourfinanceit.util.Constants;

@RestController
@RequestMapping("/public/clients")
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired(required = false)
	private HttpSession httpSession;
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(ClientController.class);
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes="application/json")
	public void register(@RequestBody ClientRegisterBean clientRegisterBean) {
		clientService.register(clientRegisterBean);
		LOGGER.info("User {} has been successfully registered", clientRegisterBean.getLogin());
		
		ClientLoginBean clientLoginBean = new ClientLoginBean();
		BeanUtils.copyProperties(clientRegisterBean, clientLoginBean);
		login(clientLoginBean);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody ClientLoginBean clientLoginBean) {
		httpSession.setAttribute(Constants.CLIENT_SESSION_ID, clientService.login(clientLoginBean));
		LOGGER.info("User {} has been successfully loggedin", clientLoginBean.getLogin());
	}

}
