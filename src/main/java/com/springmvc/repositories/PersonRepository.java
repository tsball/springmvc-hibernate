package com.springmvc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springmvc.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	@Query("SELECT p FROM Person p")
	Page<Person> findList(Pageable pageable);
	
}
