package com.oreilly.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.oreilly.security.services.CustomAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
@ComponentScan(basePackages= {"com.oreilly.security"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private AuthenticationProvider provider;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsContextMapper userDetailsContextMapper;
	public WebSecurityConfig() {
		// TODO Auto-generated constructor stub
	}
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	            
	            	.antMatchers("/appointments/").access("hasRole('ADMIN') or hasRole('USER')")
	            	.antMatchers("/schedule/").access("hasAuthority('ROLE_ADMIN')")//.access("principal.username == 'kbowersox'")
	            	.antMatchers("/h2-console").permitAll()
	            	.antMatchers("/**").access("permitAll")//.access("hasAnyRole('ANONYMOUS','USER','ADMIN')")
	                .and()
	                .formLogin()
	                	.loginPage("/login")
	                	.loginProcessingUrl("/login")
	                	.usernameParameter("custom_username")
	                	.passwordParameter("custom_password")
	                	.defaultSuccessUrl("/appointments/")
	                	.failureUrl("/login?error=true")
	                .and()
	                	.logout()
	                	.logoutUrl("/logout")
	                	.logoutSuccessUrl("/login?logout=true")
	                .and()
	             
	                	.csrf()
	                	.disable()
	                .headers()
	                	.frameOptions().disable()
	                .and()
	                .exceptionHandling()
	                	
	                	.accessDeniedPage("/login")
	                
	                
	             	;
	        
	    }

	  
	    @Bean
	    public DefaultWebSecurityExpressionHandler defaultExpressinonhandler() {
	    	return new DefaultWebSecurityExpressionHandler();
	    }
	    // tu podany jest adres gdzie odsyłać gdy user nie ma dostępu
	    @Bean
	    public LoginUrlAuthenticationEntryPoint loginEntryPoint() {
	    	return new LoginUrlAuthenticationEntryPoint("/login");
	    }
	    @Bean 
	    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
	    	SavedRequestAwareAuthenticationSuccessHandler succHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	    	succHandler.setDefaultTargetUrl("/appointments/");
	    	//succHandler.setAlwaysUseDefaultTargetUrl(true);
	    	CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
	    	
	    	SimpleUrlAuthenticationFailureHandler failHandler = new SimpleUrlAuthenticationFailureHandler();
	    	failHandler.setDefaultFailureUrl("/login?error=true");
	    	filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
	    	filter.setAuthenticationManager(this.authenticationManager());
	    	filter.setUsernameParameter("custom_username");
	    	filter.setPasswordParameter("custom_password");
	    	filter.setAuthenticationSuccessHandler(succHandler);
	    	filter.setAuthenticationFailureHandler(failHandler);
	    	
	    	return filter;
	    }
	    @Override
	    protected void configure(
	      AuthenticationManagerBuilder auth) throws Exception {
	    		auth.ldapAuthentication()
	    			.contextSource()
	    				.port(10389)
	    				.root("dc=oreilly,dc=com")
	    				.managerDn("uid=admin,ou=system")
	    				.managerPassword("secret")
	    			.and()
	    				//.userDnPatterns("uid={0},ou=people")
	    				.userSearchFilter("(uid={0})")
	    				.groupSearchBase("ou=groups")
	    				.userDetailsContextMapper(userDetailsContextMapper)
	    				
	        	;
	        
	    }
	    @Bean
	    @Primary
	    public DataSource dataSource() {
	    	return  new EmbeddedDatabaseBuilder()
	    			.addScript("schema-group-h2.sql")
	    			.setType(EmbeddedDatabaseType.H2)
	    			.build();
	    	 
	    }
	  
	   
	    
}
