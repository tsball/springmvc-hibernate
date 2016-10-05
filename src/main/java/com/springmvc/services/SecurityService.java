package com.springmvc.services;

public interface SecurityService {

	String findLoggedInName();
	
	void autologin(String name, String password);
	
}
