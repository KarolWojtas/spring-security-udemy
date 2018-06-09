package com.oreilly.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;
@Component
public class CustomUserDetailsService implements UserDetailsService {
	private AutoUserRepository userRespository;
	
	@Autowired
	public CustomUserDetailsService(AutoUserRepository userRespository) {
		super();
		this.userRespository = userRespository;
	}


	public CustomUserDetailsService() {
		super();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRespository.findByUsername(username);
	}

}
