package com.oreilly.security.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
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
	public List<Appointment> getAppointments(){
		return getAll();
	}

	@RequestMapping("/{appointmentId}")
	@PostAuthorize("hasPermission(#model['appointment'],'read')")
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
	
}
