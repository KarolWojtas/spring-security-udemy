package com.oreilly.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;
@Component(value="customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider{
	private AutoUserRepository repo;
	private BCryptPasswordEncoder encoder;
	public CustomAuthenticationProvider() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	public CustomAuthenticationProvider(AutoUserRepository repo) {
		super();
		this.repo = repo;
		encoder = new BCryptPasswordEncoder();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;
		AutoUser user = repo.findByUsername(token.getName());
		String encPasswordFromDB = encoder.encode(user.getPassword());
		if(user == null || (!user.getPassword().equalsIgnoreCase(token.getCredentials().toString())
				|| !token.getMake().equalsIgnoreCase("subaru"))){
			throw new BadCredentialsException("The credentials are invalid");
		}
		return new CustomAuthenticationToken(user, user.getPassword(),user.getAuthorities(),token.getMake());
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return CustomAuthenticationToken.class.equals(arg0);
	}

}
