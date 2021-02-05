package com.provision.cartrack.actions.readings;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.provision.cartrack.helpers.JSONable;

public class ReadingRequest implements JSONable {

	@NotBlank
	@Size(min = 3, max = 60)
	private String vehicleSerialNumber;

	@NotBlank
	private String readingType;

	@NotNull
	private Date executionDate;

	@NotBlank
	private String externalReference;

	@NotNull
	private BigDecimal readingValue;

	@NotNull
	@Min(0)
	private Integer odometerValue;

	public String getVehicleSerialNumber() {
		return vehicleSerialNumber;
	}

	public void setVehicleSerialNumber(String vehicleSerialNumber) {
		this.vehicleSerialNumber = vehicleSerialNumber;
	}

	public String getReadingType() {
		return readingType;
	}

	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public BigDecimal getReadingValue() {
		return readingValue;
	}

	public void setReadingValue(BigDecimal readingValue) {
		this.readingValue = readingValue;
	}

	public Integer getOdometerValue() {
		return odometerValue;
	}

	public void setOdometerValue(Integer odometerValue) {
		this.odometerValue = odometerValue;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	@Override
	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
