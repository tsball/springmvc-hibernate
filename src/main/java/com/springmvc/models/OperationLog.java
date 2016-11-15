package com.springmvc.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operation_logs")
public class OperationLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OperationLogRiskLevel riskLevel;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OperationLogEvent event;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OperationLogCategory category;
	
	@Column(nullable = false)
	private String message;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false)
	private Timestamp createdAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OperationLogRiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(OperationLogRiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public OperationLogEvent getEvent() {
		return event;
	}

	public void setEvent(OperationLogEvent event) {
		this.event = event;
	}

	public OperationLogCategory getCategory() {
		return category;
	}

	public void setCategory(OperationLogCategory category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	
}
