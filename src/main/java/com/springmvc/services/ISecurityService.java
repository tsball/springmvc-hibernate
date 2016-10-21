package com.springmvc.services;

import com.springmvc.models.User;

public interface ISecurityService {

	User findLoggedInUser();
	
	void autologin(String name, String password);
	
}
