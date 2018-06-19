package com.oreilly.security.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.oreilly.security.domain.entities.Appointment;
import com.oreilly.security.domain.entities.AutoUser;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	public List<Appointment> findByUser(@Param("user") AutoUser user);
}
