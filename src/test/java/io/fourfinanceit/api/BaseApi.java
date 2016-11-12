package io.fourfinanceit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import io.fourfinanceit.util.ErrorBean;
import io.fourfinanceit.util.RestUtil;

public class BaseApi {
	
	@Autowired
    protected RestTemplate restTemplate;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	public static int port;
	
	protected void checkResponse(ResponseEntity<String> response) {
		try {
			if (RestUtil.isError(response.getStatusCode())) {
				ErrorBean errorBean = objectMapper.readValue(response.getBody(), ErrorBean.class);
				throw new RestClientException(errorBean.getMessage());
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T convertResponse(ResponseEntity<String> response, Class<?> targetClass) {
		T resonseValue = null;
		try {
			if (RestUtil.isError(response.getStatusCode())) {
				ErrorBean errorBean = objectMapper.readValue(response.getBody(), ErrorBean.class);
				throw new RestClientException(errorBean.getMessage());
			} else {
				resonseValue = (T) objectMapper.readValue(response.getBody(), targetClass);
			}				
		}	catch (Exception e) {
			Throwables.propagate(e);
		}
		return resonseValue;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T convertResponse(ResponseEntity<String> response, TypeReference<T> target) {
		T resonseValue = null;
		try {
			if (RestUtil.isError(response.getStatusCode())) {
				ErrorBean errorBean = objectMapper.readValue(response.getBody(), ErrorBean.class);
				throw new RestClientException(errorBean.getMessage());
			} else {
				resonseValue = (T) objectMapper.readValue(response.getBody(), target);
			}				
		}	catch (Exception e) {
			Throwables.propagate(e);
		}
		return resonseValue;
	}
	
	protected String getBasePath() {
		return "http://localhost:" + port;
	}

}
