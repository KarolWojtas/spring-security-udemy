package com.oreilly.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.entities.Service;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import com.oreilly.security.domain.repositories.ServiceRepository;
@Component
public class UserBootstrap implements CommandLineRunner{
	private AutoUserRepository repository;
	private ServiceRepository serviceRepository;
	public UserBootstrap() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	public UserBootstrap(AutoUserRepository repository, ServiceRepository serviceRepository) {
		super();
		this.repository = repository;
		this.serviceRepository = serviceRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		AutoUser kevinM = new AutoUser("Kevin", "Bowersox", "kbowersox","password", "kevin.m.bowersox@gmail.com","ROLE_ADMIN", null);
		AutoUser johnD = new AutoUser("John","Doe","jodoe", "password", "johndoe@gmail.com", "ROLE_USER", null);
		AutoUser janeD = new AutoUser("Jane", "Doe", "jadoe", "password", "janedoe@gmail.com", "ROLE_USER", null);
		AutoUser juniorD = new AutoUser("Junior", "Doe", "jrdoe", "password", "juniordoe@gmail.com", "ROLE_USER", null);
		repository.saveAll(Arrays.asList(johnD,janeD,juniorD, kevinM));
		Service oil = new Service("Oil Change");
		Service tire = new Service("Tire Change");
		Service antifreeze = new Service("Antifreeze Change");
		Service bulb = new Service("Bulb Change");
		serviceRepository.saveAll(Arrays.asList(oil,tire, bulb, antifreeze));
		
		
	}
	

}
