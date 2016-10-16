package com.springmvc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvc.models.Resource;

@Transactional
public interface ResourceRepository extends JpaRepository<Resource, Long> {
	
	Iterable<Resource> findByControllerMappingAndHttpMethod(String controllerMapping, String httpMethod);
	
}