package com.oreilly.security.config;

import javax.servlet.FilterConfig;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.oreilly.security.services.CustomAuthenticationFilter;
import com.oreilly.security.services.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationProvider provider;
	@Autowired
	private UserDetailsService userService;

	public WebSecurityConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/appointments/").hasAnyRole("USER", "ADMIN").antMatchers("/schedule/").hasAnyRole("ADMIN")
				.antMatchers("/h2-console").permitAll().antMatchers("/**").hasAnyRole("ANONYMOUS", "USER", "ADMIN")
				.and()
				/*
				 * .formLogin() .loginPage("/login") .loginProcessingUrl("/login")
				 * .usernameParameter("custom_username") .passwordParameter("custom_password")
				 * .defaultSuccessUrl("/appointments/") .failureUrl("/login?error=true")
				 */
				// .and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").and()
				.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.userDetailsService(userService).csrf().disable().headers().frameOptions().disable().and()
				.exceptionHandling().authenticationEntryPoint(loginEntryPoint()).accessDeniedPage("/login");

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
		// succHandler.setAlwaysUseDefaultTargetUrl(true);
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
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(provider);

	}


	

}
