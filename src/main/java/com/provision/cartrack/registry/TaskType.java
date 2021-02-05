package com.provision.cartrack.registry;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

@Entity
@Table(name = "task_types")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class TaskType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "task_name", unique = true, columnDefinition = "TEXT")
	private String name;

	@Type(type = "list-array")
	@Column(name = "allowed_engine_types", columnDefinition = "bigint[]")
	private List<Long> allowedEngineTypes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getAllowedEngineTypes() {
		return allowedEngineTypes;
	}

	public void setAllowedEngineTypes(List<Long> allowedEngineTypes) {
		this.allowedEngineTypes = allowedEngineTypes;
	}

}