package io.fourfinanceit.config;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import io.fourfinanceit.util.RestUtil;

@Configuration
public class TestConfig {

	@Bean
	@Primary
	public RestTemplate statefulRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		StatefullRequestFactory statefullRequestFactory = new StatefullRequestFactory(httpClient, httpContext);		
		restTemplateBuilder = restTemplateBuilder.requestFactory(statefullRequestFactory).errorHandler(new ErrorlessResponseErrorHandler());
		return restTemplateBuilder.build();
	}
	
	public class StatefullRequestFactory extends HttpComponentsClientHttpRequestFactory {

		private final HttpContext httpContext;

		public StatefullRequestFactory(HttpClient httpClient, HttpContext httpContext) {
			super(httpClient);
			this.httpContext = httpContext;
		}

		@Override
		protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
			return this.httpContext;
		}
	}
	
	public class ErrorlessResponseErrorHandler implements ResponseErrorHandler {
		
	    @Override
	    public void handleError(ClientHttpResponse response) throws IOException {
	    	// skip
	    }

	    @Override
	    public boolean hasError(ClientHttpResponse response) throws IOException {
	    	return RestUtil.isError(response.getStatusCode());
	    }
	}
}
