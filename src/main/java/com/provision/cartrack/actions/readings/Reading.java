package com.provision.cartrack.actions.readings;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.provision.cartrack.registry.ReadingType;
import com.provision.cartrack.registry.vehicles.Vehicle;
import com.provision.cartrack.security.AuditedTable;

@Entity
@Table(name = "readings")
public class Reading extends AuditedTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "odometer_value")
	private Integer odometerValue;

	@Column(name = "reading_value")
	private BigDecimal readingValue;

	@ManyToOne(optional = false)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;

	@ManyToOne(optional = false)
	@JoinColumn(name = "reading_type_id", nullable = false)
	private ReadingType readingType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "execution_date", nullable = false)
	private Date executionDate;

	@Column(name = "external_reference", columnDefinition = "TEXT")
	private String externalReference;

	public void invariantsValidation() {
		if (executionDate.after(new Date()))
			throw new IllegalArgumentException("Execution Date cannot be in the future.");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOdometerValue() {
		return odometerValue;
	}

	public void setOdometerValue(Integer odometerValue) {
		this.odometerValue = odometerValue;
	}

	public BigDecimal getReadingValue() {
		return readingValue;
	}

	public void setReadingValue(BigDecimal readingValue) {
		this.readingValue = readingValue;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public ReadingType getReadingType() {
		return readingType;
	}

	public void setReadingType(ReadingType readingType) {
		this.readingType = readingType;
	}

	public Date getExecutionDate() {
		return executionDate;
	}
}