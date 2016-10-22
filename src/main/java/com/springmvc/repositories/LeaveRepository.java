package com.springmvc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springmvc.models.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
	
	@Query(value="SELECT l FROM Leave l JOIN FETCH l.person",
			countQuery="SELECT count(l) FROM Leave l")
	Page<Leave> findList(Pageable pageable);

}
