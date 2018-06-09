package com.oreilly.security.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.oreilly.security.domain.entities.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

}
