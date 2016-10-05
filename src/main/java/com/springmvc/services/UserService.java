package com.springmvc.services;

import com.springmvc.models.User;

public interface UserService {

	void save(User user);
	
	User findByUsername(String username);
	
}
