package com.oreilly.security.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled=true, prePostEnabled=true)
public class AclConfiguration {
	
	@Autowired
	DataSource dataSource;
	@Bean
	MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer());
		expressionHandler.setPermissionEvaluator(permissionEvaluator());
		return expressionHandler;
	}
	@Bean
	public SimpleGrantedAuthority adminAuthority() {
		return new SimpleGrantedAuthority("ROLE_ADMINISTRATOR");
	}
	@Bean
	public AclAuthorizationStrategyImpl aclAuthStrategy() {
		return new AclAuthorizationStrategyImpl(adminAuthority()
				);
	}
	@Bean
	public ConsoleAuditLogger auditLogger() {
		return new ConsoleAuditLogger();
	}
	@Bean
	public EhCacheManagerFactoryBean aclCacheManager() {
	    return new EhCacheManagerFactoryBean();
	}
	@Bean
	public EhCacheFactoryBean aclEhCacheFactoryBean() {
	    EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
	    ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
	    ehCacheFactoryBean.setCacheName("aclCache");
	    return ehCacheFactoryBean;
	}
	@Bean
	public EhCacheBasedAclCache aclCache() {
		return new EhCacheBasedAclCache(
				aclEhCacheFactoryBean().getObject(), 
				new DefaultPermissionGrantingStrategy(auditLogger()), 
				aclAuthStrategy());
	}
	@Bean
	BasicLookupStrategy lookupStrategy() {
		return new BasicLookupStrategy(dataSource//dataSource()
				, aclCache(), aclAuthStrategy(), auditLogger());
	}
	@Bean
	JdbcMutableAclService aclService() {
		return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
	}
	
	@Bean
	AclPermissionEvaluator permissionEvaluator() {
		return new AclPermissionEvaluator(aclService());
	}
	@Bean
	AclPermissionCacheOptimizer permissionCacheOptimizer() {
		return new AclPermissionCacheOptimizer(aclService());
	}
	


}
