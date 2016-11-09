package io.fourfinanceit.configuration;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.hibernate.EmptyInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.fourfinanceit.domain.BaseEntity;
import io.fourfinanceit.util.Constants;

@Configuration
public class Config extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}
	
	@Bean
	@DependsOn("updatedPropertyInterceptor")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(UpdatedPropertyInterceptor updatedPropertyInterceptor) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

		Properties additionalProperties = new Properties();
		additionalProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		additionalProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		additionalProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		additionalProperties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate4.SpringSessionContext");
		additionalProperties.put("hibernate.ejb.interceptor", updatedPropertyInterceptor);
		entityManagerFactory.setJpaProperties(additionalProperties);

		return entityManagerFactory;
	}
	
	@Bean
	public JpaTransactionManager transactionManager(SessionFactory sessionFactory) {
		JpaTransactionManager  transactionManager = new JpaTransactionManager();
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean
    public SessionFactory sessionFactory(EntityManagerFactory emf) {
        if (emf.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return emf.unwrap(SessionFactory.class);
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				if (request.getRequestURI().startsWith("/public")
						|| request.getRequestURI().startsWith("/error")) {
					return true;
				}
				if (request.getSession().getAttribute(Constants.CLIENT_SESSION_ID) == null) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
				return true;
			}
			
			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
					throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
		super.addInterceptors(registry);
	}
	
	@Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
	
	@SuppressWarnings("serial")
	@Component("updatedPropertyInterceptor")
	public class UpdatedPropertyInterceptor extends EmptyInterceptor {
	   @Override
		public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
			if (entity instanceof BaseEntity) {
				((BaseEntity) entity).setUpdated(LocalDateTime.now());
			}
			return super.onSave(entity, id, state, propertyNames, types);
		}
	   
	   @Override
		public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
				String[] propertyNames, Type[] types) {
			if (entity instanceof BaseEntity) {
				((BaseEntity) entity).setUpdated(LocalDateTime.now());
			}
			return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
		}
	}

}
