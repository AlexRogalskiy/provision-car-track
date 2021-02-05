package com.provision.cartrack.registry.vehicles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.provision.cartrack.registry.models.Model;
import com.provision.cartrack.security.AuditedTable;

@Entity
@Table(name = "vehicles")
public class Vehicle extends AuditedTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "serial_number", unique = true, columnDefinition = "TEXT")
	private String serialNumber;

	@ManyToOne(optional = false)
	@JoinColumn(name = "model_id", nullable = false)
	private Model model;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}