package io.fourfinanceit.service;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.fourfinanceit.beans.ClientLoginBean;
import io.fourfinanceit.beans.ClientRegisterBean;
import io.fourfinanceit.domain.Client;
import io.fourfinanceit.repository.ClientRepository;
import io.fourfinanceit.repository.EntityRepository;

@Service
public class ClientService {
	
	@Autowired
	private EntityRepository entityRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional
	public Long register(ClientRegisterBean clientRegisterBean) {
		boolean clientExists = clientRepository.checkClientExists(clientRegisterBean.getLogin());
		if (clientExists) {
			throw new IllegalStateException(String.format("Client with login : %s already exists", clientRegisterBean.getLogin()));
		}
		Client client = new Client();
		client.setLogin(clientRegisterBean.getLogin());
		client.setPassword(md5Hex(clientRegisterBean.getPassword()));
		entityRepository.persist(client);
		return client.getId();
	}
	
	@Transactional
	public Long login(ClientLoginBean clientLoginBean) {
		Long clientId = clientRepository.findClient(clientLoginBean.getLogin(), md5Hex(clientLoginBean.getPassword()));
		if (clientId == null) {
			throw new IllegalStateException(String.format("Unsuccessful attempt. Login : %s", clientLoginBean.getLogin()));
		}
		return clientId;
	}

}
