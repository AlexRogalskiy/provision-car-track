package com.provision.cartrack.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class AuditedTable {

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_created_by_id", nullable = false)
	@JsonIgnore
	private User createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	public void auditCreation(User user) {
		this.createdAt = new Date();
		this.createdBy = user;
	}

}
