package com.oreilly.security.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	public CustomAuthenticationFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);
		String make = request.getParameter("make");
		CustomAuthenticationToken token = new CustomAuthenticationToken(username, password, make);
		super.setDetails(request, token);
		return this.getAuthenticationManager().authenticate(token);
	}

	
	
}
