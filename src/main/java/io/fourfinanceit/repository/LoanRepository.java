package io.fourfinanceit.repository;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.enums.LoanStatus;

@SuppressWarnings("unchecked")
@Repository
public class LoanRepository extends BaseRepository<Loan> {

	public LoanRepository() {
		super(Loan.class);
	}
	
	public List<Loan> list(Long clientId) {
		return criteria().add(Restrictions.eq("client.id", clientId)).list();
	}
	
	public Loan getLastOpenLoan(Client client) {
		return (Loan) criteria().add(eq("client", client)).add(eq("status", LoanStatus.OPEN)).uniqueResult();
	}

}
