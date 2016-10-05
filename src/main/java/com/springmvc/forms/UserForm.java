package com.springmvc.forms;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class UserForm {

	@NotEmpty
	@Size(min=6, max=20)
	private String username;

	@NotEmpty
	@Size(min=6, max=20)
	private String password;
	
	private String passwordConfirm;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
}
