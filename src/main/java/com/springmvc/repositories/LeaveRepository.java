package com.springmvc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springmvc.models.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
	
	@Query(value="SELECT l FROM Leave l JOIN FETCH l.employee",
			countQuery="SELECT count(l) FROM Leave l")
	Page<Leave> findList(Pageable pageable);
	
	/**
	@Query(value = "SELECT * FROM leaves WHERE employee_id = ?1",
		    countQuery = "SELECT count(*) FROM leaves WHERE employee_id = ?1",
		    nativeQuery = true)
	Page<Leave> findApplyList(Long startEmployeeId, Pageable pageable);
	**/

}
