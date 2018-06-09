package com.oreilly.security.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.oreilly.security.domain.entities.Service;
import com.oreilly.security.domain.repositories.ServiceRepository;

@ControllerAdvice
public class MyControllerAdvice {
	@Autowired
	private ServiceRepository serviceRepository;
	public MyControllerAdvice() {
		// TODO Auto-generated constructor stub
	}
	@ModelAttribute("services")
	public List<Service> getServices(){
		return serviceRepository.findAll();
	}

}
