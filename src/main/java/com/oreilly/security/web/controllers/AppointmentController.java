package com.oreilly.security.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.security.domain.entities.Appointment;
import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.entities.Service;
import com.oreilly.security.domain.repositories.AppointmentRepository;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import com.oreilly.security.domain.repositories.ServiceRepository;


@Controller()
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private AutoUserRepository autoUserRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@ModelAttribute
	public Appointment getAppointment(){
		return new Appointment();
	}
	

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getAppointmentPage(){
		return "appointments";
	}

	@ResponseBody
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public List<Appointment> saveAppointment(@ModelAttribute Appointment appointment){
		//String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(appointment.toString());
		AutoUser user =(AutoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		appointment.setUser(user);
		appointment.setStatus("Initial");
		appointmentRepository.save(appointment);
		return getAll();
	}
	
	@ResponseBody
	@RequestMapping("/all")
	public List<Appointment> getAppointments(Authentication auth){
		AutoUser savedUser = autoUserRepository.findByUsername(auth.getName());
		return appointmentRepository.findByUser(savedUser);
	}

	@RequestMapping("/{appointmentId}")
	@PostAuthorize("authentication.principal.username == #model[appointment].user.username")
	//@PostAuthorize("returnObject == 'appointment'")
	public String getAppointment(@PathVariable("appointmentId") Long appointmentId, Model model){
		Appointment appointment = appointmentRepository.findById(appointmentId).get();
		model.addAttribute("appointment", appointment);
		return "appointment";
	}
	private List<Appointment> getAll() {
		List<Appointment> appointments = new ArrayList<>();
		appointmentRepository.findAll().iterator().forEachRemaining(appointments::add);
		return appointments;
	}
	@ResponseBody
	@RequestMapping("/confirm")
	@PreAuthorize("hasRole('ADMIN')")
	public String confirm() {
		return "confirmed";
	}

	@ResponseBody
	@RequestMapping("/cancel")
	public String cancel() {
		return "cancelled";
	}

	@ResponseBody
	@RequestMapping("/complete")
	@RolesAllowed("ROLE_ADMIN")
	public String complete() {
		return "completed";
	}
}
