package com.oreilly.security.config;

import javax.annotation.Priority;
import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.hibernate.engine.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
@Configuration
public class NewApplicationConfiguration implements WebMvcConfigurer{
	
	public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = 
			"select g.id, g.group_name, ga.authority from groups g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp").viewClass(JstlView.class);
		WebMvcConfigurer.super.configureViewResolvers(registry);
	}
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	        
	    }
	    @Bean
	    ServletRegistrationBean<WebServlet> h2servletRegistration(){
	        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<WebServlet>( new WebServlet());
	        registrationBean.addUrlMappings("/h2-console/*");
	        return registrationBean;
	    }
	    

	    @SuppressWarnings("deprecation")
	    @Bean
	    public static NoOpPasswordEncoder passwordEncoder() {
	    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	    }
	    @Bean
	    public DataSource dataSource() {
	    	return new EmbeddedDatabaseBuilder()
	    			.setType(EmbeddedDatabaseType.H2)
	    			.addScript("schema2-h2.sql")
	    			.build();
	    	 
	    }
	    @Bean
	    @Primary
	    public UserDetailsService userDetailsServiceJdbc() {
	    	
	    	JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
	    	jdbcDao.setDataSource(this.dataSource());
	    	//jdbcDao.setGroupAuthoritiesByUsernameQuery(DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY);
	    	return jdbcDao;
	    }

	

}
