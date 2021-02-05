package com.provision.cartrack.actions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.provision.cartrack.helpers.JSONable;
import com.provision.cartrack.security.AuditedTable;
import com.provision.cartrack.security.User;

@Entity
@Table(name = "events")
public class Event extends AuditedTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private EventType type;

	@Column(columnDefinition = "TEXT")
	private String payload;

	public Event() {
	}

	public Event(User user, EventType type, JSONable obj) {
		super();
		this.auditCreation(user);
		this.type = type;
		this.payload = obj.toJSON();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public static enum EventType {
		READING_ADDED, READING_UPDATED, READING_REMOVED,

		TASK_ADDED, TASK_UPDATED, TASK_REMOVED
	}
}
