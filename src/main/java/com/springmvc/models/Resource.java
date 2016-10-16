package com.springmvc.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "resources")
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, unique = true)
	private String name;
	
	// use for speed up the query
	@Column(nullable = false)
	private String controllerMapping;
	
	@Column(nullable = false)
	private String httpMethod;
	
	@Column(nullable = false)
	private String urlPattern;
	
	// /users/add and users/create own the same authority named 'user create'
	@ManyToOne
	@JoinColumn(name="authority_id")
	private Authority authority;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getControllerMapping() {
		return controllerMapping;
	}

	public void setControllerMapping(String controllerMapping) {
		this.controllerMapping = controllerMapping;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
}
