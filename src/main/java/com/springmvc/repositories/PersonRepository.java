package com.springmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvc.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Person findByUsername(String username);
	
}
