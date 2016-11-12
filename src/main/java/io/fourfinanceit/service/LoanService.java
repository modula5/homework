package io.fourfinanceit.service;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static io.fourfinanceit.util.Utils.amount;
import static io.fourfinanceit.util.Utils.calculateMonthlyPayment;
import static io.fourfinanceit.util.Utils.isLe;
import static java.time.LocalDate.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;
import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanApplication;
import io.fourfinanceit.domain.LoanExtension;
import io.fourfinanceit.enums.LoanStatus;
import io.fourfinanceit.repository.EntityRepository;
import io.fourfinanceit.repository.LoanRepository;
import io.fourfinanceit.util.Constants;

@Service
public class LoanService {
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private RiskService riskService;
	
	@Autowired
	private EntityRepository entityRepository;

	@Autowired
	private Environment environment;
	
	private BigDecimal maxAmount;
	private BigDecimal interestPerMonth;
	private BigDecimal extensionInterestPerMonth;
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(LoanService.class);
	
	@PostConstruct
	public void init() {
		maxAmount = amount(environment.getProperty("loan.max.amount"));
		interestPerMonth = amount(environment.getProperty("loan.interest.month"));
		extensionInterestPerMonth = amount(environment.getProperty("loan.extension.interest.month"));
	}
	@Transactional
	public LoanApplicationBean apply(ApplyForLoanBean applyForLoanBean) {
		Client client = entityRepository.get(applyForLoanBean.getClientId(), Client.class);
		
		checkArgument(!client.hasOpenLoans(), "Client has already one open loan");
		checkArgument(isLe(applyForLoanBean.getAmount(), maxAmount),
				"Attempt to take %s with max amount permitted %s", applyForLoanBean.getAmount(), maxAmount);
		
		LoanApplication loanApplication = new LoanApplication();
		BeanUtils.copyProperties(applyForLoanBean, loanApplication);
		loanApplication.setClient(client);
		
		LocalDateTime when = firstNonNull(applyForLoanBean.getWhen(), LocalDateTime.now());
		boolean evaluateSuccess = riskService.evaluate(when, applyForLoanBean, loanApplication);
		if (evaluateSuccess) {
			createLoan(applyForLoanBean, client, loanApplication);
			loanApplication.approve();	
			LOGGER.info("Loan has been successfully approved");
		} else {
			LOGGER.info("Loan application turned to manual resolution mode and should be approved manually by operator");
		}

		entityRepository.persist(loanApplication);
		return loanApplication.toBean();
	}
	private void createLoan(ApplyForLoanBean applyForLoanBean, Client client, LoanApplication loanApplication) {
		Loan loan = new Loan();
		loan.setPrincipal(applyForLoanBean.getAmount());
		loan.setInterest(interestPerMonth);
		loan.setMonthlyPayment(calculateMonthlyPayment(applyForLoanBean.getAmount(), applyForLoanBean.getTerm(), interestPerMonth));
		loan.setDueDate(now().plusMonths(applyForLoanBean.getTerm()));
		loan.setClient(client);
		loan.setLoanApplication(loanApplication);
		entityRepository.persist(loan);
	}
	
	@Transactional
	public void payLoan(Long clientId) {
		Client client = entityRepository.get(clientId, Client.class);
		Loan lastOpenLoan = loanRepository.getLastOpenLoan(client);	
		checkArgument(lastOpenLoan != null, "Client does not have any open loan at the moment");
		lastOpenLoan.setStatus(LoanStatus.PAID);
	}
	
	@Transactional
	public LoanExtensionBean createExtension(CreateExtensionBean createExtensionBean) {
		Client client = entityRepository.get(createExtensionBean.getClientId(), Client.class);
		Loan lastOpenLoan = loanRepository.getLastOpenLoan(client);
		if (lastOpenLoan == null) {
			throw new IllegalStateException("Client does not have any open loans at the moment to be able to do extensions");
		}
		BigDecimal extensionPayment = lastOpenLoan.getPrincipal().multiply(extensionInterestPerMonth).divide(Constants.ONE_HUNDRED, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(createExtensionBean.getTerm()));
		lastOpenLoan.setExtensionPayment(lastOpenLoan.getExtensionPayment() != null ? lastOpenLoan.getExtensionPayment().add(extensionPayment) : extensionPayment);
		LoanExtension loanExtension = new LoanExtension();
		loanExtension.setLoan(lastOpenLoan);
		loanExtension.setTerm(createExtensionBean.getTerm());
		lastOpenLoan.getLoanExtensions().add(loanExtension);
		
		lastOpenLoan.setDueDate(lastOpenLoan.getDueDate().plusMonths(loanExtension.getTerm()));		
		return loanExtension.toBean();
	}
	
	@Transactional(readOnly = true)
	public List<LoanBean> list(Long clientId) {
		return loanRepository.list(clientId).stream().map(Loan::toBean).collect(toList());
	}
	
	@Transactional(readOnly = true)
	public LoanBean lastOpenLoan(Long clientId) {
		Client client = entityRepository.get(clientId, Client.class);
		return ofNullable(loanRepository.getLastOpenLoan(client)).map(loan -> loan.toBean()).orElse(new LoanBean());
	}

}
