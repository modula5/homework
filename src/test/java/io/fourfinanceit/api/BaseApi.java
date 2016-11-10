package io.fourfinanceit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class BaseApi {
	
	@Autowired
    protected RestTemplate restTemplate;
	public static int port;
	
	protected String getBasePath() {
		return "http://localhost:" + port;
	}

}
