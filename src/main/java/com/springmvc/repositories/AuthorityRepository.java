package com.springmvc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springmvc.models.Authority;

@Repository
@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}