package io.fourfinanceit.rest;

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
	
	@Autowired(required = false)
	private HttpSession httpSession;
	
	@Autowired(required = false)
	private HttpServletRequest request;
	
	@RequestMapping(value = "/apply", method = RequestMethod.POST, consumes="application/json")
	public LoanApplicationBean apply(@RequestBody ApplyForLoanBean applyForLoanBean) {
		applyForLoanBean.setClientId((Long) httpSession.getAttribute("cid"));
		applyForLoanBean.setAddress(request.getRemoteAddr());
		return loanService.apply(applyForLoanBean);
	}
	
	@RequestMapping(value = "/extension", method = RequestMethod.POST, consumes="application/json")
	public LoanExtensionBean createExtension(@RequestBody CreateExtensionBean createExtensionBean) {
		createExtensionBean.setClientId((Long) httpSession.getAttribute("cid"));
		return loanService.createExtension(createExtensionBean);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Transactional
	public List<LoanBean> list() {
		return loanService.list((Long) httpSession.getAttribute("cid"));
	}
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public void pay() {
		loanService.payLoan((Long) httpSession.getAttribute("cid"));
	}
	
	@RequestMapping(value = "/last_open", method = RequestMethod.GET)
	public LoanBean lastOpen() {
		return loanService.lastOpenLoan((Long) httpSession.getAttribute("cid"));
	}

}
