package com.oreilly.security.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.Appointment;
import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.entities.Automobile;
import com.oreilly.security.domain.entities.Service;
import com.oreilly.security.domain.repositories.AppointmentRepository;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import com.oreilly.security.domain.repositories.ServiceRepository;
@Component
public class UserBootstrap implements CommandLineRunner{
	private AutoUserRepository repository;
	private ServiceRepository serviceRepository;
	private AppointmentRepository appointmentRepository;
	public UserBootstrap() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	public UserBootstrap(AutoUserRepository repository, ServiceRepository serviceRepository,
			AppointmentRepository appointmentRepository) {
		super();
		this.repository = repository;
		this.serviceRepository = serviceRepository;
		this.appointmentRepository = appointmentRepository;
	}


	@Override
	public void run(String... args) throws Exception {
		AutoUser kevinM = new AutoUser("Kevin", "Bowersox", "kbowersox","password", "kevin.m.bowersox@gmail.com","ROLE_ADMIN", null);
		AutoUser johnD = new AutoUser("John","Doe","jodoe", "password", "johndoe@gmail.com", "ROLE_USER", null);
		AutoUser janeD = new AutoUser("Jane", "Doe", "jadoe", "password", "janedoe@gmail.com", "ROLE_USER", null);
		AutoUser juniorD = new AutoUser("Junior", "Doe", "jrdoe", "password", "juniordoe@gmail.com", "ROLE_USER", null);
		repository.saveAll(Arrays.asList(johnD,janeD,juniorD, kevinM));
		Service bulb = new Service("Bulb Change");
		Service oil = new Service("Oil Change");
		Service tire = new Service("Tire Change");
		Service antifreeze = new Service("Antifreeze Change");
		
		serviceRepository.saveAll(Arrays.asList(oil,tire, bulb, antifreeze));
		populateAppointments(oil, kevinM,janeD);
		
		
	}
	
	private void populateAppointments(Service service, AutoUser ...autoUsers) {
		
		for (AutoUser autoUser : autoUsers) {
			Appointment appointment = new Appointment();
			appointment.setAppointmentDt(LocalDate.now());
			appointment.setAutomobile(new Automobile("Subaru","WRX", (short) 2005));
			appointment.setStatus("INITIAL");
			appointmentRepository.save(appointment);
			appointment.setUser(autoUser);
			appointment.getServices().add(service);
			appointmentRepository.save(appointment);
		}

	}


}
