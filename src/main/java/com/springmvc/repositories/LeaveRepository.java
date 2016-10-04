package com.springmvc.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springmvc.models.Leave;

public interface LeaveRepository extends CrudRepository<Leave, Long> {
	
}
