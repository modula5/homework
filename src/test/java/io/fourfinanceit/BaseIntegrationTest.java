package io.fourfinanceit;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.fourfinanceit.api.BaseApi;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
	
	@LocalServerPort
	private int port;
	
	@Configuration
	@ComponentScan("io.fourfinanceit")
	public static class SpringConfig {

	}
	
	@PostConstruct
	public void init() {
		BaseApi.port = port;
	}
}

