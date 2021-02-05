package com.provision.cartrack.registry.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.provision.cartrack.registry.EngineType;
import com.provision.cartrack.registry.Make;
import com.provision.cartrack.security.AuditedTable;

@Entity
@Table(name = "models")
public class Model extends AuditedTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "model_name", columnDefinition = "TEXT")
	private String name;

	@Column(name = "model_year")
	private Integer year;

	@ManyToOne(optional = false)
	@JoinColumn(name = "make_id", nullable = false)
	private Make make;

	@ManyToOne(optional = false)
	@JoinColumn(name = "engine_type_id", nullable = false)
	private EngineType engineType;

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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Make getMake() {
		return make;
	}

	public void setMake(Make make) {
		this.make = make;
	}

	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

}