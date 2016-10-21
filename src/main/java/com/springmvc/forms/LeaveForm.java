package com.springmvc.forms;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.springmvc.models.LeaveType;

public class LeaveForm {

	@NotNull
	private LeaveType leaveType;
	
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm")
	private Date beginAt;
	
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm")
	private Date endAt;
	
	@NotNull
	private Integer days;
	
	@NotEmpty
	private String reason;

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public Date getBeginAt() {
		return beginAt;
	}

	public void setBeginAt(Date beginAt) {
		this.beginAt = beginAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
