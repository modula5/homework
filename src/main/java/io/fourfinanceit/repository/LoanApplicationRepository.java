package io.fourfinanceit.repository;

import static io.fourfinanceit.enums.LoanApplicationStatus.OPEN;
import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIN;
import static org.hibernate.criterion.Projections.count;
import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.LoanApplication;

@Repository
public class LoanApplicationRepository extends BaseRepository<LoanApplication> {

	public LoanApplicationRepository() {
		super(LoanApplication.class);
	}
	
	public Long getApplicationsCountOnThisDay(String address) {
		LocalDateTime now = LocalDateTime.now();
		return (Long) criteria().add(eq("address", address))
				.add(between("created", now.with(MIN), now.with(MAX))).setProjection(count("id")).uniqueResult();
	}
	
	public LoanApplication getLastOpenApplication(Client client) {
		return (LoanApplication) criteria().add(eq("client", client)).add(eq("status", OPEN)).uniqueResult();
	}

}
