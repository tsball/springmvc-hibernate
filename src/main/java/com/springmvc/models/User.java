package com.springmvc.models;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;
	
	//@Transient
	//private String passwordConfirm;
	
	@ManyToMany//(fetch=FetchType.EAGER)
    //@JoinTable(name="users_roles",
    //    joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
    //    inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName="id"))
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>(0);
	
	@OneToMany(mappedBy="user", orphanRemoval=true)
    private Set<OperationLog> operationLogs = new HashSet<OperationLog>(0);
	
	@ManyToOne
	//@Fetch(FetchMode.JOIN)
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@ManyToMany(mappedBy="users")
	private Set<Notification> notifications = new HashSet<Notification>(0);
	
	@Column(nullable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	private Timestamp createdAt;
	
	@Column(nullable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	private Timestamp updatedAt;
	
	@Transient
	private List<GrantedAuthority> authorities = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<OperationLog> getOperationLogs() {
		return operationLogs;
	}

	public void setOperationLogs(Set<OperationLog> operationLogs) {
		this.operationLogs = operationLogs;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return authorities;
	}
	
	public void setAuthorities(List<GrantedAuthority> authorities) {
	    this.authorities = authorities;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

}