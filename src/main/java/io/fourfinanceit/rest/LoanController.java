package io.fourfinanceit.rest;

import static io.fourfinanceit.util.Constants.CLIENT_SESSION_ID;
import static io.fourfinanceit.util.Utils.getRemoteAddress;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.fourfinanceit.beans.ApplyForLoanBean;
import io.fourfinanceit.beans.CreateExtensionBean;
import io.fourfinanceit.beans.LoanApplicationBean;
import io.fourfinanceit.beans.LoanBean;
import io.fourfinanceit.beans.LoanExtensionBean;
import io.fourfinanceit.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {

	@Autowired private LoanService loanService;
	
	@RequestMapping(value = "/apply", method = RequestMethod.POST, consumes="application/json")
	public LoanApplicationBean apply(@RequestBody ApplyForLoanBean applyForLoanBean, HttpServletRequest request, HttpSession httpSession) {
		applyForLoanBean.setClientId((Long) httpSession.getAttribute(CLIENT_SESSION_ID));
		applyForLoanBean.setAddress(getRemoteAddress(request));
		return loanService.apply(applyForLoanBean);
	}
	
	@RequestMapping(value = "/extension", method = RequestMethod.POST, consumes="application/json")
	public LoanExtensionBean createExtension(@RequestBody CreateExtensionBean createExtensionBean, HttpSession httpSession) {
		createExtensionBean.setClientId((Long) httpSession.getAttribute(CLIENT_SESSION_ID));
		return loanService.createExtension(createExtensionBean);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Transactional
	public List<LoanBean> list(HttpSession httpSession) {
		return loanService.list((Long) httpSession.getAttribute(CLIENT_SESSION_ID));
	}
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public void pay(HttpSession httpSession) {
		loanService.payLoan((Long) httpSession.getAttribute(CLIENT_SESSION_ID));
	}
	
	@RequestMapping(value = "/last_open", method = RequestMethod.GET)
	public LoanBean lastOpen(HttpSession httpSession) {
		return loanService.lastOpenLoan((Long) httpSession.getAttribute(CLIENT_SESSION_ID));
	}

}
