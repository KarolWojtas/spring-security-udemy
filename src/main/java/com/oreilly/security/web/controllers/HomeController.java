package com.oreilly.security.web.controllers;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private AutoUserRepository userRepository;
	public HomeController(AutoUserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	@GetMapping
	public String goHome(){
		return "home";
	}
	//@PreAuthorize("hasRole('USER')")
	@RequestMapping("/services")
	public String goServices(){
		return "services";
	}
	@GetMapping("/login")
	public String goLogin(){
		
		return "login";
	}
	@RequestMapping("/schedule")
	public String goSchedule(){
		return "schedule";
	}
	@GetMapping("/register")
	public String goRegister() {
		return "register";
	}
	@PostMapping("/register")
	public String register(@ModelAttribute AutoUser user) {
		user.setRole("ROLE_USER");
		userRepository.save(user);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		return "home";
	}
	
}
