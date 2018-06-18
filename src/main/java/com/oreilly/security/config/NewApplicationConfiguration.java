package com.oreilly.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.hibernate.engine.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
@Configuration
public class NewApplicationConfiguration implements WebMvcConfigurer{
	
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp").viewClass(JstlView.class);
		WebMvcConfigurer.super.configureViewResolvers(registry);
	}
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/src/main/webapp/**").addResourceLocations("/src/main/webapp");
	        
	    }
	    @Bean
	    ServletRegistrationBean<WebServlet> h2servletRegistration(){
	        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<WebServlet>( new WebServlet());
	        registrationBean.addUrlMappings("/h2-console/*");
	        return registrationBean;
	    }
	    

	    @SuppressWarnings("deprecation")
	    @Bean
	    @Profile("ldap")
	    public static NoOpPasswordEncoder passwordEncoder() {
	    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	    }
	    @Bean
	    @Primary
	    public DataSource dataSource() {
	    	return new EmbeddedDatabaseBuilder()
	    			.setType(EmbeddedDatabaseType.H2)
	    			.addScript("schema-group-h2.sql")
	    			.build();
	    	 
	    }
	    @Bean
	    @Primary
	    @Profile(value="jdbc")
	    public UserDetailsService userDetailsServiceJdbc() {
	    	
	    	JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
	    	jdbcDao.setDataSource(this.dataSource());
	    	jdbcDao.setGroupAuthoritiesByUsernameQuery(JdbcDaoImpl.DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY);
	    	jdbcDao.setEnableGroups(true);
	    	
	    	return jdbcDao;
	    }
	    @Bean
	    @Primary
	    // Przykład z dokumentacji, mi nie działa do jdbc
	    public PasswordEncoder passwordEncoders() {
	    	String idForEncode = "bcrypt";
	    	Map<String, PasswordEncoder> encoders = new HashMap<>();
	    	
	    	encoders.put(idForEncode, new BCryptPasswordEncoder());
	    	encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
	    	encoders.put("scrypt", new SCryptPasswordEncoder());
	    	DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(idForEncode,encoders);
	    	encoder.setDefaultPasswordEncoderForMatches(encoders.get(idForEncode));
	    	return encoder;
	    }

	

}
