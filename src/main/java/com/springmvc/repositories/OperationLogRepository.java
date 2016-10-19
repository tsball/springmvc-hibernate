package com.springmvc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvc.models.OperationLog;

@Transactional
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
	
}