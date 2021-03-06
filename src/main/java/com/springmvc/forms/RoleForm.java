package com.springmvc.forms;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.springmvc.models.RoleCode;

public class RoleForm {

	@NotEmpty
	@Size(min=6, max=20)
	private String name;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private RoleCode code;
	
	@NotNull
	private Set<Long> authorities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RoleCode getCode() {
		return code;
	}

	public void setCode(RoleCode code) {
		this.code = code;
	}

	public Set<Long> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Long> authorities) {
		this.authorities = authorities;
	}
	
}
