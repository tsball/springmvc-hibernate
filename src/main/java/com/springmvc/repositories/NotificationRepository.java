package com.springmvc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvc.models.Notification;

@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
}