package com.springmvc.services;

public interface ISecurityService {

	String findLoggedInName();
	
	void autologin(String name, String password);
	
}
