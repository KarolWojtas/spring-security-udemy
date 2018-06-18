package com.oreilly.security.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.oreilly.security.domain.entities.AutoUser;

public interface AutoUserRepository extends CrudRepository<AutoUser, Long> {
	public AutoUser findByUsername(String username);
	public AutoUser findByUsernameIsLike(String word);
}
