package com.springmvc.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springmvc.models.Leave;

public interface ILeaveService {
	
	Page<Leave> findApplyList(Long applyPersonId, Pageable pageable);
	
}
