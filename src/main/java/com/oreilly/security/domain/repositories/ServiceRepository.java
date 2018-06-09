package com.oreilly.security.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.oreilly.security.domain.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
