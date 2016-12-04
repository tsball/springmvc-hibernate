package com.springmvc.forms;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class EmployeeForm {

	@NotEmpty
    @Size(min=5, max=16)
	private String name;

	@NotEmpty
	private String code;
	
	@NotEmpty
	private Long role;
	
	@NotEmpty
	private Long manager;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public Long getManager() {
		return manager;
	}

	public void setManager(Long manager) {
		this.manager = manager;
	}
	
	
}
