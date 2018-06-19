package com.oreilly.security.domain.entities.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.Appointment;
import com.oreilly.security.domain.entities.AutoUser;

@Component
public class AppointmentUtils {
	
	@PreFilter("authentication.principal.username == filterObject.user.username")
	public String saveAll(List<Appointment> appointments) {
		return appointments.stream().map(a->a.getUser().getEmail().toString())
					.collect(Collectors.joining(" ", "", ""));
	}	
	public static Appointment createAppointment(AutoUser user) {
		Appointment appointment = new Appointment();
		appointment.setUser(user);
		return appointment;
	}

}
