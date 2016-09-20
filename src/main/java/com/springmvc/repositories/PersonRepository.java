package com.springmvc.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springmvc.models.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
	
	Person findByUsername(String username);
	
}
