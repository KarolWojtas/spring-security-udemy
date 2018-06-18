package com.oreilly.security.domain.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.oreilly.security.domain.entities.util.LocalDateConverter;

@Entity
@Table(name = "APPOINTMENT")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPOINTMENT_ID")
	private Long appointmentId;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "AUTO_USER_ID")
	private AutoUser user;

	@Embedded
	private Automobile automobile;

	@Column(name = "APPOINTMENT_DT")
	@Convert(converter = LocalDateConverter.class)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate appointmentDt;

	//@ElementCollection(fetch = FetchType.EAGER)
	//@CollectionTable(name = "SERVICES", joinColumns = { @JoinColumn(name = "APPOINTMENT_ID") })
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="APPOINTMENT_SERVICES", 
				joinColumns= @JoinColumn(referencedColumnName="APPOINTMENT_ID", name="ID_APPOINTMENT"),
				inverseJoinColumns= @JoinColumn(referencedColumnName="SERVICE_ID", name="ID_SERVICE"))
	
	private Set<Service> services = new HashSet<>();

	@Column(name = "STATUS")
	private String status;

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public AutoUser getUser() {
		return user;
	}

	public void setUser(AutoUser user) {
		
		this.user = user;
	}

	public Automobile getAutomobile() {
		return automobile;
	}

	public void setAutomobile(Automobile automobile) {
		this.automobile = automobile;
	}

	public LocalDate getAppointmentDt() {
		return appointmentDt;
	}

	public void setAppointmentDt(LocalDate appointmentDt) {
		this.appointmentDt = appointmentDt;
	}


	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
