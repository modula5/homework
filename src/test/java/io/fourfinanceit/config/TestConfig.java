package io.fourfinanceit.config;

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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

	@Bean
	@Primary
	public RestTemplate statefulRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		StatefullHttpComponentsClientHttpRequestFactory statefullHttpComponentsClientHttpRequestFactory = new StatefullHttpComponentsClientHttpRequestFactory(
				httpClient, httpContext);		
		restTemplateBuilder.requestFactory(statefullHttpComponentsClientHttpRequestFactory);
		return restTemplateBuilder.build();
	}
	
	public class StatefullHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

		private final HttpContext httpContext;

		public StatefullHttpComponentsClientHttpRequestFactory(HttpClient httpClient, HttpContext httpContext) {
			super(httpClient);
			this.httpContext = httpContext;
		}

		@Override
		protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
			return this.httpContext;
		}
	}
}
