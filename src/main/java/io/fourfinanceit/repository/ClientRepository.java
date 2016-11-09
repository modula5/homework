package io.fourfinanceit.repository;

import static org.hibernate.criterion.Projections.id;
import static org.hibernate.criterion.Restrictions.eq;

import org.springframework.stereotype.Repository;

import io.fourfinanceit.domain.Client;

@Repository
public class ClientRepository extends BaseRepository<Client> {

	public ClientRepository() {
		super(Client.class);
	}
	
	public boolean checkClientExists(String login) {
		return criteria().add(eq("login", login)).setProjection(id()).uniqueResult() != null;
	}
	
	public Long findClient(String login, String password) {
		return (Long) criteria().add(eq("login", login)).add(eq("password", password)).setProjection(id()).uniqueResult();
	}

}
