package com.springmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvc.models.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
	
}
